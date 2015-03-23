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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import csns.model.academics.Course;
import csns.model.academics.CourseMapping;
import csns.model.academics.Enrollment;
import csns.model.academics.Program;
import csns.model.academics.Section;
import csns.model.advisement.CourseSubstitution;
import csns.model.advisement.CourseTransfer;
import csns.model.advisement.CourseWaiver;

public class ProgramStatus {

    Program program;

    Map<Course, Set<Course>> mappings;

    List<CourseStatus> requiredCourseStatuses;

    List<CourseStatus> electiveCourseStatuses;

    List<Enrollment> otherEnrollments;

    Map<Course, CourseStatus> courseStatusIndex;

    public ProgramStatus( Program program )
    {
        this.program = program;

        mappings = new HashMap<Course, Set<Course>>();
        courseStatusIndex = new LinkedHashMap<Course, CourseStatus>();

        requiredCourseStatuses = new ArrayList<CourseStatus>();
        for( Course course : program.getRequiredCourses() )
        {
            CourseStatus courseStatus = new CourseStatus( course );
            requiredCourseStatuses.add( courseStatus );
            courseStatusIndex.put( course, courseStatus );
        }

        electiveCourseStatuses = new ArrayList<CourseStatus>();
        for( Course course : program.getElectiveCourses() )
        {
            CourseStatus courseStatus = new CourseStatus( course );
            electiveCourseStatuses.add( courseStatus );
            courseStatusIndex.put( course, courseStatus );
        }

        otherEnrollments = new ArrayList<Enrollment>();
    }

    public void addCourseMapping( Course course, Collection<Course> courses )
    {
        Set<Course> mappedCourses = mappings.get( course );
        if( mappedCourses == null )
        {
            mappedCourses = new HashSet<Course>();
            mappings.put( course, mappedCourses );
        }
        mappedCourses.addAll( courses );
    }

    public void addCourseMapping( CourseMapping courseMapping )
    {
        for( Course course : courseMapping.getGroup1() )
            addCourseMapping( course, courseMapping.getGroup2() );
        for( Course course : courseMapping.getGroup2() )
            addCourseMapping( course, courseMapping.getGroup1() );

        if( courseMapping.getGroup1().size() > 1 )
            mergeCourseStatuses( courseMapping.getGroup1() );
        if( courseMapping.getGroup2().size() > 1 )
            mergeCourseStatuses( courseMapping.getGroup2() );
    }

    public void addCourseMappings( Collection<CourseMapping> courseMappings )
    {
        for( CourseMapping courseMapping : courseMappings )
            addCourseMapping( courseMapping );
    }

    public CourseStatus getCourseStatus( Course course )
    {
        return courseStatusIndex.get( course );
    }

    public void mergeCourseStatuses( List<Course> courses )
    {
        if( courses.size() < 2 ) return;

        CourseStatus courseStatus = getCourseStatus( courses.get( 0 ) );
        if( courseStatus != null )
        {
            for( int i = 1; i < courses.size(); ++i )
            {
                CourseStatus other = getCourseStatus( courses.get( i ) );
                if( other != null )
                {
                    courseStatus.merge( other );
                    courseStatusIndex.put( courses.get( i ), courseStatus );
                    requiredCourseStatuses.remove( other );
                    electiveCourseStatuses.remove( other );
                }
            }
        }
    }

    public void addEnrollment( Enrollment enrollment )
    {
        Course course = enrollment.getSection().getCourse();
        CourseStatus courseStatus = getCourseStatus( course );

        if( courseStatus != null )
        {
            courseStatus.addEnrollment( enrollment );
            return;
        }

        boolean added = false;
        if( mappings.get( course ) != null )
            for( Course mappedCourse : mappings.get( course ) )
            {
                courseStatus = getCourseStatus( mappedCourse );
                if( courseStatus != null )
                {
                    added = true;
                    courseStatus.addEnrollment( enrollment );
                }
            }

        if( !added ) otherEnrollments.add( enrollment );
    }

    public void addEnrollments( Collection<Enrollment> enrollments )
    {
        for( Enrollment enrollment : enrollments )
            addEnrollment( enrollment );
    }

    public void addCourseSubstitution( CourseSubstitution courseSubstitution )
    {
        Course course = courseSubstitution.getOriginal();
        CourseStatus courseStatus = getCourseStatus( course );

        if( courseStatus != null )
        {
            courseStatus.addCourseSubstitution( courseSubstitution );
            return;
        }

        if( mappings.get( course ) != null )
            for( Course mappedCourse : mappings.get( course ) )
            {
                courseStatus = getCourseStatus( mappedCourse );
                if( courseStatus != null )
                    courseStatus.addCourseSubstitution( courseSubstitution );
            }
    }

