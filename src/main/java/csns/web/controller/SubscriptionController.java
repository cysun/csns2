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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import csns.model.core.Subscribable;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.forum.dao.ForumDao;
import csns.model.forum.dao.TopicDao;
import csns.model.mailinglist.dao.MailinglistDao;
import csns.model.wiki.dao.PageDao;
import csns.security.SecurityUtils;

@Controller
public class SubscriptionController {

    @Autowired
    PageDao pageDao;

    @Autowired
    ForumDao forumDao;

    @Autowired
    TopicDao topicDao;

    @Autowired
    MailinglistDao mailinglistDao;

    @Autowired
    SubscriptionDao subscriptionDao;

    private static final Logger logger = LoggerFactory.getLogger( SubscriptionController.class );

    private Subscribable subscribe( String type, Long id )
    {
        Subscribable subscribable = null;
        switch( type )
        {
            case "page":
                subscribable = pageDao.getPage( id );
                break;
            case "forum":
                subscribable = forumDao.getForum( id );
                break;
            case "topic":
                subscribable = topicDao.getTopic( id );
                break;
            case "mailinglist":
                subscribable = mailinglistDao.getMailinglist( id );
                break;
            default:
                logger.error( "Unspported subscribable type: " + type );
                return null;
        }

        User user = SecurityUtils.getUser();
        subscriptionDao.subscribe( subscribable, user );

        logger.info( user.getUsername() + " subscribed to "
            + subscribable.getType() + " " + subscribable.getName() );

        return subscribable;
    }

    private Subscribable unsubscribe( String type, Long id )
    {
        Subscribable subscribable = null;
        switch( type )
        {
            case "page":
                subscribable = pageDao.getPage( id );
                break;
            case "forum":
                subscribable = forumDao.getForum( id );
                break;
            case "topic":
                subscribable = topicDao.getTopic( id );
                break;
            case "mailinglist":
                subscribable = mailinglistDao.getMailinglist( id );
                break;
            default:
                logger.error( "Unspported subscribable type: " + type );
                return null;
        }

        User user = SecurityUtils.getUser();
        subscriptionDao.unsubscribe( subscribable, user );

        logger.info( user.getUsername() + " unsubscribed from "
            + subscribable.getType() + " " + subscribable.getName() );

        return subscribable;
    }

    @RequestMapping(value = "/subscription/{type}/subscribe", params = "ajax")
    public @ResponseBody
    String ajaxSubscribe( @PathVariable String type, @RequestParam Long id )
    {
        subscribe( type, id );
        return "";
    }

    @RequestMapping(value = "/subscription/{type}/unsubscribe", params = "ajax")
    public @ResponseBody
    String ajaxUnsubscribe( @PathVariable String type, @RequestParam Long id )
    {
        unsubscribe( type, id );
        return "";
    }

    @RequestMapping("/subscription/{type}/subscribe")
    public String subscribe( @PathVariable String type, @RequestParam Long id,
        ModelMap models )
    {
        Subscribable subscribable = subscribe( type, id );
        String[] arguments = { subscribable.getType(), subscribable.getName() };
        models.put( "message", "status.subscribed" );
        models.put( "arguments", arguments );
        return "status";
    }

    @RequestMapping("/subscription/{type}/unsubscribe")
    public String unsubscribe( @PathVariable String type,
        @RequestParam Long id, ModelMap models )
    {
        Subscribable subscribable = unsubscribe( type, id );
        String[] arguments = { subscribable.getType(), subscribable.getName() };
        models.put( "message", "status.unsubscribed" );
        models.put( "arguments", arguments );
        return "status";
    }

}
