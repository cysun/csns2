/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.util.DefaultUrls;

@Controller
@SuppressWarnings("deprecation")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DefaultUrls defaultUrls;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("#{applicationProperties.email}")
    private String appEmail;

    @Value("#{applicationProperties.encoding}")
    private String appEncoding;

    private final static Logger logger = LoggerFactory.getLogger( UserController.class );

    @RequestMapping(value = "/user/search")
    public String search( @RequestParam(required = false) String term,
        @RequestParam(required = false) String dept, ModelMap models )
    {
        List<User> users = null;
        if( StringUtils.hasText( term ) )
        {
            if( term.toLowerCase().contains( "standing:" ) )
            {
                String words[] = term.split( ":" );
                if( words.length > 1 )
                    users = userDao.searchUsersByStanding( dept,
                        words[1].trim() );
            }
            else
                users = userDao.searchUsers( term );
        }
        models.addAttribute( "users", users );
        return "user/search";
    }

    @RequestMapping(value = "/user/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "user", userDao.getUser( id ) );
        return "user/view";
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public String resetPassword( ModelMap models )
    {
        return "resetPassword";
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword( HttpServletRequest request, ModelMap models )
    {
        String username = request.getParameter( "username" );
        String cin = request.getParameter( "cin" );
        String email = request.getParameter( "email" );

        User user = null;
        if( StringUtils.hasText( cin ) )
            user = userDao.getUserByCin( cin );
        else if( StringUtils.hasText( username ) )
            user = userDao.getUserByUsername( username );
        else if( StringUtils.hasText( email ) )
            user = userDao.getUserByEmail( email );

        models.put( "backUrl", defaultUrls.homeUrl( request ) );

        if( user == null )
        {
            models.put( "message", "error.reset.password.user.not.found" );
            return "error";
        }

        if( user.isTemporary() )
        {
            models.put( "message", "error.reset.password.temporary.user" );
            return "error";
        }

        String newPassword = "" + (int) (Math.random() * 100000000);
        user.setPassword( passwordEncoder.encodePassword( newPassword, null ) );
        userDao.saveUser( user );

        logger.info( "Reset password for " + user.getUsername() );

        Map<String, Object> vModels = new HashMap<String, Object>();
        vModels.put( "username", user.getUsername() );
        vModels.put( "password", newPassword );
        String text = VelocityEngineUtils.mergeTemplateIntoString(
            velocityEngine, "email.resetPassword.vm", appEncoding, vModels );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo( user.getPrimaryEmail() );
        message.setFrom( appEmail );
        message.setText( text );
        try
        {
            mailSender.send( message );
            logger.info( "Password reset message sent to "
                + user.getPrimaryEmail() );
        }
        catch( MailException e )
        {
            logger.error( e.getMessage() );
            models.put( "message", "error.reset.password.email.failure" );
            return "error";
        }

        models.put( "message", "status.reset.password" );
        return "status";
    }

}
