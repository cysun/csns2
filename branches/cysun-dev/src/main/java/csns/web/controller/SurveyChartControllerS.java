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
import csns.model.survey.SurveyChartPoint;
import csns.model.survey.SurveyChartSeries;
import csns.model.survey.dao.SurveyChartDao;
import csns.model.survey.dao.SurveyChartSeriesDao;
import csns.model.survey.dao.SurveyDao;
import csns.security.SecurityUtils;
import csns.web.validator.SurveyChartSeriesValidator;
import csns.web.validator.SurveyChartValidator;

@Controller
@SessionAttributes({ "chart", "series" })
public class SurveyChartControllerS {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SurveyDao surveyDao;

    @Autowired
    private SurveyChartDao surveyChartDao;

    @Autowired
    private SurveyChartSeriesDao surveyChartSeriesDao;

    @Autowired
    private SurveyChartValidator surveyChartValidator;

    @Autowired
    private SurveyChartSeriesValidator surveyChartSeriesValidator;

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
        return "redirect:view?id=" + chart.getId();
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
        chart.setDate( new Date() );
        chart = surveyChartDao.saveSurveyChart( chart );

        logger.info( user.getUsername() + " edited survey chart "
            + chart.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + chart.getId();
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

    @RequestMapping(value = "/department/{dept}/survey/chart/addSeries",
        method = RequestMethod.GET)
    public String addSeries( @RequestParam Long chartId, ModelMap models )
    {
        SurveyChartSeries series = new SurveyChartSeries();
        series.setChart( surveyChartDao.getSurveyChart( chartId ) );
        models.put( "series", series );
        return "survey/chart/addSeries";
    }

    @RequestMapping(value = "/department/{dept}/survey/chart/addSeries",
        method = RequestMethod.POST)
    public String addSeries(
        @ModelAttribute("series") SurveyChartSeries series,
        BindingResult result, SessionStatus sessionStatus )
    {
        surveyChartSeriesValidator.validate( series, result );
        if( result.hasErrors() ) return "survey/chart/addSeries";

        series.getChart().setDate( new Date() );
        series = surveyChartSeriesDao.saveSurveyChartSeries( series );

        logger.info( SecurityUtils.getUser().getUsername()
            + " added survey chart series " + series.getId() );

        sessionStatus.setComplete();
        return "redirect:viewSeries?id=" + series.getId();
    }

    @RequestMapping(value = "/department/{dept}/survey/chart/editSeries",
        method = RequestMethod.GET)
    public String editSeries( @RequestParam Long id, ModelMap models )
    {
        models.put( "series", surveyChartSeriesDao.getSurveyChartSeries( id ) );
        return "survey/chart/editSeries";
    }

    @RequestMapping(value = "/department/{dept}/survey/chart/editSeries",
        method = RequestMethod.POST)
    public String editSeries(
        @ModelAttribute("series") SurveyChartSeries series,
        BindingResult result, SessionStatus sessionStatus )
    {
        surveyChartSeriesValidator.validate( series, result );
        if( result.hasErrors() ) return "survey/chart/editSeries";

        series.getChart().setDate( new Date() );
        series = surveyChartSeriesDao.saveSurveyChartSeries( series );

        logger.info( SecurityUtils.getUser().getUsername()
            + " edited survey chart series " + series.getId() );

        sessionStatus.setComplete();
        return "redirect:viewSeries?id=" + series.getId();
    }

    @RequestMapping("/department/{dept}/survey/chart/addPoint")
    public @ResponseBody
    String addPoint( @ModelAttribute("series") SurveyChartSeries series,
        @RequestParam Long surveyId, @RequestParam int sectionIndex,
        @RequestParam int questionIndex )
    {
        SurveyChartPoint point = new SurveyChartPoint();
        if( surveyId > 0 ) // not an empty point
            point = new SurveyChartPoint( surveyDao.getSurvey( surveyId ),
                sectionIndex, questionIndex );
        series.getPoints().add( point );

        logger.info( "Chart point added: (" + surveyId + "," + sectionIndex
            + "," + questionIndex + ")" );
        return "";
    }

    @RequestMapping("/department/{dept}/survey/chart/removePoint")
    public @ResponseBody
    String removePoint( @ModelAttribute("series") SurveyChartSeries series,
        @RequestParam Long surveyId, @RequestParam int sectionIndex,
        @RequestParam int questionIndex )
    {
        SurveyChartPoint point = series.getPoint( surveyId, sectionIndex,
            questionIndex );
        if( point != null )
        {
            series.getPoints().remove( point );
            logger.info( "Chart point removed: (" + surveyId + ","
                + sectionIndex + "," + questionIndex + ")" );
        }
        else
            logger.info( "Chart point not found: (" + surveyId + ","
                + sectionIndex + "," + questionIndex + ")" );

        return "";
    }

}
