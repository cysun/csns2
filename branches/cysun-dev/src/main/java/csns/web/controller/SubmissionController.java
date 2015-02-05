/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
 * 
 * CSNS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * CSNS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CSNS. If not, see http://www.gnu.org/licenses/agpl.html.
 */
package csns.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Assignment;
import csns.model.academics.Enrollment;
import csns.model.academics.OnlineAssignment;
import csns.model.academics.OnlineSubmission;
import csns.model.academics.Submission;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SubmissionDao;
import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.FileDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.editor.CalendarPropertyEditor;

@Controller
public class SubmissionController {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private FileIO fileIO;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("#{applicationProperties.encoding}")
    private String appEncoding;

    private static final Logger logger = LoggerFactory.getLogger( SubmissionController.class );

    @InitBinder
    public void initBinder( WebDataBinder binder, WebRequest request )
    {
        binder.registerCustomEditor( Calendar.class,
            new CalendarPropertyEditor( "MM/dd/yyyy HH:mm:ss" ) );
    }

    @RequestMapping(value = "/submission/view", params = "id")
    public String view1( @RequestParam Long id, ModelMap models )
    {
        models.put( "submission", submissionDao.getSubmission( id ) );
        return "submission/view";
    }

    @RequestMapping(value = "/submission/view", params = "assignmentId")
    public String view2( @RequestParam Long assignmentId, ModelMap models )
    {
        User user = SecurityUtils.getUser();
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        Submission submission = submissionDao.getSubmission( user, assignment );
        if( submission == null )
            submission = submissionDao.saveSubmission( new Submission( user,
                assignment ) );

        models.put( "submission", submission );
        return "submission/view";
    }

    @RequestMapping(value = "/submission/description")
    public String description( @RequestParam Long assignmentId,
        ModelMap models, HttpServletResponse response )
    {
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            models.put( "message", "error.assignment.unpublished" );
            return "error";
        }
        if( assignment.isPastDue() && !assignment.isAvailableAfterDueDate() )
        {
            models.put( "message", "error.assignment.unavailable" );
            return "error";
        }

