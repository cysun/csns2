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
package csns.model.academics.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Assignment;
import csns.model.academics.Section;
import csns.model.academics.Submission;
import csns.model.academics.dao.SubmissionDao;
import csns.model.core.User;

@Repository
public class SubmissionDaoImpl implements SubmissionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Submission getSubmission( Long id )
    {
        return entityManager.find( Submission.class, id );
    }

    @Override
    public Submission getSubmission( User student, Assignment assignment )
    {
        String query = "from Submission submission "
            + "where student = :student and assignment = :assignment";

        List<Submission> submissions = entityManager.createQuery( query,
            Submission.class )
            .setParameter( "student", student )
            .setParameter( "assignment", assignment )
            .getResultList();
        return submissions.size() == 0 ? null : submissions.get( 0 );
    }

    @Override
    public List<Submission> getSubmissions( User student, Section section )
    {
        String query = "from Submission submission "
            + "where submission.student = :student "
            + "and submission.assignment.section = :section";

        return entityManager.createQuery( query, Submission.class )
            .setParameter( "student", student )
            .setParameter( "section", section )
            .getResultList();
    }

    @Override
    @Transactional
    public Submission saveSubmission( Submission Submission )
    {
        return entityManager.merge( Submission );
    }

}
