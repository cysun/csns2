/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014-2015,2017, Chengyu Sun (csun@calstatela.edu).
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import csns.helper.RubricEvaluationStats;
import csns.helper.highcharts.Chart;
import csns.helper.highcharts.Series;
import csns.model.academics.Enrollment;
import csns.model.assessment.Rubric;
import csns.model.assessment.RubricAssignment;
import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.RubricIndicator;
import csns.model.assessment.RubricSubmission;
import csns.model.assessment.dao.RubricAssignmentDao;
import csns.model.assessment.dao.RubricSubmissionDao;
import csns.model.core.User;
import csns.security.SecurityUtils;

@Controller
public class RubricSubmissionController {

    @Autowired
    private RubricAssignmentDao rubricAssignmentDao;

    @Autowired
    private RubricSubmissionDao rubricSubmissionDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory
        .getLogger( RubricSubmissionController.class );

    @RequestMapping("/rubric/submission/{role}/list")
    public String list( @PathVariable String role,
        @RequestParam Long assignmentId, ModelMap models )
    {
        RubricAssignment assignment = rubricAssignmentDao
            .getRubricAssignment( assignmentId );

        Set<User> students = new HashSet<User>();
        for( Enrollment enrollment : assignment.getSection().getEnrollments() )
            students.add( enrollment.getStudent() );

        // we need to remove the submissions for the students who already
        // dropped the class
        Iterator<RubricSubmission> i = assignment.getSubmissions().iterator();
        while( i.hasNext() )
        {
            RubricSubmission submission = i.next();
            if( !students.contains( submission.getStudent() ) )
                i.remove();
            else
                students.remove( submission.getStudent() );
        }

        // we then add a submission for each student who is in the class but
        // didn't have a submission for this assignment.
        for( User student : students )
        {
            RubricSubmission submission = new RubricSubmission( student,
                assignment );
            assignment.getSubmissions().add( submission );
        }
        assignment = rubricAssignmentDao.saveRubricAssignment( assignment );

        models.put( "user", SecurityUtils.getUser() );
        models.put( "assignment", assignment );
        // Instructor, evaluator, and student will see different views.
        return "rubric/submission/list/" + role;
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

    private String view( String role, RubricSubmission submission,
        ModelMap models )
    {
        Rubric rubric = submission.getAssignment().getRubric();
        models.put( "user", SecurityUtils.getUser() );
        models.put( "submission", submission );

        Chart chart = new Chart( rubric.getName(), "Indicator", "Mean Rating" );

        List<String> xLabels = new ArrayList<String>();
        for( RubricIndicator indicator : rubric.getIndicators() )
            xLabels.add( indicator.getName() );
        xLabels.add( "Overall" );
        chart.getxAxis().setCategories( xLabels );
        chart.getyAxis().setMax( rubric.getScale() );

        List<RubricEvaluationStats> stats;
        if( submission.getAssignment().isEvaluatedByInstructors() )
        {
            stats = RubricEvaluationStats.calcStats( submission,
                RubricEvaluation.Type.INSTRUCTOR );
            models.put( "iEvalStats", stats );
            addSeries( chart, "Instructor", stats );
        }
        if( submission.getAssignment().isEvaluatedByStudents() )
        {
            stats = RubricEvaluationStats.calcStats( submission,
                RubricEvaluation.Type.PEER );
            models.put( "sEvalStats", stats );
            addSeries( chart, "Peer", stats );
        }
        if( submission.getAssignment().isEvaluatedByExternal() )
        {
            stats = RubricEvaluationStats.calcStats( submission,
                RubricEvaluation.Type.EXTERNAL );
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

        return "rubric/submission/view/" + role;
    }

    @RequestMapping("/rubric/submission/student/view")
    public String view( @RequestParam Long assignmentId, ModelMap models )
    {
        User student = SecurityUtils.getUser();
        RubricAssignment assignment = rubricAssignmentDao
            .getRubricAssignment( assignmentId );
        RubricSubmission submission = rubricSubmissionDao
            .getRubricSubmission( student, assignment );
        if( submission == null )
            submission = new RubricSubmission( student, assignment );
        return view( "student", submission, models );
    }

    @RequestMapping("/rubric/submission/student/rubric")
    public String rubric( @RequestParam Long assignmentId, ModelMap models )
    {
        models.put( "assignment",
            rubricAssignmentDao.getRubricAssignment( assignmentId ) );
        return "rubric/submission/rubric";
    }

    @RequestMapping("/rubric/submission/{role}/view")
    public String view( @PathVariable String role, @RequestParam Long id,
        ModelMap models )
    {
        return view( role, rubricSubmissionDao.getRubricSubmission( id ),
            models );
    }

}
