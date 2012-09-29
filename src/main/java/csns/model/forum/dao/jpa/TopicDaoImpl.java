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
import csns.model.forum.Topic;
import csns.model.forum.dao.TopicDao;

@Repository
public class TopicDaoImpl implements TopicDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Topic getTopic( Long id )
    {
        return entityManager.find( Topic.class, id );
    }

    @Override
    public List<Topic> getTopics( Forum forum )
    {
        String query = "from Topic where forum = :forum and deleted = false "
            + "order by pinned desc, lastPost.date desc";

        return entityManager.createQuery( query, Topic.class )
            .setParameter( "forum", forum )
            .getResultList();
    }

    @Override
    @Transactional
    public Topic saveTopic( Topic topic )
    {
        return entityManager.merge( topic );
    }

}
