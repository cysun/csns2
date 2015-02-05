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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.mailinglist.Mailinglist;
import csns.model.mailinglist.Message;
import csns.model.mailinglist.dao.MailinglistDao;
import csns.model.mailinglist.dao.MessageDao;
import csns.security.SecurityUtils;

@Controller
public class MailinglistController {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MailinglistDao mailinglistDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @RequestMapping("/department/{dept}/mailinglist/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        for( Mailinglist mailinglist : department.getMailinglists() )
            mailinglist.setSubscriptionCount( subscriptionDao.getSubscriptionCount( mailinglist ) );

        models.put( "department", department );
        return "mailinglist/list";
    }

    @RequestMapping("/department/{dept}/mailinglist/view")
    public String view( @PathVariable String dept, @RequestParam Long id,
        ModelMap models )
    {
        Mailinglist mailinglist = mailinglistDao.getMailinglist( id );
        List<Message> messages = messageDao.getMessagess( mailinglist, 40 );

        if( SecurityUtils.isAuthenticated() )
        {
            User user = SecurityUtils.getUser();
            models.put( "isFaculty", user.isFaculty( dept ) );
            models.put( "subscription",
                subscriptionDao.getSubscription( mailinglist, user ) );
        }

        models.put( "mailinglist", mailinglist );
        models.put( "messages", messages );
        return "mailinglist/view";
    }

}
