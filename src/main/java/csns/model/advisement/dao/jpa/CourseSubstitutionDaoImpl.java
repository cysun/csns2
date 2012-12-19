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
package csns.model.advisement.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.advisement.CourseSubstitution;
import csns.model.advisement.dao.CourseSubstitutionDao;
import csns.model.core.User;

@Repository
public class CourseSubstitutionDaoImpl implements CourseSubstitutionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CourseSubstitution getCourseSubstitution( Long id )
    {
        return entityManager.find( CourseSubstitution.class, id );
    }

    @Override
    public List<CourseSubstitution> getCourseSubstitutions( User student )
    {
        String query = "from CourseSubstitution where advisementRecord.student = :student "
            + "order by original.code asc";

        return entityManager.createQuery( query, CourseSubstitution.class )
            .setParameter( "student", student )
            .getResultList();
    }

    @Override
    @Transactional
    public CourseSubstitution saveCourseSubstitution(
        CourseSubstitution courseSubstitution )
    {
        return entityManager.merge( courseSubstitution );
    }

    @Override
    @Transactional
    public void deleteCourseSubstitution( CourseSubstitution courseSubstitution )
    {
        entityManager.remove( courseSubstitution );
    }

}
