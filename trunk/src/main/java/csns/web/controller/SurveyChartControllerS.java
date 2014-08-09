/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.dao.DepartmentDao;
import csns.model.core.User;
import csns.model.survey.SurveyChart;
import csns.model.survey.dao.SurveyChartDao;
import csns.security.SecurityUtils;
import csns.web.validator.SurveyChartValidator;

@Controller
@SessionAttributes("chart")
public class SurveyChartControllerS {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SurveyChartDao surveyChartDao;

    @Autowired
    private SurveyChartValidator surveyChartValidator;

    private static final Logger logger = LoggerFactory.getLogger( SurveyChartControllerS.class );

    @RequestMapping(value = "/department/{dept}/survey/chart/create",
        method = RequestMethod.GET)
    public String create( ModelMap models )
    {
        models.put( "chart", new SurveyChart() );
        return "survey/chart/create";
    }

    @RequestMapping(value = "/department/{dept}/survey/chart/create",
        method = RequestMethod.POST)
    public String create( @ModelAttribute("chart") SurveyChart chart,
        @PathVariable String dept, BindingResult result,
        SessionStatus sessionStatus )
    {
        surveyChartValidator.validate( chart, result );
        if( result.hasErrors() ) return "survey/chart/create";

        User user = SecurityUtils.getUser();
        chart.setAuthor( user );
        chart.setDepartment( departmentDao.getDepartment( dept ) );
        chart = surveyChartDao.saveSurveyChart( chart );

        logger.info( user.getUsername() + " created survey chart "
            + chart.getId() );

        sessionStatus.setComplete();
        return "redirect:list";
    }

    @RequestMapping(value = "/department/{dept}/survey/chart/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "chart", surveyChartDao.getSurveyChart( id ) );
        return "survey/chart/edit";
    }

    @RequestMapping(value = "/department/{dept}/survey/chart/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute("chart") SurveyChart chart,
        @PathVariable String dept, BindingResult result,
        SessionStatus sessionStatus )
    {
        surveyChartValidator.validate( chart, result );
        if( result.hasErrors() ) return "survey/chart/edit";

        User user = SecurityUtils.getUser();
        chart.setAuthor( user );
        chart.setDate( new Date() );
        chart = surveyChartDao.saveSurveyChart( chart );

        logger.info( user.getUsername() + " edited survey chart "
            + chart.getId() );

        sessionStatus.setComplete();
        return "redirect:list";
    }

    @RequestMapping("/department/{dept}/survey/chart/addXCoordinate")
    public @ResponseBody
    String addXCoordinate( @ModelAttribute("chart") SurveyChart chart,
        @RequestParam String coordinate )
    {
        chart.getxCoordinates().add( coordinate );
        logger.info( "X coordinate added: " + coordinate );
        return "";
    }

    @RequestMapping("/department/{dept}/survey/chart/removeXCoordinate")
    public @ResponseBody
    String removeXCoordinate( @ModelAttribute("chart") SurveyChart chart,
        @RequestParam String coordinate )
    {
        chart.getxCoordinates().remove( coordinate );
        logger.info( "X coordinate removed: " + coordinate );
        return "";
    }

}
