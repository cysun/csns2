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

import csns.model.core.User;

@Test(groups = "UserDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class UserDaoTests extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDao userDao;

    @Test
    public void getUserById()
    {
        assert userDao.getUser( 1000L ) != null;
    }

    @Test
    public void getUserByCin()
    {
        assert userDao.getUserByCin( "1000" ) != null;
    }

    @Test
    public void getUserByUsername()
    {
        assert userDao.getUserByUsername( "cysun" ) != null;
        assert userDao.getUserByUsername( "jdoe1" ) != null;
        assert userDao.getUserByUsername( "jdoe2" ) != null;
    }

    @Test
    public void getUsers()
    {
        Long ids[] = { 1000002L, 1000003L };
        List<User> users = userDao.getUsers( ids );

        assert users.get( 0 ).getFirstName().equals( "Jane" );
        assert users.get( 1 ).getFirstName().equals( "John" );
    }

    @Test
    public void saveUser()
    {
        User user = new User();
        user.setCin( "123456789" );
        user.setUsername( "testuser1" );
        user.setPassword( "testuser1" );
        user.setLastName( "User" );
        user.setFirstName( "Test" );
        user.setPrimaryEmail( "testuser1@localhost.localdomain" );

        user = userDao.saveUser( user );
        assert user.getId() != null;
    }

}
