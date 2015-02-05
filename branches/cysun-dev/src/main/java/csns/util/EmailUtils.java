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
package csns.util;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import csns.helper.Email;
import csns.model.core.AbstractMessage;
import csns.model.core.File;
import csns.model.core.Subscribable;
import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;

@Component
public class EmailUtils {

    @Autowired
    SubscriptionDao subscriptionDao;

    @Autowired
    JavaMailSender mailSender;

    @Value("#{applicationProperties.url}")
    private String appUrl;

    @Value("#{applicationProperties.email}")
    private String appEmail;

    private static final Logger logger = LoggerFactory.getLogger( EmailUtils.class );

    public List<String> getAddresses( List<User> users,
        boolean useSecondaryEmail )
    {
        List<String> addresses = new ArrayList<String>();
        for( User user : users )
        {
            String email = user.getPrimaryEmail();
            if( useSecondaryEmail
                && StringUtils.hasText( user.getSecondaryEmail() ) )
                email = user.getSecondaryEmail();

            if( !email.endsWith( "@localhost" ) ) addresses.add( email );
        }
        return addresses;
    }

    public List<String> getAddresses( List<User> users )
    {
        return getAddresses( users, false );
    }

    public List<String> getAddresses( Subscribable subscribable )
    {
        List<Subscription> subscriptions = subscriptionDao.getSubscriptions( subscribable );
        List<String> addresses = new ArrayList<String>();
        for( Subscription subscription : subscriptions )
            addresses.add( subscription.getSubscriber().getPrimaryEmail() );
        return addresses;
    }

    public String getText( AbstractMessage message )
    {
        StringBuffer sb = new StringBuffer( message.getContent() );

        List<File> attachments = message.getAttachments();
        if( attachments.size() > 0 )
        {
            sb.append( "\n\n[Attachments]\n\n" );
            for( int i = 0; i < attachments.size(); ++i )
                sb.append( "\t(" )
                    .append( i + 1 )
                    .append( ") " )
                    .append( attachments.get( i ).getName() )
                    .append( " - " )
                    .append( appUrl )
                    .append( "/download?fileId=" )
                    .append( attachments.get( i ).getId() )
                    .append( "\n" );
        }

        return sb.toString();
    }

    public String getHtml( AbstractMessage message )
    {
        StringBuffer sb = new StringBuffer( message.getContent() );

        List<File> attachments = message.getAttachments();
        if( attachments.size() > 0 )
        {
            sb.append( "<p>[Attachments]</p>" ).append( "<ul>\n" );
            for( int i = 0; i < attachments.size(); ++i )
                sb.append( "<li><a href='" )
                    .append( appUrl )
                    .append( "/download?fileId=" )
                    .append( attachments.get( i ).getId() )
                    .append( "'>" )
                    .append( attachments.get( i ).getName() )
                    .append( "</a></li>\n" );
            sb.append( "</ul>" );
        }

        return sb.toString();
    }

    public boolean sendTextMail( Email email )
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject( email.getSubject() );
        message.setText( getText( email ) );
        message.setFrom( email.getAuthor().getPrimaryEmail() );
        message.setCc( email.getAuthor().getPrimaryEmail() );
        String addresses[] = getAddresses( email.getRecipients(),
            email.isUseSecondaryEmail() ).toArray( new String[0] );
        if( addresses.length > 1 )
        {
            message.setTo( appEmail );
            message.setBcc( addresses );
        }
        else
            message.setTo( addresses );

        mailSender.send( message );

        logger.info( email.getAuthor().getUsername() + " sent email to "
            + StringUtils.arrayToCommaDelimitedString( addresses ) );

        return true;
    }

    public boolean sendHtmlMail( Email email )
    {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper( message );

        try
        {
            message.setContent( getHtml( email ), "text/html" );

            helper.setSubject( email.getSubject() );
            helper.setFrom( email.getAuthor().getPrimaryEmail() );
            helper.setCc( email.getAuthor().getPrimaryEmail() );
            String addresses[] = getAddresses( email.getRecipients(),
                email.isUseSecondaryEmail() ).toArray( new String[0] );
            if( addresses.length > 1 )
            {
                helper.setTo( appEmail );
                helper.setBcc( addresses );
            }
            else
                helper.setTo( addresses );

            mailSender.send( message );

            logger.info( email.getAuthor().getUsername() + " sent email to "
                + StringUtils.arrayToCommaDelimitedString( addresses ) );

            return true;
        }
        catch( MessagingException e )
        {
            logger.warn( "Fail to send MIME message", e );
        }

        return false;
    }

    public String getAppUrl()
    {
        return appUrl;
    }

    public String getAppEmail()
    {
        return appEmail;
    }

}
