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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Program;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.ProgramDao;
import csns.security.SecurityUtils;
import csns.web.validator.ProgramValidator;

@Controller
@SessionAttributes("program")
public class ProgramControllerS {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private ProgramValidator programValidator;

    private static final Logger logger = LoggerFactory.getLogger( ProgramControllerS.class );

    @RequestMapping("/department/{dept}/program/addCourse")
    @ResponseBody
    public ResponseEntity<String> addCourse(
        @ModelAttribute("program") Program program,
        @RequestParam Long courseId, @RequestParam String courseType )
    {
        Course course = courseDao.getCourse( courseId );
        if( program.contains( course ) )
            return new ResponseEntity<String>( HttpStatus.BAD_REQUEST );

        if( courseType.equalsIgnoreCase( "required" ) )
            program.getRequiredCourses().add( course );
        else
            program.getElectiveCourses().add( course );

        logger.info( SecurityUtils.getUser().getUsername() + " added "
            + courseType + " course " + courseId + " to program "
            + program.getId() );

        return new ResponseEntity<String>( HttpStatus.OK );
    }

    @RequestMapping("/department/{dept}/program/removeCourse")
    @ResponseStatus(HttpStatus.OK)
    public void removeCourse( @ModelAttribute("program") Program program,
        @RequestParam Long courseId, @RequestParam String courseType )
    {
        List<Course> courses = courseType.equalsIgnoreCase( "required" )
            ? program.getRequiredCourses() : program.getElectiveCourses();
        for( Course course : courses )
            if( course.getId().equals( courseId ) )
            {
                courses.remove( course );
                break;
            }

        logger.info( SecurityUtils.getUser().getUsername() + " removed "
            + courseType + " course " + courseId + " from program "
            + program.getId() );
    }

    @RequestMapping(value = "/department/{dept}/program/create",
        method = RequestMethod.GET)
    public String create( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "program", new Program( department ) );
        return "program/create";
    }

    @RequestMapping(value = "/department/{dept}/program/create",
        method = RequestMethod.POST)
    public String create( @ModelAttribute("program") Program program,
        BindingResult result, SessionStatus sessionStatus )
    {
        programValidator.validate( program, result );
        if( result.hasErrors() ) return "course/program/create";

        program = programDao.saveProgram( program );
        logger.info( SecurityUtils.getUser().getUsername() + " added program "
            + program.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + program.getId();
    }

    @RequestMapping(value = "/department/{dept}/program/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "program", programDao.getProgram( id ) );
        return "program/edit";
    }

    @RequestMapping(value = "/department/{dept}/program/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute("program") Program program,
        BindingResult result, SessionStatus sessionStatus )
    {
        programValidator.validate( program, result );
        if( result.hasErrors() ) return "course/program/edit";

        program = programDao.saveProgram( program );
        logger.info( SecurityUtils.getUser().getUsername() + " edited program "
            + program.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + program.getId();
    }

}
