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

import csns.model.academics.Assignment;
import csns.model.academics.Section;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SectionDao;
import csns.model.assessment.RubricEvaluation;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Test(groups = "RubricEvaluationDaoTests", dependsOnGroups = { "UserDaoTests",
    "SectionDaoTests", "AssignmentDaoTests" })
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class RubricEvaluationDaoTests extends
    AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private RubricDao rubricDao;

    @Autowired
    private RubricEvaluationDao rubricEvaluationDao;

    @Test
    public void getRubricEvaluation()
    {
        assert rubricEvaluationDao.getRubricEvaluation( 1001450L ) != null;
    }

    @Test
    public void getRubricEvaluations1()
    {
        User evaluator = userDao.getUser( 1000001L );
        Section section = sectionDao.getSection( 1000301L );
        assert rubricEvaluationDao.getRubricEvaluations( section, evaluator ) != null;

        evaluator = userDao.getUser( 1000003L );
        Assignment assignment = assignmentDao.getAssignment( 1000502L );
        assert rubricEvaluationDao.getRubricEvaluations( assignment, evaluator ) != null;
    }

    @Test
    public void getRubricEvaluations2()
    {
        Section section = sectionDao.getSection( 1000301L );
        assert rubricEvaluationDao.getRubricEvaluations( section,
            RubricEvaluation.Type.INSTRUCTOR ).size() == 1;
        assert rubricEvaluationDao.getRubricEvaluations( section,
            RubricEvaluation.Type.PEER ).size() == 0;

        Assignment assignment = assignmentDao.getAssignment( 1000502L );
        assert rubricEvaluationDao.getRubricEvaluations( assignment,
            RubricEvaluation.Type.INSTRUCTOR ).size() == 0;
        assert rubricEvaluationDao.getRubricEvaluations( assignment,
            RubricEvaluation.Type.PEER ).size() == 1;
    }

}
