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
package csns.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.util.DefaultUrls;
import csns.validator.AddUserValidator;
import csns.validator.EditUserValidator;
import csns.validator.RegistrationValidator;

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

    @RequestMapping(value = "/user/autocomplete")
    public String autocomplete( @RequestParam String term,
        HttpServletResponse response ) throws JSONException, IOException
    {
        JSONArray jsonArray = new JSONArray();
        List<User> users = userDao.searchUsersByPrefix( term );
        for( User user : users )
        {
            String label = user.isCinEncrypted() ? user.getName()
                : user.getCin() + " " + user.getName();

            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", user.getId().toString() );
            json.put( "value", user.getName() );
            json.put( "label", label );
            jsonArray.put( json );
        }

        response.setContentType( "application/json" );
        jsonArray.write( response.getWriter() );
        return null;
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
        User user = SecurityUtils.getUser().clone();
        models.put( "user", user );
        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profile( @ModelAttribute("user") User cmd,
        BindingResult bindingResult, SessionStatus sessionStatus )
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
        return "redirect:" + defaultUrls.homeUrl( user );
    }

}
