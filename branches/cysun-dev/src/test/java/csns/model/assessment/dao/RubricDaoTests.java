/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.assessment.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.Rubric;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Test(groups = "RubricDaoTests", dependsOnGroups = { "UserDaoTests",
    "DepartmentDaoTests" })
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class RubricDaoTests extends
    AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RubricDao rubricDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Test
    public void getRubric()
    {
        Rubric rubric = rubricDao.getRubric( 1001400L );
        assert rubric != null;
    }

    @Test(dependsOnMethods = "getRubric")
    public void getIndicators()
    {
        Rubric rubric = rubricDao.getRubric( 1001400L );
        assert rubric.getIndicators().size() == 2;
        assert rubric.getIndicators().get( 0 ).getCriteria().size() == 3;
    }

    @Test
    public void getDepartmentRubrics()
    {
        Department department = departmentDao.getDepartment( "cs" );
        assert rubricDao.getDepartmentRubrics( department ).size() == 0;
    }

    @Test
    public void getPersonalRubrics()
    {
        User creator = userDao.getUserByUsername( "cysun" );
        assert rubricDao.getPersonalRubrics( creator ).size() == 1;
    }

}
