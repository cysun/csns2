/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2017, Chengyu Sun (csun@calstatela.edu).
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import csns.model.academics.Course;
import csns.model.academics.CourseMapping;
import csns.model.academics.Enrollment;
import csns.model.advisement.PersonalProgram;
import csns.model.advisement.PersonalProgramEntry;

public class ProgramChecker {

    List<Map<Course, PersonalProgramEntry>> entryMaps;

    public ProgramChecker( PersonalProgram personalProgram )
    {
        entryMaps = personalProgram.getEntryMaps();
    }

    private boolean setEnrollment( Course course, Enrollment enrollment )
    {
        for( Map<Course, PersonalProgramEntry> entryMap : entryMaps )
        {
            PersonalProgramEntry entry = entryMap.get( course );
            if( entry != null && entry.getEnrollment() == null )
            {
                entry.setEnrollment( enrollment );
                return true;
            }
        }
        return false;
    }

    private Set<Course> getMappedCourses( List<CourseMapping> courseMappings,
        Course course )
    {
        Set<Course> mappedCourses = new HashSet<Course>();
        for( CourseMapping courseMapping : courseMappings )
        {
            if( courseMapping.getGroup1().size() != 1
                || courseMapping.getGroup2().size() != 1 )
                continue;
            else if( courseMapping.getGroup1()
                .get( 0 )
                .getId()
                .equals( course.getId() ) )
                mappedCourses.add( courseMapping.getGroup2().get( 0 ) );
            else if( courseMapping.getGroup2()
                .get( 0 )
                .getId()
                .equals( course.getId() ) )
                mappedCourses.add( courseMapping.getGroup1().get( 0 ) );
        }
        return mappedCourses;
    }

    public int checkRequirements( Collection<Enrollment> enrollments )
    {
        int count = 0; // number of entries updated

        Iterator<Enrollment> iterator = enrollments.iterator();
        while( iterator.hasNext() )
        {
            Enrollment enrollment = iterator.next();
            if( enrollment.getGrade() == null
                || !enrollment.getGrade().isPassingGrade() ) continue;

            if( setEnrollment( enrollment.getSection().getCourse(),
                enrollment ) )
            {
                iterator.remove();
                ++count;
            }
        }

        return count;
    }

    public int checkRequirements( List<CourseMapping> courseMappings,
        Collection<Enrollment> enrollments )
    {
        int count = 0; // number of entries updated

        Iterator<Enrollment> iterator = enrollments.iterator();
        while( iterator.hasNext() )
        {
            Enrollment enrollment = iterator.next();
            if( enrollment.getGrade() == null
                || !enrollment.getGrade().isPassingGrade() ) continue;

            Set<Course> courses = getMappedCourses( courseMappings,
                enrollment.getSection().getCourse() );
            for( Course course : courses )
                if( setEnrollment( course, enrollment ) )
                {
                    iterator.remove();
                    ++count;
                    break;
                }
        }

        return count;
    }

}
