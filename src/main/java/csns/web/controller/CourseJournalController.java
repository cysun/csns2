/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014,2017, Chengyu Sun (csun@calstatela.edu).
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
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Enrollment;
import csns.model.academics.Submission;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.SubmissionDao;
import csns.model.assessment.CourseJournal;
import csns.model.assessment.Rubric;
import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.RubricIndicator;
import csns.model.assessment.RubricSubmission;
import csns.model.assessment.dao.CourseJournalDao;
import csns.model.assessment.dao.RubricSubmissionDao;
import csns.security.SecurityUtils;

@Controller
public class CourseJournalController {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private RubricSubmissionDao rubricSubmissionDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private CourseJournalDao courseJournalDao;

    private static final Logger logger = LoggerFactory
        .getLogger( CourseJournalController.class );

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/department/{dept}/journal/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "department", department );
        models.put( "submittedJournals",
            courseJournalDao.getSubmittedCourseJournals( department ) );
        return "journal/list";
    }

    @RequestMapping("/department/{dept}/journal/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "journal", courseJournalDao.getCourseJournal( id ) );
        return "journal/view";
    }

    @RequestMapping("/department/{dept}/journal/viewOnlineAssignment")
    public String viewOnlineAssignment( @RequestParam Long assignmentId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "assignment", assignmentDao.getAssignment( assignmentId ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "journal/viewOnlineAssignment";
    }

    @RequestMapping("/department/{dept}/journal/viewStudent")
    public String viewStudent( @RequestParam Long enrollmentId,
        ModelMap models )
    {
        Enrollment enrollment = enrollmentDao.getEnrollment( enrollmentId );
        models.put( "enrollment", enrollment );
        models.put( "submissions", submissionDao.getSubmissions(
            enrollment.getStudent(), enrollment.getSection() ) );
        models.put( "rubricSubmissions",
            rubricSubmissionDao.getRubricSubmissions( enrollment.getStudent(),
                enrollment.getSection() ) );
        return "journal/viewStudent";
    }

    @RequestMapping("/department/{dept}/journal/viewSubmission")
    public String viewSubmission( @RequestParam Long id,
        @RequestParam Long enrollmentId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        Submission submission = submissionDao.getSubmission( id );
        models.put( "submission", submission );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return submission.isOnline() ? "journal/viewOnlineSubmission"
            : "journal/viewSubmission";
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

    @RequestMapping("/department/{dept}/journal/viewRubricSubmission")
    public String viewRubricSubmission( @RequestParam Long id,
        @RequestParam Long enrollmentId, ModelMap models )
    {
        RubricSubmission submission = rubricSubmissionDao
            .getRubricSubmission( id );
        Rubric rubric = submission.getAssignment().getRubric();
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

        return "journal/viewRubricSubmission";
    }

    @RequestMapping("/department/{dept}/journal/approve")
    @PreAuthorize("authenticated and principal.isAdmin(#dept)")
    public String approve( @PathVariable String dept, @RequestParam Long id )
    {
        // Save the journal
        CourseJournal journal = courseJournalDao.getCourseJournal( id );
        journal.setApproveDate( new Date() );
        journal = courseJournalDao.saveCourseJournal( journal );

        // Set the journal to be the official journal of the course
        Course course = journal.getSection().getCourse();
        course.setJournal( journal );
        course = courseDao.saveCourse( course );

        logger.info( SecurityUtils.getUser().getUsername()
            + " approved course journal " + id );

        return "redirect:list";
    }

    @RequestMapping("/department/{dept}/journal/reject")
    @PreAuthorize("authenticated and principal.isAdmin(#dept)")
    public String reject( @PathVariable String dept, @RequestParam Long id )
    {
        CourseJournal journal = courseJournalDao.getCourseJournal( id );
        journal.setSubmitDate( null );
        journal = courseJournalDao.saveCourseJournal( journal );

        logger.info( SecurityUtils.getUser().getUsername()
            + " rejected course journal " + id );

        return "redirect:list#submitted";
    }

}
