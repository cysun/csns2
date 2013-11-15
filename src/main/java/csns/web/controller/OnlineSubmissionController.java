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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
public class OnlineSubmissionController {

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AnswerSheetDao answerSheetDao;

    private static final Logger logger = LoggerFactory.getLogger( OnlineSubmissionController.class );

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

        // If a submission object is created before the assignment is
        // published, the submission would not have an answer sheet field.
        if( submission.getAnswerSheet() == null )
        {
            submission.createAnswerSheet();
            submission = (OnlineSubmission) submissionDao.saveSubmission( submission );
        }

        models.put( "submission", submission );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "submission/online/view";
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

        logger.info( SecurityUtils.getUser().getUsername()
            + " autograded online submission " + id );

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

        logger.info( SecurityUtils.getUser().getUsername()
            + " autograded online assignment " + assignmentId );

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

        logger.info( SecurityUtils.getUser().getUsername()
            + " viewed submission summary of online assignment " + assignmentId );

        return "submission/online/summary";
    }

    @RequestMapping("/submission/online/list")
    public String list( @RequestParam Long assignmentId, ModelMap models )
    {
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        List<AnswerSheet> answerSheets = new ArrayList<AnswerSheet>();
        for( Submission s : assignment.getSubmissions() )
        {
            OnlineSubmission submission = (OnlineSubmission) s;
            if( submission.isSaved() || submission.isFinished() )
                answerSheets.add( submission.getAnswerSheet() );
        }

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
