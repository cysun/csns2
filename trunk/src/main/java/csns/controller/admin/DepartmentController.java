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
package csns.controller.admin;

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

import csns.editor.UserPropertyEditor;
import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.validator.DepartmentValidator;

@Controller
@SessionAttributes("department")
public class DepartmentController {

    @Autowired
    UserDao userDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    DepartmentValidator departmentValidator;

    @Autowired
    WebApplicationContext context;

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( User.class,
            (UserPropertyEditor) context.getBean( "userPropertyEditor" ) );
    }

    @RequestMapping(value = "/admin/department/list")
    public String list( ModelMap models )
    {
        models.put( "departments", departmentDao.getDepartments() );
        return "admin/department/list";
    }

    @RequestMapping(value = "/admin/department/add", method = RequestMethod.GET)
    public String add( ModelMap models )
    {
        models.put( "department", new Department() );
        return "admin/department/add";
    }

    @RequestMapping(value = "/admin/department/add",
        method = RequestMethod.POST)
    public String add( @ModelAttribute Department department,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        departmentValidator.validate( department, bindingResult );
        if( bindingResult.hasErrors() ) return "admin/department/add";

        departmentDao.saveDepartment( department );
        sessionStatus.setComplete();

        String adminRole = "DEPT_ROLE_ADMIN_" + department.getAbbreviation();
        for( User user : department.getAdministrators() )
        {
            user.getRoles().add( adminRole );
            userDao.saveUser( user );
        }

        return "redirect:/admin/department/list";
    }

    @RequestMapping(value = "/admin/department/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( id ) );
        return "admin/department/edit";
    }

    @RequestMapping(value = "/admin/department/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Department department,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        departmentValidator.validate( department, bindingResult );
        if( bindingResult.hasErrors() ) return "admin/department/edit";

        departmentDao.saveDepartment( department );
        sessionStatus.setComplete();
        return "redirect:/admin/department/list";
    }

}
