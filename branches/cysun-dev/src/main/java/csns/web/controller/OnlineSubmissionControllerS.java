/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.OnlineAssignment;
import csns.model.academics.OnlineSubmission;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SubmissionDao;
import csns.model.core.User;
import csns.security.SecurityUtils;

@Controller
@SessionAttributes("submission")
public class OnlineSubmissionControllerS {

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private SubmissionDao submissionDao;

    private static final Logger logger = LoggerFactory.getLogger( OnlineSubmissionControllerS.class );

    @RequestMapping(value = "/submission/online/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long assignmentId, ModelMap models )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            models.put( "message", "error.assignment.unpublished" );
            return "error";
        }

        User student = SecurityUtils.getUser();
        OnlineSubmission submission = (OnlineSubmission) submissionDao.getSubmission(
            student, assignment );
        if( submission != null && submission.isPastDue() || submission == null
            && assignment.isPastDue() )
        {
            models.put( "message", "error.assignment.pastdue" );
            models.put( "backUrl", "/section/taken" );
            return "error";
        }

        if( submission == null )
        {
            submission = new OnlineSubmission( student, assignment );
            submission = (OnlineSubmission) submissionDao.saveSubmission( submission );
        }

        // If a submission object is created before the assignment is
        // published, the submission would not have an answer sheet field.
        if( submission.getAnswerSheet() == null )
        {
            submission.createAnswerSheet();
            submission = (OnlineSubmission) submissionDao.saveSubmission( submission );
        }

        models.put( "submission", submission );
        models.put( "sectionIndex", 0 );
        return "submission/online/edit";
    }

    @RequestMapping(value = "/submission/online/edit",
        method = RequestMethod.POST)
    public String edit(
        @ModelAttribute("submission") OnlineSubmission submission,
        @RequestParam int sectionIndex, HttpServletRequest request,
        ModelMap models, SessionStatus sessionStatus )
    {
        if( submission.isPastDue() )
        {
            models.put( "message", "error.assignment.pastdue" );
            models.put( "backUrl", "/section/taken" );
            return "error";
        }

        if( request.getParameter( "save" ) != null )
            submission.setSaved( true );
        if( request.getParameter( "finish" ) != null )
            submission.setFinished( true );
        submission.getAnswerSheet().setDate( new Date() );
        submission = (OnlineSubmission) submissionDao.saveSubmission( submission );

        logger.info( SecurityUtils.getUser().getUsername()
            + " edited online submission " + submission.getId() );

        if( request.getParameter( "finish" ) != null )
        {
            sessionStatus.setComplete();
            models.put( "message", "status.assignment.completed" );
            models.put( "backUrl", "/section/taken#section-"
                + submission.getAssignment().getSection().getId() );
            return "status";
        }

        if( request.getParameter( "prev" ) != null ) --sectionIndex;
        if( request.getParameter( "next" ) != null ) ++sectionIndex;

        models.put( "submission", submission );
        models.put( "sectionIndex", sectionIndex );
        return "submission/online/edit";
    }

}