    public void addCourseSubstitutions(
        Collection<CourseSubstitution> courseSubstitutions )
    {
        for( CourseSubstitution courseSubstitution : courseSubstitutions )
            addCourseSubstitution( courseSubstitution );
    }

    public void addCourseTransfer( CourseTransfer courseTransfer )
    {
        Course course = courseTransfer.getCourse();
        CourseStatus courseStatus = getCourseStatus( course );

        if( courseStatus != null )
        {
            courseStatus.addCourseTransfer( courseTransfer );
            return;
        }

        if( mappings.get( course ) != null )
            for( Course mappedCourse : mappings.get( course ) )
            {
                courseStatus = getCourseStatus( mappedCourse );
                if( courseStatus != null )
                    courseStatus.addCourseTransfer( courseTransfer );
            }
    }

    public void addCourseTransfers( Collection<CourseTransfer> courseTransfers )
    {
        for( CourseTransfer courseTransfer : courseTransfers )
            addCourseTransfer( courseTransfer );
    }

    public void addCourseWaiver( CourseWaiver courseWaiver )
    {
        Course course = courseWaiver.getCourse();
        CourseStatus courseStatus = getCourseStatus( course );

        if( courseStatus != null )
        {
            courseStatus.addCourseWaiver( courseWaiver );
            return;
        }

        if( mappings.get( course ) != null )
            for( Course mappedCourse : mappings.get( course ) )
            {
                courseStatus = getCourseStatus( mappedCourse );
                if( courseStatus != null )
                    courseStatus.addCourseWaiver( courseWaiver );
            }
    }

    public void addCourseWaivers( Collection<CourseWaiver> courseWaivers )
    {
        for( CourseWaiver courseWaiver : courseWaivers )
            addCourseWaiver( courseWaiver );
    }

    public void sort()
    {
        EnrollmentComparator enrollmentComparator = new EnrollmentComparator();

        for( CourseStatus courseStatus : requiredCourseStatuses )
            Collections.sort( courseStatus.enrollments, enrollmentComparator );
        for( CourseStatus courseStatus : electiveCourseStatuses )
            Collections.sort( courseStatus.enrollments, enrollmentComparator );

        Collections.sort( otherEnrollments, enrollmentComparator );
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

        List<Course> courses;

        List<Enrollment> enrollments;

        List<CourseSubstitution> courseSubstitutions;

        List<CourseTransfer> courseTransfers;

        List<CourseWaiver> courseWaivers;

        public CourseStatus( Course course )
        {
            courses = new ArrayList<Course>();
            courses.add( course );
            enrollments = new ArrayList<Enrollment>();
            courseSubstitutions = new ArrayList<CourseSubstitution>();
            courseTransfers = new ArrayList<CourseTransfer>();
            courseWaivers = new ArrayList<CourseWaiver>();
        }

        public void merge( CourseStatus courseStatus )
        {
            for( Course course : courseStatus.courses )
                addCourse( course );
            for( Enrollment enrollment : courseStatus.enrollments )
                addEnrollment( enrollment );
        }

        public void addCourse( Course course )
        {
            if( !courses.contains( course ) ) courses.add( course );
        }

        public void addEnrollment( Enrollment enrollment )
        {
            if( !enrollments.contains( enrollment ) )
                enrollments.add( enrollment );
        }

        public void addCourseSubstitution( CourseSubstitution courseSubstitution )
        {
            if( !courseSubstitutions.contains( courseSubstitution ) )
                courseSubstitutions.add( courseSubstitution );
        }

        public void addCourseTransfer( CourseTransfer courseTransfer )
        {
            if( !courseTransfers.contains( courseTransfer ) )
                courseTransfers.add( courseTransfer );
        }

        public void addCourseWaiver( CourseWaiver courseWaiver )
        {
            if( !courseWaivers.contains( courseWaiver ) )
                courseWaivers.add( courseWaiver );
        }

        public List<Course> getCourses()
        {
            return courses;
        }

        public List<Enrollment> getEnrollments()
        {
            return enrollments;
        }

        public List<CourseSubstitution> getCourseSubstitutions()
        {
            return courseSubstitutions;
        }

        public List<CourseTransfer> getCourseTransfers()
        {
            return courseTransfers;
        }

        public List<CourseWaiver> getCourseWaivers()
        {
            return courseWaivers;
        }

    }

    public class EnrollmentComparator implements Comparator<Enrollment> {

        @Override
        public int compare( Enrollment e1, Enrollment e2 )
        {
            Section s1 = e1.getSection();
            Section s2 = e2.getSection();

            int cmp = s1.getCourse().compareTo( s2.getCourse() );
            if( cmp == 0 ) cmp = s1.getQuarter().compareTo( s2.getQuarter() );

            return cmp;
        }

    }
}
