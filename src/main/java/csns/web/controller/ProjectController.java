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

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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

import csns.model.academics.Department;
import csns.model.academics.Project;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.ProjectDao;
import csns.model.core.User;
import csns.security.SecurityUtils;
import csns.web.editor.UserPropertyEditor;
import csns.web.validator.ProjectValidator;

@Controller
@SessionAttributes("project")
public class ProjectController {

    @Autowired
    ProjectDao projectDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    ProjectValidator projectValidator;

    @Autowired
    WebApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger( ProjectController.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( User.class,
            (UserPropertyEditor) context.getBean( "userPropertyEditor" ) );
    }

    @RequestMapping(value = "/project/search")
    public String search( @RequestParam(required = false) String term,
        ModelMap models )
    {
        List<Project> projects = null;
        if( StringUtils.hasText( term ) )
            projects = projectDao.searchProjects( term, 30 );
        models.addAttribute( "projects", projects );
        return "project/search";
    }

    @RequestMapping("/department/{dept}/projects")
    public String projects( @PathVariable String dept,
        @RequestParam(required = false) Integer year, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );

        Integer currentYear = Calendar.getInstance().get( Calendar.YEAR );
        if( year == null ) year = currentYear;
        List<Integer> years = projectDao.getProjectYears( department );
        if( !years.contains( currentYear ) ) years.add( 0, currentYear );
        if( !years.contains( currentYear + 1 ) )
            years.add( 0, currentYear + 1 );

        models.put( "department", department );
        models.put( "year", year );
        models.put( "years", years );
        models.put( "projects", projectDao.getProjects( department, year ) );
        models.put( "user", SecurityUtils.getUser() );
        return "department/projects";
    }

    @RequestMapping("/department/{dept}/project/view")
    public String view( @PathVariable String dept, @RequestParam Long id,
        ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( dept ) );
        models.put( "project", projectDao.getProject( id ) );
        models.put( "user", SecurityUtils.getUser() );
        return "project/view";
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
        sessionStatus.setComplete();
        logger.info( SecurityUtils.getUser().getUsername() + " added project "
            + project.getId() );

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
        sessionStatus.setComplete();
        logger.info( SecurityUtils.getUser().getUsername() + " edited project "
            + project.getId() );

        return "redirect:/department/" + dept + "/project/view?id="
            + project.getId();
    }

    @RequestMapping("/department/{dept}/project/publish")
    public String publish( @PathVariable String dept, @RequestParam Long id )
    {
        Project project = projectDao.getProject( id );
        project.setPublished( true );
        project = projectDao.saveProject( project );
        logger.info( SecurityUtils.getUser().getUsername()
            + " published project " + project.getId() );

        return "redirect:/department/" + dept + "/project/view?id=" + id;
    }

    @RequestMapping("/department/{dept}/project/delete")
    public String delete( @PathVariable String dept, @RequestParam Long id )
    {
        Project project = projectDao.getProject( id );
        project.setDeleted( true );
        project = projectDao.saveProject( project );
        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted project " + project.getId() );

        return "redirect:/department/" + dept + "/projects";
    }

}
