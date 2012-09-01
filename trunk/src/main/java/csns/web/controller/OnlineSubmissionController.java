/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012, Chengyu Sun (csun@calstatela.edu).
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.Assignment;
import csns.model.academics.OnlineAssignment;
import csns.model.academics.OnlineSubmission;
import csns.model.academics.Submission;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SubmissionDao;
import csns.model.core.User;
import csns.model.qa.AnswerSheet;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.Question;
import csns.model.qa.RatingQuestion;
import csns.model.qa.dao.AnswerSheetDao;
import csns.model.qa.dao.QuestionDao;
import csns.security.SecurityUtils;

@Controller
@SessionAttributes("submission")
public class OnlineSubmissionController {

    @Autowired
    AssignmentDao assignmentDao;

    @Autowired
    SubmissionDao submissionDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    AnswerSheetDao answerSheetDao;

    @RequestMapping("/submission/online/view")
    public String view( @RequestParam Long assignmentId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPastDue() )
            return "redirect:/submission/online/edit?assignmentId="
                + assignmentId;

        User student = SecurityUtils.getUser();
        OnlineSubmission submission = (OnlineSubmission) submissionDao.getSubmission(
            student, assignment );
        if( submission != null && !submission.isPastDue() )
            return "redirect:/submission/online/edit?assignmentId="
                + assignmentId;

        if( submission == null )
        {
            submission = new OnlineSubmission( student, assignment );
            submission = (OnlineSubmission) submissionDao.saveSubmission( submission );
        }

        models.put( "submission", submission );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "submission/online/view";
    }

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
            submission.creatAnswerSheet();
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
        if( request.getParameter( "save" ) != null )
            submission.setSaved( true );
        if( request.getParameter( "finish" ) != null )
            submission.setFinished( true );
        submission.getAnswerSheet().setAuthor( SecurityUtils.getUser() );
        submission.getAnswerSheet().setDate( new Date() );
        submission = (OnlineSubmission) submissionDao.saveSubmission( submission );

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

    @RequestMapping("/submission/online/grade")
    public String grade1( @RequestParam Long id,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "submission", submissionDao.getSubmission( id ) );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "submission/online/grade";
    }

    @RequestMapping(value = "/submission/online/grade",
        params = "answerSheetId")
    public String grade2( @RequestParam Long answerSheetId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "submission", submissionDao.findSubmission( answerSheetId ) );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "submission/online/grade";
    }

    @RequestMapping("/submission/online/autograde")
    public String autograde1( @RequestParam Long id )
    {
        OnlineSubmission submission = (OnlineSubmission) submissionDao.getSubmission( id );
        submission.grade();
        submissionDao.saveSubmission( submission );
        return "redirect:grade?id=" + id;
    }

    @RequestMapping(value = "/submission/online/autograde",
        params = "assignmentId")
    public String autograde2( @RequestParam Long assignmentId )
    {
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        for( Submission submission : assignment.getSubmissions() )
        {
            ((OnlineSubmission) submission).grade();
            submissionDao.saveSubmission( submission );
        }
        return "redirect:/submission/list?assignmentId=" + assignmentId;
    }

    @RequestMapping("/submission/online/summary")
    public String summary( @RequestParam Long assignmentId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        List<OnlineSubmission> submissions = new ArrayList<OnlineSubmission>();
        for( Submission s : assignment.getSubmissions() )
        {
            OnlineSubmission submission = (OnlineSubmission) s;
            if( submission.isSaved() || submission.isFinished() )
                submissions.add( submission );
        }

        models.put( "assignment", assignment );
        models.put( "submissions", submissions );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "submission/online/summary";
    }

    @RequestMapping("/submission/online/list")
    public String list( @RequestParam Long assignmentId, ModelMap models )
    {
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        List<AnswerSheet> answerSheets = new ArrayList<AnswerSheet>();
        for( Submission submission : assignment.getSubmissions() )
            answerSheets.add( ((OnlineSubmission) submission).getAnswerSheet() );

        models.put( "assignment", assignment );
        models.put( "answerSheets", answerSheets );
        return "submission/online/list";
    }

    @RequestMapping(value = "/submission/online/list", params = "questionId")
    public String list( @RequestParam Long assignmentId,
        @RequestParam Long questionId,
        @RequestParam(required = false) Integer selection,
        @RequestParam(required = false) Integer rating, ModelMap models )
    {
        assert selection != null || rating != null;

        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        Question question = questionDao.getQuestion( questionId );
        List<AnswerSheet> answerSheets = selection != null
            ? answerSheetDao.findAnswerSheets( (ChoiceQuestion) question,
                selection ) : answerSheetDao.findAnswerSheets(
                (RatingQuestion) question, rating );

        models.put( "assignment", assignment );
        models.put( "question", question );
        models.put( "answerSheets", answerSheets );
        return "submission/online/list";
    }

}
