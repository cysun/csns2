/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015, Chengyu Sun (csun@calstatela.edu).
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
package csns.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import csns.model.academics.Course;
import csns.model.academics.Enrollment;
import csns.model.academics.Program;
import csns.model.advisement.CourseSubstitution;
import csns.model.advisement.CourseTransfer;
import csns.model.advisement.CourseWaiver;

public class ProgramStatus {

    Program program;

    List<CourseStatus> requiredCourseStatuses;

    List<CourseStatus> electiveCourseStatuses;

    List<Enrollment> otherEnrollments;

    Set<Course> requiredCourses;

    Set<Course> electiveCourses;

    public ProgramStatus( Program program )
    {
        this.program = program;

        requiredCourseStatuses = new ArrayList<CourseStatus>();
        for( Course course : program.getRequiredCourses() )
            requiredCourseStatuses.add( new CourseStatus( course ) );

        electiveCourseStatuses = new ArrayList<CourseStatus>();
        for( Course course : program.getElectiveCourses() )
            electiveCourseStatuses.add( new CourseStatus( course ) );

        requiredCourses = new HashSet<Course>();
        requiredCourses.addAll( program.getRequiredCourses() );
        electiveCourses = new HashSet<Course>();
        electiveCourses.addAll( program.getElectiveCourses() );
    }

    public void addEnrollment( Enrollment enrollment )
    {

    }

    public class CourseStatus {

        /**
         * Taken: the course was taken. Mapped: an equivalent course was taken.
         * Substituted: the course was substituted by another course.
         * Transferred: the course was transferred. Waived: the course was
         * waived.
         * 
         */
        String status;

        Course course;

        Enrollment enrollment;

        List<Enrollment> mappedEnrollments;

        CourseSubstitution courseSubstituion;

        CourseTransfer courseTransfer;

        CourseWaiver courseWaiver;

        public CourseStatus( Course course )
        {
            this.course = course;
            this.mappedEnrollments = new ArrayList<Enrollment>();
        }
    }

}
