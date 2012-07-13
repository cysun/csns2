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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.academics.Course;
import csns.model.academics.Quarter;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Test(groups = "SectionDaoTests", dependsOnGroups = { "UserDaoTests",
    "CourseDaoTests" })
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class SectionDaoTests extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDao userDao;

    @Autowired
    CourseDao courseDao;

    @Autowired
    SectionDao sectionDao;

    @Test
    public void getSectionsByQuarter()
    {
        Quarter f10 = new Quarter( 1109 );
        assert sectionDao.getSections( f10 ).size() == 1;
    }

    @Test
    public void getSectionsByCourse()
    {
        Course cs520 = courseDao.getCourse( "CS520" );
        assert sectionDao.getSections( cs520 ).size() == 1;
    }

    @Test
    public void getSectionsByInstructor()
    {
        Quarter f10 = new Quarter( 1109 );
        User cysun = userDao.getUserByUsername( "cysun" );
        assert sectionDao.getSectionsByInstructor( cysun, f10 ).size() == 1;
    }

    @Test
    public void getSectionsByStudent()
    {
        Quarter f10 = new Quarter( 1109 );
        User jdoe1 = userDao.getUserByUsername( "jdoe1" );

        assert sectionDao.getSectionsByStudent( jdoe1, f10 ).size() == 1;
    }

}
