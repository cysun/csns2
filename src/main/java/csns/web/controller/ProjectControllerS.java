/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;

import csns.model.academics.Project;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.ProjectDao;
import csns.model.core.User;
import csns.security.SecurityUtils;
import csns.web.editor.UserPropertyEditor;
import csns.web.validator.ProjectValidator;

@Controller
@SessionAttributes("project")
public class ProjectControllerS {

    @Autowired
    ProjectDao projectDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    ProjectValidator projectValidator;

    @Autowired
    WebApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger( ProjectControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( User.class,
            (UserPropertyEditor) context.getBean( "userPropertyEditor" ) );
    }

    @RequestMapping(value = "/department/{dept}/project/add",
        method = RequestMethod.GET)
    public String add( @PathVariable String dept,
        @RequestParam(required = false) Integer year, ModelMap models )
    {
        Project project = new Project();
        project.setDepartment( departmentDao.getDepartment( dept ) );
        if( year != null ) project.setYear( year );
        models.put( "project", project );
        return "project/add";
    }

    @RequestMapping(value = "/department/{dept}/project/add",
        method = RequestMethod.POST)
    public String add( @ModelAttribute Project project,
        @PathVariable String dept, BindingResult bindingResult,
        SessionStatus sessionStatus )
    {
        projectValidator.validate( project, bindingResult );
        if( bindingResult.hasErrors() ) return "project/add";

        project = projectDao.saveProject( project );

        logger.info( SecurityUtils.getUser().getUsername() + " added project "
            + project.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/project/view?id="
            + project.getId();
    }

    @RequestMapping(value = "/department/{dept}/project/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "project", projectDao.getProject( id ) );
        models.put( "user", SecurityUtils.getUser() );
        return "project/edit";
    }

    @RequestMapping(value = "/department/{dept}/project/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Project project,
        @PathVariable String dept, BindingResult bindingResult,
        SessionStatus sessionStatus )
    {
        projectValidator.validate( project, bindingResult );
        if( bindingResult.hasErrors() ) return "project/edit";

        project = projectDao.saveProject( project );

        logger.info( SecurityUtils.getUser().getUsername() + " edited project "
            + project.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/project/view?id="
            + project.getId();
    }

}
