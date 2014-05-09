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
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import csns.model.core.Subscribable;
import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;

@Component
public class NotificationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private MassMailSender massMailSender;

    @Value("#{applicationProperties.url}")
    private String appUrl;

    @Value("#{applicationProperties.email}")
    private String appEmail;

    @Value("#{applicationProperties.encoding}")
    private String appEncoding;

    public void notifiy( Subscribable subscribable, String subject,
        String vTemplate, Map<String, Object> vModels, boolean notificationFlag )
    {
        vModels.put( "appUrl", appUrl );
        vModels.put( "appEmail", appEmail );

        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        List<Subscription> subscriptions = subscriptionDao.getSubscriptions( subscribable );
        List<String> addresses = new ArrayList<String>();
        for( Subscription subscription : subscriptions )
            if( !subscription.isNotificationSent()
                && subscription.getSubscriber() != user )
            {
                addresses.add( subscription.getSubscriber().getEmail() );
                if( subscription.isNotificationSent() != notificationFlag )
                {
                    subscription.setNotificationSent( notificationFlag );
                    subscriptionDao.saveSubscription( subscription );
                }
            }

        if( addresses.size() > 0 )
        {
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, vTemplate, appEncoding, vModels );
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom( appEmail );
            email.setTo( appEmail );
            email.setSubject( subject );
            email.setText( text );
            massMailSender.send( email, addresses );
        }
    }

}
