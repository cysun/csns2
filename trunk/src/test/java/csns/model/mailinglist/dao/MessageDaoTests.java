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
package csns.model.mailinglist.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.mailinglist.Mailinglist;
import csns.model.mailinglist.Message;

@Test(groups = "MessageDaoTests", dependsOnGroups = "MailinglistDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class MessageDaoTests extends AbstractTestNGSpringContextTests {

    @Autowired
    MailinglistDao mailinglistDao;

    @Autowired
    MessageDao messageDao;

    @Test
    public void searchMessages()
    {
        Mailinglist mailinglist = mailinglistDao.getMailinglist( 1001101L );
        List<Message> messages = messageDao.searchMessages( mailinglist,
            "welcome", 10 );

        assert messages.get( 0 ).getAuthor().getUsername().equals( "rpamula" );
    }

}
