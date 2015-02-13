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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import csns.model.academics.Course;
import csns.model.academics.CourseMapping;

public class CourseMapper {

    Map<Course, Set<Course>> mappings;

    public CourseMapper( List<CourseMapping> courseMappings )
    {
        mappings = new HashMap<Course, Set<Course>>();

        for( CourseMapping courseMapping : courseMappings )
        {
            if( courseMapping.getGroup1().size() > 1
                || courseMapping.getGroup2().size() > 1 ) continue;

            for( Course course : courseMapping.getGroup1() )
                addMapping( course, courseMapping.getGroup2() );
            for( Course course : courseMapping.getGroup2() )
                addMapping( course, courseMapping.getGroup1() );
        }
    }

    public void addMapping( Course course, Collection<Course> courses )
    {
        Set<Course> mappedCourses = mappings.get( course );
        if( mappedCourses == null )
        {
            mappedCourses = new HashSet<Course>();
            mappings.put( course, mappedCourses );
        }
        mappedCourses.addAll( courses );
    }

    public boolean hasMapping( Course course )
    {
        return mappings.keySet().contains( course );
    }

    public Set<Course> getMappedCourses( Course course )
    {
        return hasMapping( course ) ? mappings.get( course )
            : new HashSet<Course>();
    }

}
