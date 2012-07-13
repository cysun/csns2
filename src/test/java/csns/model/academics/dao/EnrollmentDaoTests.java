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

import csns.model.academics.Enrollment;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Test(groups = "EnrollmentDaoTests", dependsOnGroups = "UserDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class EnrollmentDaoTests extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDao userDao;

    @Autowired
    EnrollmentDao enrollmentDao;

    @Test
    public void getEnrollments()
    {
        User jdoe1 = userDao.getUserByUsername( "jdoe1" );
        List<Enrollment> enrollments = enrollmentDao.getEnrollments( jdoe1 );
        assert enrollments.size() == 2;
        assert enrollments.get( 1 ).getSection().getQuarter().getCode() == 1109;
    }

}
