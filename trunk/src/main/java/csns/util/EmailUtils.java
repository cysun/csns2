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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import csns.model.core.AbstractMessage;
import csns.model.core.File;
import csns.model.core.Subscribable;
import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;

@Component
public class EmailUtils {

    @Value("#{applicationProperties.url}")
    private String appUrl;

    @Value("#{applicationProperties.email}")
    private String appEmail;

    @Autowired
    SubscriptionDao subscriptionDao;

    public List<String> getAddresses( List<User> users,
        boolean useSecondaryEmail )
    {
        List<String> addresses = new ArrayList<String>();
        for( User user : users )
        {
            if( useSecondaryEmail
                && StringUtils.hasText( user.getSecondaryEmail() ) )
                addresses.add( user.getSecondaryEmail() );
            else
                addresses.add( user.getPrimaryEmail() );
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

    public String getAppUrl()
    {
        return appUrl;
    }

    public String getAppEmail()
    {
        return appEmail;
    }

}
