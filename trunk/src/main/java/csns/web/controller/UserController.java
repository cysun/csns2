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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.util.DefaultUrls;
import csns.web.validator.AddUserValidator;
import csns.web.validator.EditUserValidator;
import csns.web.validator.RegistrationValidator;

@Controller
@SessionAttributes("user")
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    DefaultUrls defaultUrls;

    @Autowired
    AddUserValidator addUserValidator;

    @Autowired
    EditUserValidator editUserValidator;

    @Autowired
    RegistrationValidator registrationValidator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    VelocityEngine velocityEngine;

    @Value("#{applicationProperties.email}")
    String appEmail;

    private final static Logger logger = LoggerFactory.getLogger( UserController.class );

    /*
     * The default Spring date property editor does not accept null or empty
     * string so we have to explicitly register one that does.
     */
    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Date.class, new CustomDateEditor(
            new SimpleDateFormat( "MM/dd/yyyy" ),
            true ) );
    }

    @RequestMapping(value = "/user/search")
    public String search( @RequestParam(required = false) String term,
        ModelMap models )
    {
        List<User> users = null;
        if( StringUtils.hasText( term ) ) users = userDao.searchUsers( term );
        models.addAttribute( "users", users );
        return "user/search";
    }

    @RequestMapping(value = "/user/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "user", userDao.getUser( id ) );
        return "user/view";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String add( ModelMap models )
    {
        models.put( "user", new User() );
        return "user/add";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String add( @ModelAttribute User user, BindingResult bindingResult,
        SessionStatus sessionStatus )
    {
        addUserValidator.validate( user, bindingResult );
        if( bindingResult.hasErrors() ) return "user/add";

        user.setUsername( user.getCin() );
        user.setPassword( passwordEncoder.encodePassword( user.getCin(), null ) );
        if( !StringUtils.hasText( user.getPrimaryEmail() ) )
            user.setPrimaryEmail( user.getCin() + "@localhost" );
        user.setTemporary( true );
        user = userDao.saveUser( user );

        sessionStatus.setComplete();
        return "redirect:/user/view?id=" + user.getId();
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "user", userDao.getUser( id ) );
        return "user/edit";
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String edit( @ModelAttribute User user, BindingResult bindingResult,
        SessionStatus sessionStatus )
    {
        editUserValidator.validate( user, bindingResult );
        if( bindingResult.hasErrors() ) return "user/edit";

        String password = user.getPassword1();
        if( StringUtils.hasText( password ) )
            user.setPassword( passwordEncoder.encodePassword( password, null ) );
        user = userDao.saveUser( user );

        sessionStatus.setComplete();
        return "redirect:/user/view?id=" + user.getId();
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register( ModelMap models )
    {
        User user = SecurityUtils.getUser().clone();
        user.setUsername( null );
        user.setPrimaryEmail( null );
        models.put( "user", user );
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register( @ModelAttribute("user") User cmd,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        registrationValidator.validate( cmd, bindingResult );
        if( bindingResult.hasErrors() ) return "register";

        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        user.copySelfEditableFieldsFrom( cmd );
        user.setUsername( cmd.getUsername() );
        user.setPassword( passwordEncoder.encodePassword( cmd.getPassword1(),
            null ) );
        user.setTemporary( false );
        userDao.saveUser( user );

        sessionStatus.setComplete();
        return "redirect:/j_spring_security_logout";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile( ModelMap models )
    {
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        models.put( "user", user );
        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profile( @ModelAttribute("user") User cmd,
        HttpServletRequest request, BindingResult bindingResult,
        SessionStatus sessionStatus )
    {
        editUserValidator.validate( cmd, bindingResult );
        if( bindingResult.hasErrors() ) return "profile";

        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        user.copySelfEditableFieldsFrom( cmd );
        String password = cmd.getPassword1();
        if( StringUtils.hasText( password ) )
            user.setPassword( passwordEncoder.encodePassword( password, null ) );
        user = userDao.saveUser( user );

        sessionStatus.setComplete();
        return "redirect:" + defaultUrls.userHomeUrl( request );
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

        Map<String, String> vModels = new HashMap<String, String>();
        vModels.put( "username", user.getUsername() );
        vModels.put( "password", newPassword );
        String text = VelocityEngineUtils.mergeTemplateIntoString(
            velocityEngine, "email.resetPassword.vm", vModels );

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
