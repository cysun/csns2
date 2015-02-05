/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015, Chengyu Sun (csun@calstatela.edu).
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

import csns.model.academics.CourseMapping;
import csns.model.academics.Department;
import csns.model.academics.dao.CourseMappingDao;

@Repository
public class CourseMappingDaoImpl implements CourseMappingDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CourseMapping getCourseMapping( Long id )
    {
        return entityManager.find( CourseMapping.class, id );
    }

    @Override
    public List<CourseMapping> getCourseMappings( Department department )
    {
        String query = "from CourseMapping where department = :department "
            + "and deleted = false order by id asc";

        return entityManager.createQuery( query, CourseMapping.class )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    @Transactional
    public CourseMapping saveCourseMapping( CourseMapping courseMapping )
    {
        return entityManager.merge( courseMapping );
    }

}
