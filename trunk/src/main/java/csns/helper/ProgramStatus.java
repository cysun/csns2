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
import java.util.List;

import csns.model.academics.Course;
import csns.model.academics.Enrollment;
import csns.model.academics.Program;

public class ProgramStatus {

    Program program;

    List<CourseStatus> requiredCourseStatuses;

    List<CourseStatus> electiveCourseStatuses;

    List<Enrollment> otherEnrollments;

    public ProgramStatus( Program program )
    {
        this.program = program;

        requiredCourseStatuses = new ArrayList<CourseStatus>();
        for( Course course : program.getRequiredCourses() )
            requiredCourseStatuses.add( new CourseStatus( course ) );

        electiveCourseStatuses = new ArrayList<CourseStatus>();
        for( Course course : program.getElectiveCourses() )
            electiveCourseStatuses.add( new CourseStatus( course ) );

        otherEnrollments = new ArrayList<Enrollment>();
    }

    public CourseStatus getCourseStatus( Course course )
    {
        for( CourseStatus courseStatus : requiredCourseStatuses )
            if( courseStatus.course.getId().equals( course.getId() ) )
                return courseStatus;

        for( CourseStatus courseStatus : electiveCourseStatuses )
            if( courseStatus.course.getId().equals( course.getId() ) )
                return courseStatus;

        return null;
    }

    public boolean addEnrollment( Enrollment enrollment )
    {
        Course course = enrollment.getSection().getCourse();
        CourseStatus courseStatus = getCourseStatus( course );
        if( courseStatus != null )
        {
            courseStatus.addEnrollment( enrollment );
            return true;
        }

        return false;
    }

    public boolean addMappedEnrollment( Course course, Enrollment enrollment )
    {
        CourseStatus courseStatus = getCourseStatus( course );
        if( courseStatus != null )
        {
            courseStatus.addMappedEnrollment( enrollment );
            return true;
        }

        return false;
    }

    public void addOtherEnrollment( Enrollment enrollment )
    {
        otherEnrollments.add( enrollment );
    }

    public Program getProgram()
    {
        return program;
    }

    public List<CourseStatus> getRequiredCourseStatuses()
    {
        return requiredCourseStatuses;
    }

    public List<CourseStatus> getElectiveCourseStatuses()
    {
        return electiveCourseStatuses;
    }

    public List<Enrollment> getOtherEnrollments()
    {
        return otherEnrollments;
    }

    public class CourseStatus {

        String status;

        Course course;

        Enrollment enrollment;

        Enrollment mappedEnrollment;

        public CourseStatus( Course course )
        {
            this.course = course;
        }

        // Decide if e1 is better than e2
        private boolean isBetterThan( Enrollment e1, Enrollment e2 )
        {
            return e2 == null
                || e1.getGrade() != null
                && (e2.getGrade() == null || e2.getSection()
                    .getQuarter()
                    .before( e1.getSection().getQuarter() ));
        }

        public void addEnrollment( Enrollment enrollment )
        {
            if( isBetterThan( enrollment, this.enrollment ) )
            {
                status = "Taken";
                this.enrollment = enrollment;
            }
        }

        public void addMappedEnrollment( Enrollment enrollment )
        {
            if( isBetterThan( enrollment, this.enrollment )
                && isBetterThan( enrollment, this.mappedEnrollment ) )
            {
                status = "Mapped";
                this.mappedEnrollment = enrollment;
            }
        }

        public String getStatus()
        {
            return status;
        }

        public Course getCourse()
        {
            return course;
        }

        public Enrollment getEnrollment()
        {
            return enrollment;
        }

        public Enrollment getMappedEnrollment()
        {
            return mappedEnrollment;
        }

    }

}
