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
package csns.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import csns.model.core.dao.SubscriptionDao;

@Component
public class SimpleTasks {

    private static final Logger logger = LoggerFactory.getLogger( SimpleTasks.class );

    @Autowired
    SubscriptionDao subscriptionDao;

    // Run this task every Monday at 1:01:01AM.
    @Scheduled(cron = "1 1 1 ? * 1")
    public void autoUnsubscribe()
    {
        int n = subscriptionDao.autoUnsubscribe();
        logger.info( "Removed " + n + " subscriptions." );
    }

}
