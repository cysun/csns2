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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.Question;
import csns.model.qa.RatingQuestion;
import csns.model.qa.dao.QuestionDao;
import csns.model.survey.Survey;
import csns.model.survey.SurveyResponse;
import csns.model.survey.SurveyType;
import csns.model.survey.dao.SurveyDao;
import csns.model.survey.dao.SurveyResponseDao;
import csns.security.SecurityUtils;
import csns.web.validator.SurveyResponseValidator;

@Controller
@SessionAttributes("response")
public class SurveyResponseController {

    @Autowired
    UserDao userDao;

    @Autowired
    SurveyDao surveyDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    SurveyResponseDao surveyResponseDao;

    @Autowired
    SurveyResponseValidator surveyResponseValidator;

    @RequestMapping(value = "/department/{dept}/survey/response/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long surveyId, ModelMap models )
    {
        Survey survey = surveyDao.getSurvey( surveyId );

        models.put( "backUrl", "../current" );

        if( !survey.isPublished() )
        {
            models.put( "message", "error.survey.unpublished" );
            return "error";
        }

        if( survey.isClosed() )
        {
            models.put( "message", "error.survey.closed" );
            return "error";
        }

        if( !survey.getType().equals( SurveyType.ANONYMOUS )
            && SecurityUtils.isAnonymous() )
        {
            models.put( "message", "error.survey.nonanonymous" );
            return "error";
        }

        if( survey.getType().equals( SurveyType.RECORDED ) )
        {
            User user = userDao.getUser( SecurityUtils.getUser().getId() );
            if( user.getSurveysTaken().contains( survey ) )
            {
                models.put( "message", "error.survey.taken" );
                return "error";
            }
        }

        SurveyResponse response = null;
        if( !survey.getType().equals( SurveyType.ANONYMOUS ) )
            response = surveyResponseDao.getSurveyResponse( survey,
                SecurityUtils.getUser() );
        if( response == null ) response = new SurveyResponse( survey );

        models.put( "response", response );
        models.put( "sectionIndex", 0 );

        return "survey/response/edit";
    }

    @RequestMapping(value = "/department/{dept}/survey/response/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute("response") SurveyResponse response,
        @RequestParam int sectionIndex, HttpServletRequest request,
        ModelMap models, BindingResult result, SessionStatus sessionStatus )
    {
        surveyResponseValidator.validate( response, sectionIndex, result );
        if( !result.hasErrors() )
        {
            if( request.getParameter( "prev" ) != null ) --sectionIndex;
            if( request.getParameter( "next" ) != null ) ++sectionIndex;
        }

        if( !result.hasErrors() && request.getParameter( "finish" ) != null )
        {
            Survey survey = response.getSurvey();
            if( survey.getType().equals( SurveyType.NAMED ) )
                response.getAnswerSheet().setAuthor( SecurityUtils.getUser() );
            if( survey.getType().equals( SurveyType.RECORDED ) )
            {
                User user = userDao.getUser( SecurityUtils.getUser().getId() );
                user.getSurveysTaken().add( survey );
                userDao.saveUser( user );
            }
            response.getAnswerSheet().setDate( new Date() );
            response = surveyResponseDao.saveSurveyResponse( response );

            sessionStatus.setComplete();
            models.put( "message", "status.survey.completed" );
            models.put( "backUrl", "../current" );
            return "status";
        }
        else
        {
            models.put( "response", response );
            models.put( "sectionIndex", sectionIndex );
            return "survey/response/edit";
        }
    }

    @RequestMapping("/department/{dept}/survey/response/view")
    public String view( @RequestParam Long answerSheetId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "response",
            surveyResponseDao.findSurveyResponse( answerSheetId ) );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "survey/response/view";
    }

    @RequestMapping("/department/{dept}/survey/response/list")
    public String list( @RequestParam Long surveyId, ModelMap models )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        models.put( "survey", survey );
        models.put( "responses", survey.getResponses() );
        return "survey/response/list";
    }

    @RequestMapping(value = "/department/{dept}/survey/response/list",
        params = "questionId")
    public String list( @RequestParam Long surveyId,
        @RequestParam Long questionId,
        @RequestParam(required = false) Integer selection,
        @RequestParam(required = false) Integer rating, ModelMap models )
    {
        assert selection != null || rating != null;

        Survey survey = surveyDao.getSurvey( surveyId );
        Question question = questionDao.getQuestion( questionId );
        List<SurveyResponse> responses = selection != null
            ? surveyResponseDao.findSurveyResponses( (ChoiceQuestion) question,
                selection ) : surveyResponseDao.findSurveyResponses(
                (RatingQuestion) question, rating );

        models.put( "survey", survey );
        models.put( "question", question );
        models.put( "responses", responses );
        return "survey/response/list";
    }

}
