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
package csns.model.core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.forum.Forum;
import csns.model.forum.Topic;
import csns.model.forum.dao.ForumDao;
import csns.model.forum.dao.TopicDao;

@Test(groups = "SubscriptionDaoTests", dependsOnGroups = { "UserDaoTests",
    "ForumDaoTests", "TopicDaoTests" })
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class SubscriptionDaoTests extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDao userDao;

    @Autowired
    ForumDao forumDao;

    @Autowired
    TopicDao topicDao;

    @Autowired
    SubscriptionDao subscriptionDao;

    @Test
    public void getSubscription()
    {
        Forum forum = forumDao.getForum( 3000L );
        User subscriber = userDao.getUser( 1000001L );
        assert subscriptionDao.getSubscription( forum, subscriber ) != null;
    }

    @Test
    public void getForumSubscribers()
    {
        Forum forum = forumDao.getForum( 3000L );
        List<Subscription> subscriptions = subscriptionDao.getSubscriptions( forum );
        assert subscriptions.size() == 1;
        assert subscriptions.get( 0 )
            .getSubscriber()
            .getUsername()
            .equals( "cysun" );
    }

    @Test
    public void getUserSubscriptions()
    {
        User user = userDao.getUser( 1000001L );
        assert subscriptionDao.getSubscriptions( user, Topic.class ).size() == 1;
    }

}
