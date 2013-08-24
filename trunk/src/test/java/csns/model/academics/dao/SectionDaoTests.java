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
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.academics.Assignment;
import csns.model.academics.Department;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Test(groups = "SectionDaoTests", dependsOnGroups = { "UserDaoTests",
    "DepartmentDaoTests" })
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class SectionDaoTests extends
    AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    UserDao userDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    SectionDao sectionDao;

    @Test
    public void getSection()
    {
        assert sectionDao.getSection( 1000300L ) != null;
    }

    @Test
    public void getSections()
    {
        Department department = departmentDao.getDepartment( "cs" );
        assert sectionDao.getSections( department, new Quarter() ).size() == 2;
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

    /**
     * To test lazy loading of collections like assignments, the test class must
     * extend AbstractTransactionalTestNGSpringContextTests instead of
     * AbstractTestNGSpringContextTests so Spring will run each test method in a
     * transaction and keep the entity manager open until the method finishes.
     */
    @Test(dependsOnMethods = "getSection")
    public void loadAssignments()
    {
        Section section = sectionDao.getSection( 1000300L );
        List<Assignment> assignments = section.getAssignments();

        assert assignments.size() == 2;
        assert assignments.get( 0 ).getName().equals( "Homework 1" );
        assert assignments.get( 1 ).getName().equals( "Homework 2" );
    }

    @Test(dependsOnMethods = "loadAssignments")
    public void addAssignment()
    {
        Section section = sectionDao.getSection( 1000300L );
        Assignment assignment = new Assignment();
        assignment.setName( "Homework 3" );
        assignment.setAlias( "HW3" );
        assignment.setSection( section );
        section.getAssignments().add( assignment );
        sectionDao.saveSection( section );

        assert section.getAssignments().size() == 3;
        assert section.getAssignments().get( 2 ).getAlias().equals( "HW3" );
    }

}
