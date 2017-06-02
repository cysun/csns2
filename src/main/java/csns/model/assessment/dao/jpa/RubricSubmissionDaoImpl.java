/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014-2015,2017, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.assessment.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Section;
import csns.model.assessment.RubricAssignment;
import csns.model.assessment.RubricSubmission;
import csns.model.assessment.dao.RubricSubmissionDao;
import csns.model.core.User;

@Repository
public class RubricSubmissionDaoImpl implements RubricSubmissionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public RubricSubmission getRubricSubmission( Long id )
    {
        return entityManager.find( RubricSubmission.class, id );
    }

    @Override
    public RubricSubmission getRubricSubmission( User student,
        RubricAssignment assignment )
    {
        String query = "from RubricSubmission where student = :student "
            + "and assignment = :assignment";

        List<RubricSubmission> results = entityManager
            .createQuery( query, RubricSubmission.class )
            .setParameter( "student", student )
            .setParameter( "assignment", assignment )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    public List<RubricSubmission> getRubricSubmissions( User student,
        Section section )
    {
        String query = "from RubricSubmission submission "
            + "where submission.student = :student "
            + "and submission.assignment.section = :section "
            + "and submission.assignment.deleted = false";

        return entityManager.createQuery( query, RubricSubmission.class )
            .setParameter( "student", student )
            .setParameter( "section", section )
            .getResultList();
    }

    @Override
    @Transactional
    public RubricSubmission saveRubricSubmission( RubricSubmission submission )
    {
        return entityManager.merge( submission );
    }

}
