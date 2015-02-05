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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.model.survey.Survey;
import csns.model.survey.SurveyResponse;
import csns.model.survey.SurveyType;
import csns.model.survey.dao.SurveyDao;
import csns.model.survey.dao.SurveyResponseDao;
import csns.security.SecurityUtils;
import csns.web.validator.SurveyResponseValidator;

@Controller
@SessionAttributes("response")
public class SurveyResponseControllerS {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SurveyDao surveyDao;

    @Autowired
    private SurveyResponseDao surveyResponseDao;

    @Autowired
    private SurveyResponseValidator surveyResponseValidator;

    private static final Logger logger = LoggerFactory.getLogger( SurveyResponseControllerS.class );

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

            if( survey.getType() == SurveyType.NAMED )
                logger.info( SecurityUtils.getUser().getUsername()
                    + " completed survey " + survey.getId() );
            else
                logger.info( "A user completed survey " + survey.getId() );

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

}
