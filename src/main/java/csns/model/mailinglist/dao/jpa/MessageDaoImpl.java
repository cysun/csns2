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
package csns.model.mailinglist.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.mailinglist.Mailinglist;
import csns.model.mailinglist.Message;
import csns.model.mailinglist.dao.MessageDao;

@Repository
public class MessageDaoImpl implements MessageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Message getMessage( Long id )
    {
        return entityManager.find( Message.class, id );
    }

    @Override
    public List<Message> getMessagess( Mailinglist mailinglist, int maxResults )
    {
        String query = "from Message where mailinglist = :mailinglist "
            + "order by date desc";

        return entityManager.createQuery( query, Message.class )
            .setParameter( "mailinglist", mailinglist )
            .setMaxResults( maxResults )
            .getResultList();
    }

    @Override
    public List<Message> searchMessages( Mailinglist mailinglist, String term,
        int maxResults )
    {
        return entityManager.createNamedQuery( "mailinglist.message.search",
            Message.class )
            .setParameter( "mailinglistId", mailinglist.getId() )
            .setParameter( "term", term )
            .setMaxResults( maxResults )
            .getResultList();
    }

    @Override
    @Transactional
    public Message saveMessage( Message message )
    {
        return entityManager.merge( message );
    }

}
