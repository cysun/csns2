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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.mailinglist.Mailinglist;

@Controller
public class UserSubscriptionController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @RequestMapping("/user/subscriptions")
    public String subscriptions( @RequestParam Long userId, ModelMap models )
    {
        User user = userDao.getUser( userId );

        List<Subscription> mailinglists = subscriptionDao.getSubscriptions(
            user, Mailinglist.class );
        List<Subscription> forums = subscriptionDao.getSubscriptions( user,
            Forum.class );
        List<Subscription> departmentForums = new ArrayList<Subscription>();
        List<Subscription> courseForums = new ArrayList<Subscription>();
        List<Subscription> otherForums = new ArrayList<Subscription>();

        for( Subscription subscription : forums )
        {
            Forum forum = (Forum) subscription.getSubscribable();
            if( forum.getDepartment() != null )
                departmentForums.add( subscription );
            else if( forum.getCourse() != null )
                courseForums.add( subscription );
            else
                otherForums.add( subscription );
        }

        models.put( "mailinglists", mailinglists );
        models.put( "departmentForums", departmentForums );
        models.put( "courseForums", courseForums );
        models.put( "otherForums", otherForums );

        return "user/subscriptions";
    }

}
