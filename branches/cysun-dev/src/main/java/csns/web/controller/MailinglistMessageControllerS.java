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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.core.User;
import csns.model.mailinglist.Message;
import csns.model.mailinglist.dao.MailinglistDao;
import csns.model.mailinglist.dao.MessageDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.util.MassMailSender;
import csns.web.validator.MessageValidator;

@Controller
@SessionAttributes("message")
public class MailinglistMessageControllerS {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MailinglistDao mailinglistDao;

    @Autowired
    private MessageValidator messageValidator;

    @Autowired
    private FileIO fileIO;

    @Autowired
    private MassMailSender massMailSender;

    @RequestMapping(value = "/department/{dept}/mailinglist/message/compose",
        method = RequestMethod.GET)
    public String compose( @RequestParam Long listId, ModelMap models )
    {
        Message message = new Message( mailinglistDao.getMailinglist( listId ) );
        models.put( "message", message );
        return "mailinglist/message/compose";
    }

    @RequestMapping(value = "/department/{dept}/mailinglist/message/compose",
        method = RequestMethod.POST)
    public String compose( @ModelAttribute Message message,
        @PathVariable String dept, @RequestParam(value = "file",
            required = false) MultipartFile[] uploadedFiles,
        BindingResult result, SessionStatus sessionStatus, ModelMap models )
    {
        messageValidator.validate( message, result );
        if( result.hasErrors() ) return "mailinglist/message/compose";

        User user = SecurityUtils.getUser();
        message.setAuthor( user );
        message.setDate( new Date() );
        if( uploadedFiles != null )
            message.getAttachments().addAll(
                fileIO.save( uploadedFiles, user, true ) );

        message = messageDao.saveMessage( message );
        massMailSender.send( message, message.getMailinglist() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/mailinglist/view?id="
            + message.getMailinglist().getId();
    }

}
