/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012, Chengyu Sun (csun@calstatela.edu).
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
package csns.importer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.core.User;

@Component
@Scope("prototype")
public class GradesImporter extends RosterImporter {

    Department department;

    List<Integer> sectionNumbers;

    public GradesImporter()
    {
        sectionNumbers = new ArrayList<Integer>();
    }

    public void clear()
    {
        super.clear();
        sectionNumbers.clear();
    }

    public List<User> getInstructors()
    {
        List<User> instructors = new ArrayList<User>();
        if( department != null )
        {
            instructors.addAll( department.getFaculty() );
            instructors.addAll( department.getInstructors() );
        }
        return instructors;
    }

    public List<Course> getCourses()
    {
        List<Course> courses = new ArrayList<Course>();
        if( department != null )
        {
            courses.addAll( department.getUndergraduateCourses() );
            courses.addAll( department.getGraduateCourses() );
        }
        return courses;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public List<Integer> getSectionNumbers()
    {
        return sectionNumbers;
    }

    public void setSectionNumbers( List<Integer> sectionNumbers )
    {
        this.sectionNumbers = sectionNumbers;
    }

}
