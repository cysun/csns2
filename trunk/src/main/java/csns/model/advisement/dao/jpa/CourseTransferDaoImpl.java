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

import csns.model.advisement.CourseTransfer;
import csns.model.advisement.dao.CourseTransferDao;
import csns.model.core.User;

@Repository
public class CourseTransferDaoImpl implements CourseTransferDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CourseTransfer getCourseTransfer( Long id )
    {
        return entityManager.find( CourseTransfer.class, id );
    }

    @Override
    public List<CourseTransfer> getCourseTransfers( User student )
    {
        String query = "from CourseTransfer where advisementRecord.student = :student "
            + "order by course.code asc";

        return entityManager.createQuery( query, CourseTransfer.class )
            .setParameter( "student", student )
            .getResultList();
    }

    @Override
    @Transactional
    public CourseTransfer saveCourseTransfer( CourseTransfer courseTransfer )
    {
        return entityManager.merge( courseTransfer );
    }

    @Override
    @Transactional
    public void deleteCourseTransfer( CourseTransfer courseTransfer )
    {
        entityManager.remove( courseTransfer );
    }

}
