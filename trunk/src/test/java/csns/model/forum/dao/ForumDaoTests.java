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
package csns.model.forum.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;

@Test(groups = "ForumDaoTests", dependsOnGroups = "UserDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class ForumDaoTests extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDao userDao;

    @Autowired
    ForumDao forumDao;

    @Test
    public void getForum()
    {
        assert forumDao.getForum( 1000700L ) != null;
    }

    @Test
    public void getSystemForums()
    {
        User user = userDao.getUserByUsername( "cysun" );
        List<Forum> forums = forumDao.getSystemForums( user );

        assert forums.size() == 1;
        assert forums.get( 0 ).getName().equals( "CSNS" );
    }

    @Test
    public void getDepartmentForums()
    {
        User user = userDao.getUserByUsername( "cysun" );
        List<Forum> forums = forumDao.getDepartmentForums( user );

        assert forums.size() == 1;
        assert forums.get( 0 ).getDepartment().getAbbreviation().equals( "cs" );
    }

    @Test
    public void getCourseForums()
    {
        User user = userDao.getUserByUsername( "cysun" );
        List<Forum> forums = forumDao.getCourseForums( user );

        assert forums.size() == 1;
        assert forums.get( 0 ).getCourse().getCode().equals( "CS520" );
    }

}
