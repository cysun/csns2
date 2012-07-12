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
package csns.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.DepartmentDao;

@Controller
public class DepartmentCourseController {

    @Autowired
    CourseDao courseDao;

    @Autowired
    DepartmentDao departmentDao;

    private final static Logger logger = LoggerFactory.getLogger( DepartmentCourseController.class );

    @RequestMapping(value = "/department/{dept}/courses")
    public String courses( @PathVariable String dept, ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( dept ) );
        return "department/courses";
    }

    @RequestMapping(value = "/department/{dept}/course/{level}/{operation}")
    public String operation( @PathVariable String dept,
        @PathVariable String level, @PathVariable String operation,
        @RequestParam Long courseId )
    {
        Department department = departmentDao.getDepartment( dept );
        Course course = courseDao.getCourse( courseId );
        boolean isDepartmentCourse = course.getCode().startsWith(
            dept.toUpperCase() );

        List<Course> courses;
        switch( level )
        {
            case "undergraduate":
                courses = isDepartmentCourse ? department.getUndergraduateCourses()
                    : department.getAdditionalUndergraduateCourses();
                break;
            case "graduate":
                courses = isDepartmentCourse ? department.getGraduateCourses()
                    : department.getAdditionalGraduateCourses();
                break;
            default:
                logger.warn( "Invalid course level: " + level );
                return "redirect:/department/" + dept + "/courses";
        }

        switch( operation )
        {
            case "add":
                if( !courses.contains( course ) ) courses.add( course );
                break;
            case "remove":
                if( courses.contains( course ) ) courses.remove( course );
                break;
            default:
                logger.warn( "Invalid operation type: " + operation );
                return "redirect:/department/" + dept + "/courses";
        }

        departmentDao.saveDepartment( department );

        return "redirect:/department/" + dept + "/courses#" + level;
    }

}
