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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.dao.CourseDao;
import csns.security.SecurityUtils;

@Controller
public class CourseController {

    @Autowired
    CourseDao                   courseDao;

    private static final Logger logger = LoggerFactory
                                           .getLogger( CourseController.class );

    @RequestMapping(value = "/course/search")
    public String search( @RequestParam(required = false) String term,
        ModelMap models )
    {
        List<Course> courses = null;
        if( StringUtils.hasText( term ) )
        {
            courses = courseDao.searchCourses( term, -1 );
            logger.info( SecurityUtils.getUser().getUsername()
                + " searched courses with [" + term + "]" );
        }

        models.addAttribute( "courses", courses );
        return "course/search";
    }

    @RequestMapping(value = "/course/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "course", courseDao.getCourse( id ) );
        logger.info( SecurityUtils.getUser().getUsername() + " viewed course "
            + id );

        return "course/view";
    }

}
