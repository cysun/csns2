/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013-2014, Chengyu Sun (csun@calstatela.edu).
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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.util.ImageUtils;
import csns.web.validator.EditUserValidator;

@Controller
@SessionAttributes("user")
@SuppressWarnings("deprecation")
public class ProfileControllerS {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EditUserValidator editUserValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileIO fileIO;

    @Autowired
    private ImageUtils imageUtils;

    private static final Logger logger = LoggerFactory.getLogger( ProfileControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Date.class, new CustomDateEditor(
            new SimpleDateFormat( "MM/dd/yyyy" ), true ) );
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public String edit( ModelMap models )
    {
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        models.put( "user", user );
        return "profile/edit";
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String edit(
        @ModelAttribute("user") User cmd,
        @RequestParam(value = "file", required = false) MultipartFile uploadedFile,
        HttpServletRequest request, BindingResult bindingResult,
        SessionStatus sessionStatus )
    {
        editUserValidator.validate( cmd, bindingResult );
        if( bindingResult.hasErrors() ) return "profile/edit";

        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        user.copySelfEditableFieldsFrom( cmd );
        String password = cmd.getPassword1();
        if( StringUtils.hasText( password ) )
            user.setPassword( passwordEncoder.encodePassword( password, null ) );
        user = userDao.saveUser( user );

        logger.info( user.getUsername() + " edited profile." );

        if( uploadedFile != null && !uploadedFile.isEmpty() )
        {
            File picture0 = fileIO.save( uploadedFile, user, true );
            File picture1 = imageUtils.resizeToProfilePicture( picture0 );
            File picture2 = imageUtils.resizeToProfileThumbnail( picture0 );
            user.setOriginalPicture( picture0 );
            user.setProfilePicture( picture1 );
            user.setProfileThumbnail( picture2 );
            user = userDao.saveUser( user );

            logger.info( "Saved profile picture for " + user.getUsername() );
        }

        sessionStatus.setComplete();
        return "redirect:/profile";
    }

}
