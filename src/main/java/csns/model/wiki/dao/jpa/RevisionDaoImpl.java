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
package csns.model.wiki.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.wiki.Page;
import csns.model.wiki.Revision;
import csns.model.wiki.dao.RevisionDao;

@Repository
public class RevisionDaoImpl implements RevisionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Revision getRevision( Long id )
    {
        return entityManager.find( Revision.class, id );
    }

    @Override
    public Revision getRevision( String path )
    {
        String query = "from Revision where page.path = :path order by date desc";

        List<Revision> results = entityManager.createQuery( query,
            Revision.class )
            .setParameter( "path", path )
            .setMaxResults( 1 )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    public List<Revision> getRevisions( Page page )
    {
        String query = "from Revision where page = :page order by date desc";

        return entityManager.createQuery( query, Revision.class )
            .setParameter( "page", page )
            .getResultList();
    }

    @Override
    @Transactional
    public Revision saveRevision( Revision revision )
    {
        return entityManager.merge( revision );
    }

}
