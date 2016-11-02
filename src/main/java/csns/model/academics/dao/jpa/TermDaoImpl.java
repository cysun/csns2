/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2016, Chengyu Sun (csun@calstatela.edu).
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

import csns.model.academics.Department;
import csns.model.academics.Term;
import csns.model.academics.dao.TermDao;
import csns.model.core.User;

@Repository
public class TermDaoImpl implements TermDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Term> getTermsByInstructor( User instructor )
    {
        String query = "select distinct section.term from Section section "
            + "join section.instructors instructor "
            + "where instructor = :instructor order by section.term desc";

        return entityManager.createQuery( query, Term.class )
            .setParameter( "instructor", instructor )
            .getResultList();
    }

    @Override
    public List<Term> getTermsByStudent( User student )
    {
        String query = "select distinct section.term from Section section "
            + "join section.enrollments enrollment "
            + "where enrollment.student = :student "
            + "order by section.term desc";

        return entityManager.createQuery( query, Term.class )
            .setParameter( "student", student )
            .getResultList();
    }

    @Override
    public List<Term> getTermsByEvaluator( User evaluator )
    {
        String query = "select distinct section.term from Section section "
            + "join section.rubricAssignments assignment "
            + "join assignment.externalEvaluators evaluator "
            + "where evaluator = :evaluator order by section.term desc";

        return entityManager.createQuery( query, Term.class )
            .setParameter( "evaluator", evaluator )
            .getResultList();
    }

    @Override
    public List<Term> getSectionTerms( Department department )
    {
        String query = "select distinct s.term from Section s "
            + "where course.department = :department "
            + "and s.instructors is not empty order by s.term desc";

        return entityManager.createQuery( query, Term.class )
            .setParameter( "department", department )
            .getResultList();
    }

}
