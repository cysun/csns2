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

import csns.model.forum.Forum;
import csns.model.forum.Post;
import csns.model.forum.dao.PostDao;

@Repository
public class PostDaoImpl implements PostDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Post getPost( Long id )
    {
        return entityManager.find( Post.class, id );
    }

    @Override
    public List<Post> searchPosts( Forum forum, String term, int maxResults )
    {
        return entityManager.createNamedQuery( "forum.post.search", Post.class )
            .setParameter( "forumId", forum.getId() )
            .setParameter( "term", term )
            .setMaxResults( maxResults )
            .getResultList();
    }

    @Override
    @Transactional
    public Post savePost( Post post )
    {
        return entityManager.merge( post );
    }

}
