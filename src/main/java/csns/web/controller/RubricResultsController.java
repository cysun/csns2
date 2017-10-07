/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014-2017, Chengyu Sun (csun@calstatela.edu).
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
import java.util.SortedMap;
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

import csns.helper.highcharts.Chart;
import csns.helper.highcharts.Series;
import csns.model.academics.Course;
import csns.model.academics.Section;
import csns.model.academics.dao.CourseDao;
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
    private CourseDao courseDao;

    @Autowired
    private SectionDao sectionDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory
        .getLogger( RubricResultsController.class );

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

    @RequestMapping(value = "/department/{dept}/rubric/results",
        params = { "rubricId", "sectionId" })
    public String results( @RequestParam Long rubricId,
        @RequestParam Long sectionId, ModelMap models )
    {
        Rubric rubric = rubricDao.getRubric( rubricId );
        Section section = sectionDao.getSection( sectionId );
        models.put( "rubric", rubric );
        models.put( "section", section );

        // if there are more than one assignment in a section for the same
        // rubric (which should be extremely rare), we take the last assignment
        // because aggregating multiple assignments doesn't make too much sense.
        Map<String, int[][]> ratingCountsByType = null;
        RubricAssignment assignment = section.getRubricAssignment( rubric );
        if( assignment != null )
        {
            assignment.countRatings();
            ratingCountsByType = assignment.getRatingCountsByType();
            models.put( "ratingCountsByType", ratingCountsByType );
        }

        Chart chart = new Chart(
            rubric.getName() + ", " + section.getCourse().getCode() + " "
                + section.getTerm().getShortString() );

        List<String> xLabels = new ArrayList<String>();
        for( RubricIndicator indicator : rubric.getIndicators() )
            xLabels.add( indicator.getName() );
        chart.getxAxis().setCategories( xLabels );

        if( ratingCountsByType != null )
        {
            boolean showInLegend = true;
            for( String key : ratingCountsByType.keySet() )
            {
                int ratingCounts[][] = ratingCountsByType.get( key );
                for( int i = rubric.getScale() - 1; i >= 0; --i )
                {
                    List<Integer> data = new ArrayList<Integer>();
                    for( int j = 0; j < rubric.getIndicators().size(); ++j )
                        data.add( ratingCounts[j][i] );
                    chart.getSeries().add(
                        new Series( i + 1 + "", data, showInLegend, key ) );
                }
                if( showInLegend ) showInLegend = false;
            }
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

    @RequestMapping(value = "/department/{dept}/rubric/results",
        params = { "rubricId", "courseId" })
    public String results( @RequestParam Long rubricId,
        @RequestParam Long courseId,
        @RequestParam(required = false) Integer beginYear,
        @RequestParam(required = false) Integer endYear,
        @RequestParam(required = false,
            name = "evalType") RubricEvaluation.Type type,
        ModelMap models )
    {
        Rubric rubric = rubricDao.getRubric( rubricId );
        Course course = courseDao.getCourse( courseId );
        models.put( "rubric", rubric );
        models.put( "course", course );

        List<Integer> years = rubricEvaluationDao
            .getRubricEvaluationYears( rubric, course );
        if( endYear == null ) endYear = years.get( 0 );
        if( beginYear == null )
        {
            beginYear = years.get( years.size() - 1 );
            beginYear = beginYear < endYear - 5 ? endYear - 5 : beginYear;
        }
        models.put( "years", years );
        models.put( "beginYear", beginYear );
        models.put( "endYear", endYear );

        if( type == null ) type = RubricEvaluation.Type.INSTRUCTOR;
        models.put( "evalType", type.name() );

        SortedMap<Integer, int[][]> ratingCountsByYear = new TreeMap<Integer, int[][]>();
        for( int i = beginYear; i <= endYear; ++i )
            ratingCountsByYear.put( i,
                new int[rubric.getIndicators().size()][rubric.getScale()] );
        List<Section> sections = sectionDao.getSections( course, beginYear,
            endYear );
        for( Section section : sections )
        {
            RubricAssignment assignment = section.getRubricAssignment( rubric );
            if( assignment == null ) continue;

            assignment.countRatings();
            Integer year = section.getTerm().getYear();
            if( assignment.getRatingCountsByType().get( type.name() ) != null )
            {
                for( int i = 0; i < rubric.getIndicators().size(); ++i )
                    for( int j = 0; j < rubric.getScale(); ++j )
                        ratingCountsByYear.get( year )[i][j] += assignment
                            .getRatingCountsByType().get( type.name() )[i][j];
            }
        }
        models.put( "ratingCountsByYear", ratingCountsByYear );

        Chart chart = new Chart(
            rubric.getName() + ", " + course.getCode() + " " + type.name()
                + " Evaluations, " + beginYear + "-" + endYear );

        List<String> xLabels = new ArrayList<String>();
        for( RubricIndicator indicator : rubric.getIndicators() )
            xLabels.add( indicator.getName() );
        chart.getxAxis().setCategories( xLabels );

        boolean showInLegend = true;
        for( Integer key : ratingCountsByYear.keySet() )
        {
            int ratingCounts[][] = ratingCountsByYear.get( key );
            for( int i = rubric.getScale() - 1; i >= 0; --i )
            {
                List<Integer> data = new ArrayList<Integer>();
                for( int j = 0; j < rubric.getIndicators().size(); ++j )
                    data.add( ratingCounts[j][i] );
                chart.getSeries().add( new Series( i + 1 + "", data,
                    showInLegend, key.toString() ) );
            }
            if( showInLegend ) showInLegend = false; // only show legends once
        }

        try
        {
            models.put( "chart", objectMapper.writeValueAsString( chart ) );
        }
        catch( JsonProcessingException e )
        {
            logger.warn( "Cannot serialize chart.", e );
        }

        return "rubric/result/course";
    }

}
