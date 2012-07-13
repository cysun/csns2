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
package csns.model.academics.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.academics.Quarter;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Test(groups = "QuarterDaoTests", dependsOnGroups = "UserDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class QuarterDaoTests extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDao userDao;

    @Autowired
    QuarterDao quarterDao;

    @Test
    public void getQuartersByInstructor()
    {
        User cysun = userDao.getUserByUsername( "cysun" );
        List<Quarter> quarters = quarterDao.getQuartersByInstructor( cysun );
        assert quarters.size() == 2;
        assert quarters.get( 1 ).getCode() == 1109;
    }

    @Test
    public void getQuartersByStudent()
    {
        User jdoe1 = userDao.getUserByUsername( "jdoe1" );
        List<Quarter> quarters = quarterDao.getQuartersByStudent( jdoe1 );
        assert quarters.size() == 2;
        assert quarters.get( 1 ).getCode() == 1109;
    }

}
