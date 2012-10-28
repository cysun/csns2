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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.forum.dao.ForumDao;
import csns.model.forum.dao.TopicDao;
import csns.security.SecurityUtils;

@Controller
public class ForumController {

    @Autowired
    UserDao userDao;

    @Autowired
    ForumDao forumDao;

    @Autowired
    TopicDao topicDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    SubscriptionDao subscriptionDao;

    @RequestMapping(value = "/department/{dept}/forum/list")
    public String list( @PathVariable String dept,
        @RequestParam(required = false) Boolean showAll, HttpSession session,
        ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "department", department );
        models.put( "systemForums", forumDao.getSystemForums() );

        if( showAll != null )
        {
            if( showAll )
                session.setAttribute( "showAll", true );
            else
                session.removeAttribute( "showAll" );
        }

        List<Forum> courseForums = new ArrayList<Forum>();
        if( SecurityUtils.isAnonymous()
            || session.getAttribute( "showAll" ) != null )
        {
            for( Course course : department.getUndergraduateCourses() )
                courseForums.add( course.getForum() );
            for( Course course : department.getGraduateCourses() )
                courseForums.add( course.getForum() );
        }
        else
        {
            courseForums = forumDao.getCourseForums( SecurityUtils.getUser() );
        }
        models.put( "courseForums", courseForums );

        return "forum/list";
    }

    @RequestMapping(value = "/department/{dept}/forum/view")
    public String view( @PathVariable String dept, @RequestParam Long id,
        ModelMap models )
    {
        Forum forum = forumDao.getForum( id );
        if( SecurityUtils.isAuthenticated() )
        {
            User user = userDao.getUser( SecurityUtils.getUser().getId() );
            models.put( "isModerator", forum.isModerator( user ) );
            models.put( "subscription",
                subscriptionDao.getSubscription( forum, user ) );
        }

        models.put( "forum", forum );
        models.put( "topics", topicDao.getTopics( forum ) );
        models.put( "department", departmentDao.getDepartment( dept ) );

        return "forum/view";
    }

}
