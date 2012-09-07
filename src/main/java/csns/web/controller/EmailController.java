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

import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.FileDao;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.helper.Email;
import csns.web.validator.EmailValidator;

@Controller
@SessionAttributes("email")
public class EmailController {

    @Autowired
    UserDao userDao;

    @Autowired
    FileDao fileDao;

    @Autowired
    FileIO fileIO;

    @Autowired
    MailSender mailSender;

    @Autowired
    EmailValidator emailValidator;

    @Resource(name = "applicationProperties")
    Properties applicationProperties;

    private static final Logger logger = LoggerFactory.getLogger( EmailController.class );

    @RequestMapping("/email/compose")
    public String compose(
        @RequestParam(value = "userId", required = false) Long ids[],
        ModelMap models )
    {
        Email email = new Email( applicationProperties );
        email.setAuthor( SecurityUtils.getUser() );
        email.setRecipients( userDao.getUsers( ids ) );

        models.put( "email", email );
        return "email/compose";
    }

    @RequestMapping(value = "/email/compose", params = "send")
    public String compose( @ModelAttribute Email email,
        @RequestParam("userId") Long ids[],
        @RequestParam("file") MultipartFile[] uploadedFiles,
        @RequestParam(value = "backUrl", required = false) String backUrl,
        BindingResult result, SessionStatus sessionStatus, ModelMap models )
    {
        email.setRecipients( userDao.getUsers( ids ) );
        emailValidator.validate( email, result );
        if( result.hasErrors() ) return "email/compose";

        User user = SecurityUtils.getUser();
        for( MultipartFile uploadedFile : uploadedFiles )
        {
            if( uploadedFile.isEmpty() ) continue;

            File file = new File();
            file.setName( uploadedFile.getOriginalFilename() );
            file.setType( uploadedFile.getContentType() );
            file.setSize( uploadedFile.getSize() );
            file.setDate( new Date() );
            file.setOwner( user );
            file.setPublic( true );
            file = fileDao.saveFile( file );
            fileIO.save( file, uploadedFile );
            email.addAttachment( file );
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject( email.getSubject() );
        message.setText( email.getContent() );
        message.setFrom( user.getPrimaryEmail() );
        message.setCc( user.getPrimaryEmail() );
        message.setTo( email.getTo() );

        mailSender.send( message );
        sessionStatus.setComplete();
        logger.info( user.getUsername() + " sent email to " + email.getTo() );

        models.put( "backUrl", backUrl );
        models.put( "message", "status.email.sent" );
        return "status";
    }

}
