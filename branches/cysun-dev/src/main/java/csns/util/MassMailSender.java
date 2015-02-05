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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import csns.model.core.AbstractMessage;
import csns.model.core.Subscribable;

/**
 * Sometimes an email server may reject an email as spam if the list of
 * recipients is too long. MassMailSender breaks the list of recipients into
 * groups (of 30 by default) and send a separate email to each group. It also
 * uses bcc so a recipient won't see other people's email addresses.
 */
@Component
public class MassMailSender {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    EmailUtils emailUtils;

    int maxRecipientsPerMessage = 30;

    private final static Logger logger = LoggerFactory.getLogger( MassMailSender.class );

    public MassMailSender()
    {
    }

    public void send( SimpleMailMessage email, List<String> addresses )
    {
        List<String> bccAddresses = new ArrayList<String>();
        for( int i = 0; i < addresses.size(); ++i )
        {
            if( !addresses.get( i ).endsWith( "@localhost" ) )
                bccAddresses.add( addresses.get( i ) );
            if( bccAddresses.size() >= maxRecipientsPerMessage
                || bccAddresses.size() > 0 && i == addresses.size() - 1 )
            {
                email.setBcc( bccAddresses.toArray( new String[0] ) );
                try
                {
                    mailSender.send( email );
                }
                catch( MailException e )
                {
                    logger.warn( e.getMessage() );
                }
                logger.debug( "sent email to "
                    + StringUtils.collectionToCommaDelimitedString( bccAddresses ) );
                bccAddresses.clear();
            }
        }
    }

    public void send( AbstractMessage message, Subscribable subscribable )
    {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject( message.getSubject() );
        email.setText( emailUtils.getText( message ) );
        email.setFrom( message.getAuthor().getPrimaryEmail() );
        email.setTo( emailUtils.getAppEmail() );
        send( email, emailUtils.getAddresses( subscribable ) );
    }

}