        switch( assignment.getDescription().getType() )
        {
            case TEXT:
                models.put( "assignment", assignment );
                return "submission/description";

            case FILE:
                fileIO.write( assignment.getDescription().getFile(), response );
                return null;

            case URL:
                return "redirect:" + assignment.getDescription().getUrl();

            default:
                logger.warn( "Invalid resource type: "
                    + assignment.getDescription().getType() );
                models.put( "message", "error.resource.type.invalid" );
                return "error";
        }
    }

    @RequestMapping(value = "/submission/add", method = RequestMethod.GET)
    public String upload( @RequestParam Long id, ModelMap models )
    {
        models.put( "submission", submissionDao.getSubmission( id ) );
        return "submission/add";
    }

    @RequestMapping(value = "/submission/upload", method = RequestMethod.POST)
    public String upload( @RequestParam Long id,
        @RequestParam boolean additional,
        @RequestParam MultipartFile uploadedFile, ModelMap models )
    {
        User user = SecurityUtils.getUser();
        Submission submission = submissionDao.getSubmission( id );
        boolean isInstructor = submission.getAssignment()
            .getSection()
            .isInstructor( user );
        String view = additional && isInstructor ? "/submission/add?id=" + id
            : "/submission/view?id=" + id;

        if( !isInstructor && submission.isPastDue() )
        {
            models.put( "message", "error.assignment.pastdue" );
            models.put( "backUrl", view );
            return "error";
        }

        if( !uploadedFile.isEmpty() )
        {
            String fileName = uploadedFile.getOriginalFilename();
            if( !isInstructor
                && !submission.getAssignment().isFileExtensionAllowed(
                    File.getFileExtension( fileName ) ) )
            {
                models.put( "message", "error.assignment.file.type" );
                models.put( "backUrl", view );
                return "error";
            }

            File file = new File();
            file.setName( fileName );
            file.setType( uploadedFile.getContentType() );
            file.setSize( uploadedFile.getSize() );
            file.setDate( new Date() );
            file.setOwner( submission.getStudent() );
            file.setSubmission( submission );
            file = fileDao.saveFile( file );
            fileIO.save( file, uploadedFile );

            submission.incrementFileCount();
            submissionDao.saveSubmission( submission );

            logger.info( user.getUsername() + " uploaded file " + file.getId() );
        }

        return "redirect:" + view;
    }

    @RequestMapping("/submission/remove")
    public @ResponseBody
    String remove( @RequestParam Long fileId )
    {
        User user = SecurityUtils.getUser();
        File file = fileDao.getFile( fileId );
        Submission submission = file.getSubmission();
        if( !submission.isPastDue()
            || submission.getAssignment().getSection().isInstructor( user ) )
        {
            file.setSubmission( null );
            if( submission.getFiles().remove( file ) )
                submission.decrementFileCount();
            submissionDao.saveSubmission( submission );
        }

        logger.info( user.getUsername() + " removed file " + file.getId()
            + " from submission " + submission.getId() );

        return "";
    }

    @RequestMapping("/submission/list")
    public String list( @RequestParam Long assignmentId, ModelMap models )
    {
        Assignment assignment = assignmentDao.getAssignment( assignmentId );

        Set<User> students = new HashSet<User>();
        for( Enrollment enrollment : assignment.getSection().getEnrollments() )
            students.add( enrollment.getStudent() );

        // we need to remove the submissions from the students who already
        // dropped the class
        Iterator<Submission> i = assignment.getSubmissions().iterator();
        while( i.hasNext() )
        {
            Submission submission = i.next();
            if( !students.contains( submission.getStudent() ) )
                i.remove();
            else
                students.remove( submission.getStudent() );
        }

        // we then add a submission for each student who is in the class but
        // didn't have a submission for this assignment.
        for( User student : students )
        {
            Submission submission = assignment.isOnline()
                ? new OnlineSubmission( student, (OnlineAssignment) assignment )
                : new Submission( student, assignment );
            assignment.getSubmissions().add( submission );
        }
        assignment = assignmentDao.saveAssignment( assignment );

        models.put( "assignment", assignment );
        return "submission/list";
    }

    @RequestMapping("/submission/grade")
    public String grade( @RequestParam Long id, ModelMap models )
    {
        models.put( "submission", submissionDao.getSubmission( id ) );
        return "submission/grade";
    }

    @RequestMapping(value = "/submission/edit", params = "dueDate")
    public String editDueDate( @RequestParam Long id,
        @RequestParam Calendar dueDate )
    {
        Submission submission = submissionDao.getSubmission( id );
        submission.setDueDate( dueDate );
        submissionDao.saveSubmission( submission );

        return submission.isOnline() ? "redirect:/submission/online/grade?id="
            + id : "redirect:/submission/grade?id=" + id;
    }

    @RequestMapping(value = "/submission/edit", params = "grade")
    public String editGrade( @RequestParam Long id, @RequestParam String grade,
        HttpServletResponse response ) throws IOException
    {
        grade = grade.trim();
        Submission submission = submissionDao.getSubmission( id );
        String oldGrade = submission.getGrade();
        if( oldGrade == null || !oldGrade.equals( grade ) )
        {
            submission.setGrade( grade );
            submission.setGradeMailed( false );
            submissionDao.saveSubmission( submission );
        }

        response.setContentType( "text/plain" );
        response.getWriter().print( submission.getGrade() );
        return null;
    }

    @RequestMapping(value = "/submission/edit", params = "comments")
    public String editComments( @RequestParam Long id,
        @RequestParam String comments, HttpServletResponse response )
        throws IOException
    {
        Submission submission = submissionDao.getSubmission( id );
        submission.setComments( comments );
        submissionDao.saveSubmission( submission );

        response.setContentType( "text/plain" );
        response.getWriter().print( submission.getComments() );
        return null;
    }

    private void emailGrade( Submission submission )
    {
        if( !StringUtils.hasText( submission.getGrade() )
            || submission.isGradeMailed() ) return;

        User instructor = SecurityUtils.getUser();
        User student = submission.getStudent();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom( instructor.getEmail() );
        message.setTo( student.getEmail() );

        String subject = submission.getAssignment()
            .getSection()
            .getCourse()
            .getCode()
            + " " + submission.getAssignment().getName() + " Grade";
        message.setSubject( subject );

        Map<String, Object> vModels = new HashMap<String, Object>();
        vModels.put( "grade", submission.getGrade() );
        String comments = submission.getComments();
        vModels.put( "comments", comments != null ? comments : "" );
        String text = VelocityEngineUtils.mergeTemplateIntoString(
            velocityEngine, "email.submission.grade.vm", appEncoding, vModels );
        message.setText( text );

        try
        {
            mailSender.send( message );
            submission.setGradeMailed( true );
            submissionDao.saveSubmission( submission );
            logger.info( instructor.getUsername() + " sent grade to "
                + student.getEmail() );
        }
        catch( MailException e )
        {
            logger.warn( instructor.getUsername() + " failed to send grade to "
                + student.getEmail() );
            logger.debug( e.getMessage() );
        }
    }

    @RequestMapping(value = "/submission/email", params = "submissionId")
    public String emailGrade( @RequestParam Long submissionId )
    {
        Submission submission = submissionDao.getSubmission( submissionId );
        emailGrade( submission );
        return submission.isOnline() ? "redirect:/submission/online/grade?id="
            + submissionId : "redirect:/submission/grade?id=" + submissionId;
    }

    @RequestMapping(value = "/submission/email", params = "assignmentId")
    public String emailGrades( @RequestParam Long assignmentId )
    {
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        for( Submission submission : assignment.getSubmissions() )
            emailGrade( submission );
        return "redirect:/submission/list?assignmentId=" + assignmentId;
    }

}
