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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.Question;
import csns.model.qa.QuestionSection;
import csns.model.qa.RatingQuestion;
import csns.model.qa.TextQuestion;
import csns.model.survey.Survey;
import csns.model.survey.SurveyType;
import csns.model.survey.dao.SurveyDao;
import csns.security.SecurityUtils;
import csns.web.editor.CalendarPropertyEditor;
import csns.web.validator.QuestionValidator;
import csns.web.validator.SurveyValidator;

@Controller
@SessionAttributes({ "survey", "question", "questionSection" })
public class SurveyController {

    @Autowired
    SurveyDao surveyDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    SurveyValidator surveyValidator;

    @Autowired
    QuestionValidator questionValidator;

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Calendar.class,
            new CalendarPropertyEditor( "MM/dd/yyyy" ) );
    }

    @RequestMapping("/department/{dept}/survey/current")
    public String current( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "department", department );
        models.put( "surveys", surveyDao.getCurrentSurveys( department ) );
        return "survey/current";
    }

    @RequestMapping("/department/{dept}/survey/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "department", department );
        models.put( "surveys", surveyDao.getSurveys( department ) );
        return "survey/list";
    }

    @RequestMapping("/department/{dept}/survey/view")
    public String view( @RequestParam Long id,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "survey", surveyDao.getSurvey( id ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "survey/view";
    }

    @RequestMapping("/department/{dept}/survey/clone")
    public String clone( @PathVariable String dept, @RequestParam Long id )
    {
        Survey oldSurvey = surveyDao.getSurvey( id );
        Survey newSurvey = oldSurvey.clone();
        newSurvey.setDepartment( departmentDao.getDepartment( dept ) );
        newSurvey.setAuthor( SecurityUtils.getUser() );
        newSurvey.setDate( new Date() );
        newSurvey = surveyDao.saveSurvey( newSurvey );

        return "redirect:edit?id=" + newSurvey.getId();
    }

    @RequestMapping(value = "/department/{dept}/survey/create",
        method = RequestMethod.GET)
    public String create( ModelMap models )
    {
        models.put( "survey", new Survey() );
        models.put( "surveyTypes", SurveyType.values() );
        return "survey/create";
    }

    @RequestMapping(value = "/department/{dept}/survey/create",
        method = RequestMethod.POST)
    public String create( @ModelAttribute Survey survey,
        @PathVariable String dept, BindingResult result,
        SessionStatus sessionStatus )
    {
        surveyValidator.validate( survey, result );
        if( result.hasErrors() ) return "survey/create";

        survey.setDepartment( departmentDao.getDepartment( dept ) );
        survey.setAuthor( SecurityUtils.getUser() );
        survey.setDate( new Date() );
        survey = surveyDao.saveSurvey( survey );

        sessionStatus.setComplete();
        return "redirect:editQuestionSheet?surveyId=" + survey.getId();
    }

    @RequestMapping(value = "/department/{dept}/survey/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "survey", surveyDao.getSurvey( id ) );
        models.put( "surveyTypes", SurveyType.values() );
        return "survey/edit";
    }

    @RequestMapping(value = "/department/{dept}/survey/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Survey survey,
        HttpServletRequest request, BindingResult result,
        SessionStatus sessionStatus )
    {
        surveyValidator.validate( survey, result );
        if( result.hasErrors() ) return "survey/edit";

        survey = surveyDao.saveSurvey( survey );
        sessionStatus.setComplete();
        return request.getParameter( "next" ) == null ? "redirect:list"
            : "redirect:editQuestionSheet?surveyId=" + survey.getId();
    }

    @RequestMapping("/department/{dept}/survey/editQuestionSheet")
    public String editQuestions( @RequestParam Long surveyId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "survey", surveyDao.getSurvey( surveyId ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "survey/editQuestionSheet";
    }

    @RequestMapping(value = "/department/{dept}/survey/editSection",
        method = RequestMethod.GET)
    public String editSection( @RequestParam Long surveyId,
        @RequestParam int sectionIndex, ModelMap models )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        models.put( "survey", survey );
        models.put( "questionSection", survey.getQuestionSheet()
            .getSections()
            .get( sectionIndex ) );
        return "survey/editSection";
    }

    @RequestMapping(value = "/department/{dept}/survey/editSection",
        method = RequestMethod.POST)
    public String editSection( @ModelAttribute QuestionSection questionSection,
        @RequestParam Long surveyId, @RequestParam int sectionIndex,
        SessionStatus sessionStatus )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        survey.getQuestionSheet()
            .getSections()
            .set( sectionIndex, questionSection );
        surveyDao.saveSurvey( survey );

        sessionStatus.setComplete();
        return "redirect:editQuestionSheet?surveyId=" + surveyId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping("/department/{dept}/survey/deleteSection")
    public String deleteSection( @RequestParam Long surveyId,
        @RequestParam int sectionIndex )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        if( !survey.isPublished() )
        {
            survey.getQuestionSheet().getSections().remove( sectionIndex );
            surveyDao.saveSurvey( survey );
        }

        return "redirect:editQuestionSheet?surveyId=" + surveyId;
    }

    @RequestMapping(value = "/department/{dept}/survey/addQuestion",
        method = RequestMethod.GET)
    public String addQuestion( @PathVariable String dept,
        @RequestParam Long surveyId, @RequestParam String questionType,
        ModelMap models )
    {
        models.put( "survey", surveyDao.getSurvey( surveyId ) );

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

        return "survey/addQuestion";
    }

    @RequestMapping(value = "/department/{dept}/survey/addQuestion",
        method = RequestMethod.POST)
    public String addQuestion( @ModelAttribute("question") Question question,
        @RequestParam Long surveyId, @RequestParam int sectionIndex,
        BindingResult result, SessionStatus sessionStatus )
    {
        questionValidator.validate( question, result );
        if( result.hasErrors() ) return "survey/addQuestion";

        Survey survey = surveyDao.getSurvey( surveyId );
        if( !survey.isPublished() )
        {
            survey.getQuestionSheet()
                .getSections()
                .get( sectionIndex )
                .getQuestions()
                .add( question );
            surveyDao.saveSurvey( survey );
        }

        sessionStatus.setComplete();
        return "redirect:editQuestionSheet?surveyId=" + surveyId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping(value = "/department/{dept}/survey/editQuestion",
        method = RequestMethod.GET)
    public String editQuestion( @RequestParam Long surveyId,
        @RequestParam int sectionIndex, @RequestParam Long questionId,
        ModelMap models )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        Question question = survey.getQuestionSheet()
            .getSections()
            .get( sectionIndex )
            .getQuestion( questionId );

        models.put( "survey", survey );
        models.put( "question", question );

        return "survey/editQuestion";
    }

    @RequestMapping(value = "/department/{dept}/survey/editQuestion",
        method = RequestMethod.POST)
    public String editQuestion( @ModelAttribute("question") Question question,
        @RequestParam Long surveyId, @RequestParam int sectionIndex,
        BindingResult result, SessionStatus sessionStatus )
    {
        questionValidator.validate( question, result );
        if( result.hasErrors() ) return "survey/editQuestion";

        Survey survey = surveyDao.getSurvey( surveyId );
        survey.getQuestionSheet()
            .getSections()
            .get( sectionIndex )
            .replaceQuestion( question );
        surveyDao.saveSurvey( survey );

        sessionStatus.setComplete();
        return "redirect:editQuestionSheet?surveyId=" + surveyId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping("/department/{dept}/survey/reorderQuestion")
    public String reorderQuestion( @RequestParam Long surveyId,
        @RequestParam int sectionIndex, @RequestParam Long questionId,
        @RequestParam int newIndex, HttpServletResponse response )
        throws IOException
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        if( !survey.isPublished() )
        {
            QuestionSection questionSection = survey.getQuestionSheet()
                .getSections()
                .get( sectionIndex );
            Question question = questionSection.removeQuestion( questionId );
            if( question != null )
            {
                questionSection.getQuestions().add( newIndex, question );
                surveyDao.saveSurvey( survey );
            }
        }

        response.setContentType( "text/plain" );
        response.getWriter().print( "" );

        return null;
    }

    @RequestMapping("/department/{dept}/survey/deleteQuestion")
    public String deleteQuestion( @RequestParam Long surveyId,
        @RequestParam int sectionIndex, @RequestParam Long questionId )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        if( !survey.isPublished() )
        {
            survey.getQuestionSheet()
                .getSections()
                .get( sectionIndex )
                .removeQuestion( questionId );
            surveyDao.saveSurvey( survey );
        }

        return "redirect:editQuestionSheet?surveyId=" + surveyId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping("/department/{dept}/survey/remove")
    public String remove( @RequestParam Long id )
    {
        Survey survey = surveyDao.getSurvey( id );
        survey.setDeleted( true );
        surveyDao.saveSurvey( survey );
        return "redirect:list";
    }

    @RequestMapping("/department/{dept}/survey/publish")
    public String publish( @RequestParam Long id, HttpServletResponse response )
        throws IOException
    {
        Survey survey = surveyDao.getSurvey( id );
        if( !survey.isPublished() )
        {
            survey.setPublishDate( Calendar.getInstance() );
            survey = surveyDao.saveSurvey( survey );
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        response.setContentType( "text/plain" );
        response.getWriter().print(
            dateFormat.format( survey.getPublishDate().getTime() ) );

        return null;
    }

    @RequestMapping("/department/{dept}/survey/close")
    public String close( @RequestParam Long id, HttpServletResponse response )
        throws IOException
    {
        Survey survey = surveyDao.getSurvey( id );
        if( !survey.isClosed() )
        {
            survey.setCloseDate( Calendar.getInstance() );
            survey = surveyDao.saveSurvey( survey );
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        response.setContentType( "text/plain" );
        response.getWriter().print(
            dateFormat.format( survey.getCloseDate().getTime() ) );

        return null;
    }

    @RequestMapping("/department/{dept}/survey/results")
    @PreAuthorize("principal.isFaculty(#dept)")
    public String results( @PathVariable String dept, @RequestParam Long id,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "survey", surveyDao.getSurvey( id ) );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "survey/results";
    }

}
