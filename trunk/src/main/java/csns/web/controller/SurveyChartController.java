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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import csns.helper.highcharts.Chart;
import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.survey.SurveyChart;
import csns.model.survey.SurveyChartSeries;
import csns.model.survey.dao.SurveyChartDao;
import csns.model.survey.dao.SurveyChartSeriesDao;
import csns.security.SecurityUtils;

@Controller
public class SurveyChartController {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SurveyChartDao surveyChartDao;

    @Autowired
    private SurveyChartSeriesDao surveyChartSeriesDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger( SurveyChartController.class );

    @RequestMapping("/department/{dept}/survey/chart/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "department", department );
        models.put( "charts", surveyChartDao.getSurveyCharts( department ) );
        return "survey/chart/list";
    }

    @RequestMapping("/department/{dept}/survey/chart/view")
    public String view( @RequestParam Long id, ModelMap models )
        throws JsonProcessingException
    {
        SurveyChart chart = surveyChartDao.getSurveyChart( id );
        if( chart.setValues() )
            chart = surveyChartDao.saveSurveyChart( chart );
        Chart highchart = chart.getHighchart();

        models.put( "chart", chart );
        models.put( "highchart", objectMapper.writeValueAsString( highchart ) );
        return "survey/chart/view";
    }

    @RequestMapping("/department/{dept}/survey/chart/delete")
    public String delete( @RequestParam Long id )
    {
        SurveyChart chart = surveyChartDao.getSurveyChart( id );
        chart.setDeleted( true );
        chart = surveyChartDao.saveSurveyChart( chart );

        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted survey chart " + chart.getId() );
        return "redirect:list";
    }

    @RequestMapping("/department/{dept}/survey/chart/viewSeries")
    public String viewSeries( @RequestParam Long id, ModelMap models )
    {
        models.put( "series", surveyChartSeriesDao.getSurveyChartSeries( id ) );
        return "survey/chart/viewSeries";
    }

    @RequestMapping("/department/{dept}/survey/chart/deleteSeries")
    public String deleteSeries( @RequestParam Long id )
    {
        SurveyChartSeries series = surveyChartSeriesDao.getSurveyChartSeries( id );
        SurveyChart chart = series.getChart();

        series.setChart( null );
        series = surveyChartSeriesDao.saveSurveyChartSeries( series );

        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted survey chart series " + series.getId() );
        return "redirect:view?id=" + chart.getId();
    }

}
