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
package csns.model.forum.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.core.User;
import csns.model.forum.Forum;
import csns.model.forum.dao.ForumDao;

@Repository
public class ForumDaoImpl implements ForumDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Forum getForum( Long id )
    {
        return entityManager.find( Forum.class, id );
    }

    @Override
    public List<Forum> getSystemForums()
    {
        String query = "from Forum where department is null and course is null "
            + "and hidden = false";

        return entityManager.createQuery( query, Forum.class ).getResultList();
    }

    @Override
    public List<Forum> getSystemForums( User user )
    {
        return entityManager.createNamedQuery( "subscribed.system.forums",
            Forum.class )
            .setParameter( "userId", user.getId() )
            .getResultList();
    }

    @Override
    public List<Forum> getDepartmentForums( User user )
    {
        return entityManager.createNamedQuery( "subscribed.department.forums",
            Forum.class )
            .setParameter( "userId", user.getId() )
            .getResultList();
    }

    @Override
    public List<Forum> getCourseForums( User user )
    {
        return entityManager.createNamedQuery( "subscribed.course.forums",
            Forum.class )
            .setParameter( "userId", user.getId() )
            .getResultList();
    }

    @Override
    @Transactional
    public Forum saveForum( Forum forum )
    {
        return entityManager.merge( forum );
    }

}
