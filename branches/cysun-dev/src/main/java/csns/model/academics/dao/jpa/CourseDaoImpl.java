/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
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
import javax.persistence.TypedQuery;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Course;
import csns.model.academics.dao.CourseDao;

@Repository
public class CourseDaoImpl implements CourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Course getCourse( Long id )
    {
        return entityManager.find( Course.class, id );
    }

    @Override
    public Course getCourse( String code )
    {
        List<Course> courses = entityManager.createQuery(
            "from Course where code = :code", Course.class )
            .setParameter( "code", code.toUpperCase() )
            .getResultList();
        return courses.size() == 0 ? null : courses.get( 0 );
    }

    @Override
    public List<Course> searchCourses( String term, int maxResults )
    {
        TypedQuery<Course> query = entityManager.createNamedQuery(
            "course.search", Course.class );
        if( maxResults > 0 ) query.setMaxResults( maxResults );
        return query.setParameter( "term", term ).getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("principal.admin or principal.id == #course.coordinator.id")
    public Course saveCourse( Course course )
    {
        return entityManager.merge( course );
    }

}
