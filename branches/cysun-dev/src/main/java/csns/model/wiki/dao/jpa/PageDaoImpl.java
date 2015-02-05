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

import csns.helper.WikiSearchResult;
import csns.model.wiki.Page;
import csns.model.wiki.dao.PageDao;

@Repository
public class PageDaoImpl implements PageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page getPage( Long id )
    {
        return entityManager.find( Page.class, id );
    }

    @Override
    public Page getPage( String path )
    {
        List<Page> results = entityManager.createQuery(
            "from Page where path = :path", Page.class )
            .setParameter( "path", path )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    public List<WikiSearchResult> searchPages( String term, int maxResults )
    {
        return entityManager.createNamedQuery( "wiki.page.search",
            WikiSearchResult.class )
            .setParameter( "term", term )
            .setMaxResults( maxResults )
            .getResultList();
    }

    @Override
    @Transactional
    public Page savePage( Page page )
    {
        return entityManager.merge( page );
    }

}
