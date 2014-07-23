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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import csns.helper.RubricEvaluationStats;
import csns.helper.highcharts.Chart;
import csns.helper.highcharts.Series;
import csns.model.academics.Course;
import csns.model.academics.Section;
import csns.model.academics.dao.SectionDao;
import csns.model.assessment.Rubric;
import csns.model.assessment.RubricAssignment;
import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.RubricIndicator;
import csns.model.assessment.dao.RubricDao;
import csns.model.assessment.dao.RubricEvaluationDao;

@Controller
public class RubricResultsController {

    @Autowired
    private RubricDao rubricDao;

    @Autowired
    private RubricEvaluationDao rubricEvaluationDao;

    @Autowired
    private SectionDao sectionDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger( RubricResultsController.class );

    @RequestMapping(value = "/department/{dept}/rubric/results", params = "id")
    public String results( @RequestParam Long id, ModelMap models )
    {
        Rubric rubric = rubricDao.getRubric( id );
        List<Section> sections = sectionDao.getSectionsByRubric( rubric );
        Collections.sort( sections );

        Map<Course, List<Section>> mappedSections = new TreeMap<Course, List<Section>>();
        for( Section section : sections )
        {
            List<Section> ss = mappedSections.get( section.getCourse() );
            if( ss == null )
            {
                ss = new ArrayList<Section>();
                mappedSections.put( section.getCourse(), ss );
            }
            ss.add( section );
        }

        models.put( "rubric", rubric );
        models.put( "mappedSections", mappedSections );
        return "rubric/results";
    }

    private void addSeries( Chart chart, String name,
        List<RubricEvaluationStats> stats )
    {
        if( stats.get( 0 ).getCount() == 0 ) return;

        List<Double> data = new ArrayList<Double>();
        for( int i = 1; i < stats.size(); ++i )
            data.add( stats.get( i ).getMean() );
        // The overall stats is the first one in the list
        data.add( stats.get( 0 ).getMean() );

        chart.getSeries().add( new Series( name, data, true ) );
    }

    @RequestMapping(value = "/department/{dept}/rubric/results", params = {
        "rubricId", "sectionId" })
    public String results( @RequestParam Long rubricId,
        @RequestParam Long sectionId, ModelMap models )
    {
        Rubric rubric = rubricDao.getRubric( rubricId );
        Section section = sectionDao.getSection( sectionId );
        models.put( "rubric", rubric );
        models.put( "section", section );

        boolean instructorEvaluated = false;
        boolean studentEvaluated = false;
        boolean externalEvaluated = false;
        for( RubricAssignment assignment : section.getRubricAssignments( rubric ) )
        {
            if( assignment.isEvaluatedByInstructors() )
                instructorEvaluated = true;
            if( assignment.isEvaluatedByStudents() ) studentEvaluated = true;
            if( assignment.isEvaluatedByExternal() ) externalEvaluated = true;
        }

        Chart chart = new Chart( rubric.getName() + ", "
            + section.getCourse().getCode() + " "
            + section.getQuarter().getShortString(), "Indicator", "Mean Rating" );

        List<String> xLabels = new ArrayList<String>();
        for( RubricIndicator indicator : rubric.getIndicators() )
            xLabels.add( indicator.getName() );
        xLabels.add( "Overall" );
        chart.getxAxis().setCategories( xLabels );
        chart.getyAxis().setMax( rubric.getScale() );

        List<RubricEvaluationStats> stats;
        if( instructorEvaluated )
        {
            stats = rubricEvaluationDao.getRubricEvaluationStats( rubric,
                section, RubricEvaluation.Type.INSTRUCTOR );
            models.put( "iEvalStats", stats );
            addSeries( chart, "Instructor", stats );
        }

        if( studentEvaluated )
        {
            stats = rubricEvaluationDao.getRubricEvaluationStats( rubric,
                section, RubricEvaluation.Type.PEER );
            models.put( "sEvalStats", stats );
            addSeries( chart, "Peer", stats );
        }

        if( externalEvaluated )
        {
            stats = rubricEvaluationDao.getRubricEvaluationStats( rubric,
                section, RubricEvaluation.Type.EXTERNAL );
            models.put( "eEvalStats", stats );
            addSeries( chart, "External", stats );
        }

        try
        {
            models.put( "chart", objectMapper.writeValueAsString( chart ) );
        }
        catch( JsonProcessingException e )
        {
            logger.warn( "Cannot serialize chart.", e );
        }

        return "rubric/result/section";
    }

}
