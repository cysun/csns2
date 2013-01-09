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

import csns.model.academics.Department;
import csns.model.academics.Project;

@Test(groups = "ProjectDaoTests", dependsOnGroups = "DepartmentDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class ProjectDaoTests extends
    AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    ProjectDao projectDao;

    @Test
    public void getProjects()
    {

        Department cs = departmentDao.getDepartment( "cs" );
        List<Project> projects = projectDao.getProjects( cs, 2013 );
        assert projects.size() >= 1;

        Project project = projects.get( 0 );
        assert project.getAdvisors().get( 0 ).getUsername().equals( "cysun" );
        assert project.getStudents().size() == 2;
    }

}
