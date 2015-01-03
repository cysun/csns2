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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Enrollment;
import csns.model.assessment.RubricAssignment;
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

    @RequestMapping("/rubric/submission/{role}/list")
    public String list( @PathVariable String role,
        @RequestParam Long assignmentId, ModelMap models )
    {
        RubricAssignment assignment = rubricAssignmentDao.getRubricAssignment( assignmentId );

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

    @RequestMapping("/rubric/submission/{role}/view")
    public String view( @PathVariable String role, @RequestParam Long id,
        ModelMap models )
    {
        // Students don't get a ViewRubricSubmission page. If it's a
        // student, they are redirected to the ViewRubricEvaluation page.
        if( role.equalsIgnoreCase( "student" ) )
            return "redirect:/rubric/evaluation/student/view?submissionId="
                + id;

        models.put( "user", SecurityUtils.getUser() );
        models.put( "submission", rubricSubmissionDao.getRubricSubmission( id ) );
        return "rubric/submission/view/" + role;
    }

}
