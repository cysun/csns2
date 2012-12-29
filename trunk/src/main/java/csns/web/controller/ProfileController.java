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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.mailinglist.Mailinglist;
import csns.security.SecurityUtils;
import csns.util.DefaultUrls;
import csns.web.validator.EditUserValidator;

@Controller
@SessionAttributes("user")
public class ProfileController {

    @Autowired
    UserDao userDao;

    @Autowired
    SubscriptionDao subscriptionDao;

    @Autowired
    EditUserValidator editUserValidator;

    @Autowired
    DefaultUrls defaultUrls;

    @Autowired
    PasswordEncoder passwordEncoder;

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Date.class, new CustomDateEditor(
            new SimpleDateFormat( "MM/dd/yyyy" ), true ) );
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile( ModelMap models )
    {
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        models.put( "user", user );
        return "profile/account";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profile( @ModelAttribute("user") User cmd,
        HttpServletRequest request, BindingResult bindingResult,
        SessionStatus sessionStatus )
    {
        editUserValidator.validate( cmd, bindingResult );
        if( bindingResult.hasErrors() ) return "profile/account";

        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        user.copySelfEditableFieldsFrom( cmd );
        String password = cmd.getPassword1();
        if( StringUtils.hasText( password ) )
            user.setPassword( passwordEncoder.encodePassword( password, null ) );
        user = userDao.saveUser( user );

        sessionStatus.setComplete();
        return "redirect:" + defaultUrls.userHomeUrl( request );
    }

    @RequestMapping("/profile/forums")
    public String forumSubscriptions( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        List<Subscription> subscriptions = subscriptionDao.getSubscriptions(
            user, Forum.class );

        List<Forum> departmentForums = new ArrayList<Forum>();
        List<Forum> courseForums = new ArrayList<Forum>();
        List<Forum> otherForums = new ArrayList<Forum>();
        for( Subscription subscription : subscriptions )
        {
            Forum forum = (Forum) subscription.getSubscribable();
            if( forum.getDepartment() != null )
                departmentForums.add( forum );
            else if( forum.getCourse() != null )
                courseForums.add( forum );
            else
                otherForums.add( forum );
        }

        models.put( "departmentForums", departmentForums );
        models.put( "courseForums", courseForums );
        models.put( "otherForums", otherForums );

        return "profile/forums";
    }

    @RequestMapping("/profile/mailinglists")
    public String mailinglistSubscriptions( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        List<Subscription> subscriptions = subscriptionDao.getSubscriptions(
            user, Mailinglist.class );

        models.put( "subscriptions", subscriptions );
        return "profile/mailinglists";
    }

}
