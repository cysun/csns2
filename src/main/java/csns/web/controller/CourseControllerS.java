/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013-2014, Chengyu Sun (csun@calstatela.edu).
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Course;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.User;
import csns.model.forum.Forum;
import csns.model.forum.dao.ForumDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.editor.UserPropertyEditor;
import csns.web.validator.CourseValidator;

@Controller
@SessionAttributes("course")
public class CourseControllerS {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ForumDao forumDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CourseValidator courseValidator;

    @Autowired
    private FileIO fileIO;

    @Autowired
    private WebApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger( CourseControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( User.class,
            (UserPropertyEditor) context.getBean( "userPropertyEditor" ) );
    }

    @RequestMapping(value = "/course/create", method = RequestMethod.GET)
    public String create( ModelMap models )
    {
        models.put( "course", new Course() );
        return "course/create";
    }

    @RequestMapping(value = "/course/create", method = RequestMethod.POST)
    public String create(
        @ModelAttribute Course course,
        @RequestParam(value = "file", required = false) MultipartFile uploadedFile,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        courseValidator.validate( course, bindingResult );
        if( bindingResult.hasErrors() ) return "course/create";

        if( uploadedFile != null && !uploadedFile.isEmpty() )
            course.setDescription( fileIO.save( uploadedFile,
                SecurityUtils.getUser(), true ) );

        course.setDepartment( departmentDao.getDepartment( course.getDept() ) );
        course = courseDao.saveCourse( course );

        Forum forum = new Forum( course.getCode() + " " + course.getName() );
        forum.setCourse( course );
        forumDao.saveForum( forum );

        logger.info( SecurityUtils.getUser().getUsername() + " created course "
            + course.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + course.getId();
    }

    @RequestMapping(value = "/course/edit", method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "course", courseDao.getCourse( id ) );
        return "course/edit";
    }

    @RequestMapping(value = "/course/edit", method = RequestMethod.POST)
    public String edit(
        @ModelAttribute Course course,
        @RequestParam(value = "file", required = false) MultipartFile uploadedFile,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        courseValidator.validate( course, bindingResult );
        if( bindingResult.hasErrors() ) return "course/edit";

        if( uploadedFile != null && !uploadedFile.isEmpty() )
            course.setDescription( fileIO.save( uploadedFile,
                SecurityUtils.getUser(), true ) );

        course.setDepartment( departmentDao.getDepartment( course.getDept() ) );
        course = courseDao.saveCourse( course );

        Forum forum = forumDao.getForum( course );
        forum.setName( course.getCode() + " " + course.getName() );
        forumDao.saveForum( forum );

        logger.info( SecurityUtils.getUser().getUsername() + " edited course "
            + course.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + course.getId();
    }

}
