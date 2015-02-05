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

import csns.model.advisement.CourseWaiver;
import csns.model.advisement.dao.CourseWaiverDao;
import csns.model.core.User;

@Repository
public class CourseWaiverDaoImpl implements CourseWaiverDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CourseWaiver getCourseWaiver( Long id )
    {
        return entityManager.find( CourseWaiver.class, id );
    }

    @Override
    public List<CourseWaiver> getCourseWaivers( User student )
    {
        String query = "from CourseWaiver where advisementRecord.student = :student "
            + "order by course.code asc";

        return entityManager.createQuery( query, CourseWaiver.class )
            .setParameter( "student", student )
            .getResultList();
    }

    @Override
    @Transactional
    public CourseWaiver saveCourseWaiver( CourseWaiver courseWaiver )
    {
        return entityManager.merge( courseWaiver );
    }

    @Override
    @Transactional
    public void deleteCourseWaiver( CourseWaiver courseWaiver )
    {
        entityManager.remove( courseWaiver );
    }

}
