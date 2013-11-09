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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.news.dao.NewsDao;

@Controller
public class IndexController {

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private DepartmentDao departmentDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index( ModelMap models )
    {
        models.addAttribute( "departments", departmentDao.getDepartments() );
        return "index";
    }

    @RequestMapping({ "/department/{dept}/", "/department/{dept}" })
    public String index( @PathVariable String dept, ModelMap models,
        HttpServletResponse response )
    {
        Department department = departmentDao.getDepartment( dept );
        if( department == null ) return "redirect:/";

        Cookie cookie = new Cookie( "default-dept", dept );
        cookie.setPath( "/" );
        cookie.setMaxAge( 100000000 );
        response.addCookie( cookie );

        models.addAttribute( "department", department );
        models.addAttribute( "newses", newsDao.getNews( department ) );
        return "department/index";
    }

}
