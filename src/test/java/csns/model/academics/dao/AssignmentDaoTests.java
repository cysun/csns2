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
import csns.model.academics.Submission;

@Test(groups = "AssignmentDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class AssignmentDaoTests extends
    AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    AssignmentDao assignmentDao;

    @Test
    public void getAssignment()
    {
        assert assignmentDao.getAssignment( 1000500L ) != null;
    }

    /**
     * To test lazy loading of collections like submissions, the test class must
     * extend AbstractTransactionalTestNGSpringContextTests instead of
     * AbstractTestNGSpringContextTests so Spring will run each test method in a
     * transaction and keep the entity manager open until the method finishes.
     */
    @Test(dependsOnMethods = "getAssignment")
    public void loadSubmissions()
    {
        Assignment assignment = assignmentDao.getAssignment( 1000500L );
        List<Submission> submissions = assignment.getSubmissions();

        assert submissions.size() == 2;
    }

}
