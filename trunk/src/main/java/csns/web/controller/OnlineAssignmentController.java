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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.Assignment;
import csns.model.academics.OnlineAssignment;
import csns.model.academics.Section;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SectionDao;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.Question;
import csns.model.qa.QuestionSection;
import csns.model.qa.RatingQuestion;
import csns.model.qa.TextQuestion;
import csns.web.editor.CalendarPropertyEditor;
import csns.web.validator.AssignmentValidator;
import csns.web.validator.QuestionValidator;

@Controller
@SessionAttributes({ "assignment", "question", "questionSection" })
public class OnlineAssignmentController {

    @Autowired
    SectionDao sectionDao;

    @Autowired
    AssignmentDao assignmentDao;

    @Autowired
    QuestionValidator questionValidator;

    @Autowired
    AssignmentValidator assignmentValidator;

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Calendar.class,
            new CalendarPropertyEditor() );
    }

    @RequestMapping("/assignment/online/list")
    public String list( @RequestParam Long sectionId, ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        models.put( "section", section );

        List<OnlineAssignment> assignments = new ArrayList<OnlineAssignment>();
        for( Assignment assignment : section.getAssignments() )
            if( assignment.isOnline() )
                assignments.add( (OnlineAssignment) assignment );
        models.put( "assignments", assignments );

        return "assignment/online/list";
    }

    @RequestMapping(value = "/assignment/online/create",
        method = RequestMethod.GET)
    public String create( @RequestParam Long sectionId, ModelMap models )
    {
        OnlineAssignment assignment = new OnlineAssignment();
        assignment.setSection( sectionDao.getSection( sectionId ) );
        models.put( "assignment", assignment );
        return "assignment/online/create";
    }

    // Remember that the default @ModelAttribute name is inferred from
    // the parameter type, not the parameter name.
    @RequestMapping(value = "/assignment/online/create",
        method = RequestMethod.POST)
    public String create(
        @ModelAttribute("assignment") OnlineAssignment assignment,
        BindingResult result, SessionStatus sessionStatus )
    {
        assignmentValidator.validate( assignment, result );
        if( result.hasErrors() ) return "assignment/online/create";

        assignment = (OnlineAssignment) assignmentDao.saveAssignment( assignment );
        sessionStatus.setComplete();
        return "redirect:/assignment/online/edit?id=" + assignment.getId();
    }

    @RequestMapping("/assignment/online/view")
    public String view( @RequestParam Long id,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "assignment", assignmentDao.getAssignment( id ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "assignment/online/view";
    }

    @RequestMapping("/assignment/online/edit")
    public String edit( @RequestParam Long id,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "assignment", assignmentDao.getAssignment( id ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "assignment/online/edit";
    }

    @RequestMapping(value = "/assignment/online/editSection",
        method = RequestMethod.GET)
    public String editSection( @RequestParam Long assignmentId,
        @RequestParam int sectionIndex, ModelMap models )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        models.put( "assignment", assignment );
        models.put( "questionSection", assignment.getQuestionSheet()
            .getSections()
            .get( sectionIndex ) );
        return "assignment/online/editSection";
    }

    @RequestMapping(value = "/assignment/online/editSection",
        method = RequestMethod.POST)
    public String editSection( @ModelAttribute QuestionSection questionSection,
        @RequestParam Long assignmentId, @RequestParam int sectionIndex,
        SessionStatus sessionStatus )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        assignment.getQuestionSheet()
            .getSections()
            .set( sectionIndex, questionSection );
        assignmentDao.saveAssignment( assignment );

        sessionStatus.setComplete();
        return "redirect:/assignment/online/edit?id=" + assignmentId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping("/assignment/online/deleteSection")
    public String deleteSection( @RequestParam Long assignmentId,
        @RequestParam int sectionIndex )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            assignment.getQuestionSheet().getSections().remove( sectionIndex );
            assignmentDao.saveAssignment( assignment );
        }

        return "redirect:/assignment/online/edit?id=" + assignmentId;
    }

    @RequestMapping(value = "/assignment/online/addQuestion",
        method = RequestMethod.GET)
    public String addQuestion( @RequestParam Long assignmentId,
        @RequestParam String questionType, ModelMap models )
    {
        models.put( "assignment", assignmentDao.getAssignment( assignmentId ) );

        Question question;
        switch( questionType )
        {
            case "CHOICE":
                question = new ChoiceQuestion();
                break;
            case "RATING":
                question = new RatingQuestion();
                break;
            default:
                question = new TextQuestion();
        }
        models.put( "question", question );

        return "assignment/online/addQuestion";
    }

    @RequestMapping(value = "/assignment/online/addQuestion",
        method = RequestMethod.POST)
    public String addQuestion( @ModelAttribute("question") Question question,
        @RequestParam Long assignmentId, @RequestParam int sectionIndex,
        BindingResult result, SessionStatus sessionStatus )
    {
        questionValidator.validate( question, result );
        if( result.hasErrors() ) return "assignment/online/addQuestion";

        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            assignment.getQuestionSheet()
                .getSections()
                .get( sectionIndex )
                .getQuestions()
                .add( question );
            assignmentDao.saveAssignment( assignment );
        }

        return "redirect:/assignment/online/edit?id=" + assignmentId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping(value = "/assignment/online/editQuestion",
        method = RequestMethod.GET)
    public String editQuestion( @RequestParam Long assignmentId,
        @RequestParam int sectionIndex, @RequestParam Long questionId,
        ModelMap models )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        Question question = assignment.getQuestionSheet()
            .getSections()
            .get( sectionIndex )
            .getQuestion( questionId );
        models.put( "assignment", assignment );
        models.put( "question", question );
        return "assignment/online/editQuestion";
    }

    @RequestMapping(value = "/assignment/online/editQuestion",
        method = RequestMethod.POST)
    public String editQuestion( @ModelAttribute("question") Question question,
        @RequestParam Long assignmentId, @RequestParam int sectionIndex,
        BindingResult result, SessionStatus sessionStatus )
    {
        questionValidator.validate( question, result );
        if( result.hasErrors() ) return "assignment/online/editQuestion";

        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        assignment.getQuestionSheet()
            .getSections()
            .get( sectionIndex )
            .replaceQuestion( question );
        assignmentDao.saveAssignment( assignment );

        return "redirect:/assignment/online/edit?id=" + assignmentId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping("/assignment/online/deleteQuestion")
    public String deleteQuestion( @RequestParam Long assignmentId,
        @RequestParam int sectionIndex, @RequestParam Long questionId )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            assignment.getQuestionSheet()
                .getSections()
                .get( sectionIndex )
                .removeQuestion( questionId );
            assignmentDao.saveAssignment( assignment );
        }

        return "redirect:/assignment/online/edit?id=" + assignmentId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping("/assignment/online/reorderQuestion")
    public String reorderQuestion( @RequestParam Long assignmentId,
        @RequestParam int sectionIndex, @RequestParam Long questionId,
        @RequestParam int newIndex, HttpServletResponse response )
        throws IOException
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            QuestionSection questionSection = assignment.getQuestionSheet()
                .getSections()
                .get( sectionIndex );
            Question question = questionSection.removeQuestion( questionId );
            if( question != null )
            {
                questionSection.getQuestions().add( newIndex, question );
                assignmentDao.saveAssignment( assignment );
            }
        }

        response.setContentType( "text/plain" );
        response.getWriter().print( "" );

        return null;
    }

}
