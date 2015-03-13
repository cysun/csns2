/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2015, Chengyu Sun (csun@calstatela.edu).
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.ProgramDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;

@Controller
public class DepartmentInfoController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private DepartmentDao departmentDao;

    private final static Logger logger = LoggerFactory.getLogger( DepartmentInfoController.class );

    @RequestMapping(value = "/department/{dept}/people")
    public String people( @PathVariable String dept,
        @RequestParam(required = false) String term, ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( dept ) );
        return "department/people";
    }

    @PreAuthorize("hasRole('DEPT_ROLE_ADMIN_' + #dept)")
    @RequestMapping(value = "/department/{dept}/personnel/{personnel}/{operation}")
    public String people( @PathVariable String dept,
        @PathVariable String personnel, @PathVariable String operation,
        @RequestParam Long userId )
    {
        Department department = departmentDao.getDepartment( dept );

        String role;
        List<User> users;
        switch( personnel )
        {
            case "admin":
                role = "DEPT_ROLE_ADMIN_" + dept;
                users = department.getAdministrators();
                break;
            case "faculty":
                role = "DEPT_ROLE_FACULTY_" + dept;
                users = department.getFaculty();
                break;
            case "instructor":
                role = "DEPT_ROLE_INSTRUCTOR_" + dept;
                users = department.getInstructors();
                break;
            case "evaluator":
                role = "DEPT_ROLE_EVALUATOR_" + dept;
                users = department.getEvaluators();
                break;
            case "reviewer":
                role = "DEPT_ROLE_REVIEWER_" + dept;
                users = department.getReviewers();
                break;
            default:
                logger.warn( "Invalid personnel type: " + personnel );
                return "redirect:/department/" + dept + "/people";
        }

        User user = userDao.getUser( userId );
        switch( operation )
        {
            case "add":
                if( !users.contains( user ) )
                {
                    users.add( user );
                    user.getRoles().add( role );
                    logger.info( SecurityUtils.getUser().getUsername()
                        + " added " + role + " to " + user.getUsername() );
                }
                break;
            case "remove":
                if( users.contains( user ) )
                {
                    users.remove( user );
                    user.getRoles().remove( role );
                    logger.info( SecurityUtils.getUser().getUsername()
                        + " removed " + role + " from " + user.getUsername() );
                }
                break;
            default:
                logger.warn( "Invalid operation type: " + operation );
                return "redirect:/department/" + dept + "/people";
        }

        userDao.saveUser( user );
        departmentDao.saveDepartment( department );

        return "redirect:/department/" + dept + "/people#" + personnel;
    }

    @RequestMapping(value = "/department/{dept}/programs")
    public String programs( @PathVariable String dept, ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( dept ) );
        return "department/programs";
    }

    @RequestMapping(value = "/department/{dept}/courses")
    public String courses( @PathVariable String dept, ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( dept ) );
        return "department/courses";
    }

    @PreAuthorize("hasRole('DEPT_ROLE_ADMIN_' + #dept)")
    @RequestMapping(value = "/department/{dept}/course/{level}/{operation}")
    public String courses( @PathVariable String dept,
        @PathVariable String level, @PathVariable String operation,
        @RequestParam Long courseId )
    {
        Department department = departmentDao.getDepartment( dept );
        Course course = courseDao.getCourse( courseId );
        boolean isDepartmentCourse = course.getDepartment() != null
            && course.getDepartment().getId().equals( department.getId() );

        List<Course> courses;
        switch( level )
        {
            case "undergraduate":
                courses = isDepartmentCourse
                    ? department.getUndergraduateCourses()
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

    @RequestMapping("/department/{dept}/option/{option}")
    public String option( @PathVariable String dept,
        @PathVariable String option, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "result", department.getOptions().contains( option ) );
        return "jsonView";
    }

}
