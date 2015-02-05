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

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Enrollment;
import csns.model.academics.Section;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.core.User;

@Repository
public class EnrollmentDaoImpl implements EnrollmentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @PostAuthorize("returnObject.section.isInstructor(principal) or returnObject.student.id == principal.id or principal.admin")
    public Enrollment getEnrollment( Long id )
    {
        return entityManager.find( Enrollment.class, id );
    }

    @Override
    public Enrollment getEnrollment( Section section, User student )
    {
        String query = "from Enrollment where section = :section "
            + "and student = :student";

        List<Enrollment> enrollments = entityManager.createQuery( query,
            Enrollment.class )
            .setParameter( "section", section )
            .setParameter( "student", student )
            .getResultList();
        return enrollments.size() == 0 ? null : enrollments.get( 0 );
    }

    @Override
    public List<Enrollment> getEnrollments( User student )
    {
        String query = "from Enrollment where student = :student "
            + "order by section.quarter desc";

        return entityManager.createQuery( query, Enrollment.class )
            .setParameter( "student", student )
            .getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("#enrollment.section.isInstructor(principal) or principal.admin")
    public Enrollment saveEnrollment( Enrollment enrollment )
    {
        return entityManager.merge( enrollment );
    }

    @Override
    @Transactional
    @PreAuthorize("#enrollment.section.isInstructor(principal) or principal.admin")
    public void deleteEnrollment( Enrollment enrollment )
    {
        entityManager.remove( enrollment );
    }

}
