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

import csns.model.academics.Department;
import csns.model.academics.Project;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.ProjectDao;
import csns.model.core.User;
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

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( User.class,
            (UserPropertyEditor) context.getBean( "userPropertyEditor" ) );
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
        return "department/projects";
    }

    @RequestMapping("/department/{dept}/project/view")
    public String view( @PathVariable String dept, @RequestParam Long id,
        ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( dept ) );
        models.put( "project", projectDao.getProject( id ) );
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

        projectDao.saveProject( project );
        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/projects?year="
            + project.getYear();
    }

}
