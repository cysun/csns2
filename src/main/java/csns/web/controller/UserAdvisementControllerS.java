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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.helper.Email;
import csns.model.advisement.AdvisementRecord;
import csns.model.advisement.dao.AdvisementRecordDao;
import csns.model.core.File;
import csns.model.core.User;
import csns.security.SecurityUtils;
import csns.util.EmailUtils;
import csns.util.FileIO;
import csns.web.validator.EmailValidator;

@Controller
@SessionAttributes({ "email", "record" })
public class UserAdvisementControllerS {

    @Autowired
    private AdvisementRecordDao advisementRecordDao;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private FileIO fileIO;

    private Logger logger = LoggerFactory.getLogger( UserAdvisementControllerS.class );

    @RequestMapping(value = "/user/advisement/edit", method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "record", advisementRecordDao.getAdvisementRecord( id ) );
        return "user/advisement/edit";
    }

    @RequestMapping(value = "/user/advisement/edit",
        method = RequestMethod.POST)
    public String edit(
        @ModelAttribute("record") AdvisementRecord record,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles,
        SessionStatus sessionStatus )
    {
        User student = record.getStudent();
        if( uploadedFiles != null )
            record.getAttachments().addAll(
                fileIO.save( uploadedFiles, student, false ) );

        record = advisementRecordDao.saveAdvisementRecord( record );

        logger.info( SecurityUtils.getUser().getUsername()
            + " edited advisment record " + record.getId() );

        sessionStatus.setComplete();
        // Advisement is the 5th tab
        return "redirect:/user/view?id=" + student.getId() + "#4";
    }

    @RequestMapping("/user/advisement/deleteAttachment")
    public @ResponseBody String deleteAttachment(
        @ModelAttribute("record") AdvisementRecord record,
        @RequestParam Long fileId )
    {
        for( File attachment : record.getAttachments() )
            if( attachment.getId().equals( fileId ) )
            {
                record.getAttachments().remove( attachment );

                logger.info( SecurityUtils.getUser().getUsername()
                    + " deleted attachment " + fileId
                    + " from advisement record " + record.getId() );

                break;
            }

        return "";
    }

    @RequestMapping(value = "/user/advisement/email",
        method = RequestMethod.GET)
    public String email( @RequestParam Long id, ModelMap models )
    {
        AdvisementRecord record = advisementRecordDao.getAdvisementRecord( id );

        Email email = new Email();
        email.setAuthor( SecurityUtils.getUser() );
        email.addRecipient( record.getStudent() );
        email.setSubject( "Advisement Record #" + record.getId() );
        email.setContent( record.getComment() );
        email.setAttachments( record.getAttachments() );

        models.put( "record", record );
        models.put( "email", email );
        return "user/advisement/email";
    }

    @RequestMapping(value = "/user/advisement/email",
        method = RequestMethod.POST)
    public String email( @ModelAttribute Email email, BindingResult result,
        SessionStatus sessionStatus, ModelMap models )
    {
        emailValidator.validate( email, result );
        if( result.hasErrors() ) return "user/advisement/email";

        emailUtils.sendHtmlMail( email );
        sessionStatus.setComplete();

        models.put( "backUrl", "/user/view?id="
            + email.getRecipients().get( 0 ).getId() + "#4" );
        models.put( "message", "status.email.sent" );
        return "status";
    }

}
