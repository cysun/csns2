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
package csns.model.forum.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.forum.Forum;
import csns.model.forum.dao.ForumDao;

@Repository
public class ForumDaoImpl implements ForumDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @PostAuthorize("not returnObject.membersOnly or authenticated and returnObject.isMember(principal)")
    public Forum getForum( Long id )
    {
        return entityManager.find( Forum.class, id );
    }

    @Override
    public Forum getForum( String name )
    {
        return entityManager.createQuery( "from Forum where name = :name",
            Forum.class )
            .setParameter( "name", name )
            .getSingleResult();
    }

    @Override
    public Forum getForum( Course course )
    {
        return entityManager.createQuery( "from Forum where course = :course",
            Forum.class )
            .setParameter( "course", course )
            .getSingleResult();
    }

    @Override
    public List<Forum> getSystemForums()
    {
        String query = "from Forum where department is null and course is null "
            + "and hidden = false";

        return entityManager.createQuery( query, Forum.class ).getResultList();
    }

    @Override
    public List<Forum> getCourseForums( Department department )
    {
        String query = "from Forum where course.department = :department "
            + "and course.obsolete = false order by course.code asc";

        return entityManager.createQuery( query, Forum.class )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    public List<Forum> searchForums( String term, int maxResults )
    {
        TypedQuery<Forum> query = entityManager.createNamedQuery(
            "forum.search", Forum.class );
        if( maxResults > 0 ) query.setMaxResults( maxResults );
        return query.setParameter( "term", term ).getResultList();
    }

    @Override
    @Transactional
    public Forum saveForum( Forum forum )
    {
        return entityManager.merge( forum );
    }

}
