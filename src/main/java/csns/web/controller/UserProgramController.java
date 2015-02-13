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
package csns.web.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.helper.CourseMapper;
import csns.helper.ProgramStatus;
import csns.model.academics.Course;
import csns.model.academics.Enrollment;
import csns.model.academics.Program;
import csns.model.academics.dao.CourseMappingDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.ProgramDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;

@Controller
public class UserProgramController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CourseMappingDao courseMappingDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    private static final Logger logger = LoggerFactory.getLogger( UserProgramController.class );

    @RequestMapping("/user/program")
    public String program( @RequestParam Long userId, ModelMap models )
    {
        User user = userDao.getUser( userId );
        models.put( "user", user );
        models.put( "departments", departmentDao.getDepartments() );

        if( user.getMajor() != null )
            models.put( "programs", programDao.getPrograms( user.getMajor() ) );

        Program program = user.getProgram();
        if( program != null )
        {
            ProgramStatus programStatus = new ProgramStatus( user.getProgram() );
            CourseMapper courseMapper = new CourseMapper(
                courseMappingDao.getCourseMappings( program.getDepartment() ) );
            List<Enrollment> enrollments = enrollmentDao.getEnrollments( user );
            for( Enrollment enrollment : enrollments )
            {
                boolean added = programStatus.addEnrollment( enrollment );
                if( !added )
                {
                    Set<Course> mappedCourses = courseMapper.getMappedCourses( enrollment.getSection()
                        .getCourse() );
                    if( mappedCourses.size() == 1 )
                        added = programStatus.addMappedEnrollment(
                            mappedCourses.iterator().next(), enrollment );
                    if( !added )
                        programStatus.addOtherEnrollment( enrollment );
                }
            }
            models.put( "programStatus", programStatus );
        }

        return "user/program";
    }

    @RequestMapping("/user/setMajor")
    public String setMajor( @RequestParam Long userId,
        @RequestParam(required = false) Long majorId )
    {
        User user = userDao.getUser( userId );
        user.setMajor( majorId == null ? null
            : departmentDao.getDepartment( majorId ) );
        user.setProgram( null );
        user = userDao.saveUser( user );

        if( user.getMajor() == null )
            logger.info( SecurityUtils.getUser().getUsername()
                + " removed major for " + user.getUsername() );
        else
            logger.info( SecurityUtils.getUser().getUsername()
                + " set major to " + user.getMajor().getAbbreviation()
                + " for " + user.getUsername() );

        // Program is the 4th tab
        return "redirect:view?id=" + userId + "#3";
    }

    @RequestMapping("/user/setProgram")
    public String setProgram( @RequestParam Long userId,
        @RequestParam(required = false) Long programId )
    {
        User user = userDao.getUser( userId );
        user.setProgram( programId == null ? null
            : programDao.getProgram( programId ) );
        user = userDao.saveUser( user );

        if( user.getProgram() == null )
            logger.info( SecurityUtils.getUser().getUsername()
                + " removed program for " + user.getUsername() );
        else
            logger.info( SecurityUtils.getUser().getUsername()
                + " set program to " + user.getProgram().getId() + " for "
                + user.getUsername() );

        // Program is the 4th tab
        return "redirect:view?id=" + userId + "#3";
    }

}
