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
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.academics.Department;

/**
 * To test lazy loaded collections (e.g. administrators etc. in Department), the
 * test class must inherit AbstractTransactionalTestNGSpringContextTests instead
 * of AbstractTestNGSpringContextTests so Spring will run each test method in a
 * transaction and keep the entity manager open until the method finishes.
 */
@Test(groups = "DepartmentDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class DepartmentDaoTests extends
    AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    DepartmentDao departmentDao;

    @Test
    public void getDepartment()
    {
        assert departmentDao.getDepartment( "cs" ) != null;
    }

    @Test
    public void getDepartments()
    {
        assert departmentDao.getDepartments().size() == 2;
    }

    @Test(dependsOnMethods = "getDepartment")
    public void getDepartmentAdministrators()
    {
        Department cs = departmentDao.getDepartment( "cs" );

        assert cs.getAdministrators().size() == 2;
        assert cs.getAdministrators().get( 0 ).getUsername().equals( "rpamula" );
    }

}
