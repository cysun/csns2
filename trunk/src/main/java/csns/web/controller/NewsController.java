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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.news.News;
import csns.model.news.dao.NewsDao;
import csns.security.SecurityUtils;

@Controller
public class NewsController {

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private DepartmentDao departmentDao;

    private static final Logger logger = LoggerFactory.getLogger( NewsController.class );

    @RequestMapping("/department/{dept}/news/current")
    public String current( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "user", SecurityUtils.getUser() );
        models.put( "newses", newsDao.getNews( department ) );
        return "news/current";
    }

    @RequestMapping("/department/{dept}/news/delete")
    public String delete( @PathVariable String dept, @RequestParam Long id )
    {
        News news = newsDao.getNews( id );
        news.setExpireDate( Calendar.getInstance() );
        news = newsDao.saveNews( news );

        logger.info( SecurityUtils.getUser().getUsername() + " deleted news "
            + news.getId() );

        return "redirect:/department/" + dept + "/news/current";
    }

}
