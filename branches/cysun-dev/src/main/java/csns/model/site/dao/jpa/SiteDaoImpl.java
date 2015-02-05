/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.site.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Course;
import csns.model.core.User;
import csns.model.site.Site;
import csns.model.site.dao.SiteDao;

@Repository
public class SiteDaoImpl implements SiteDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Site getSite( Long id )
    {
        return entityManager.find( Site.class, id );
    }

    @Override
    public List<Site> getSites( Course course, User instructor, int maxResults )
    {
        String query = "select s from Site s join s.section.instructors i "
            + "where s.section.course = :course and (s.shared = true or i = :instructor) "
            + "order by s.section.quarter desc, s.section.number asc";

        TypedQuery<Site> typedQuery = entityManager.createQuery( query,
            Site.class )
            .setParameter( "course", course )
            .setParameter( "instructor", instructor );
        if( maxResults > 0 ) typedQuery.setMaxResults( maxResults );

        return typedQuery.getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("authenticated and (principal.admin or #site.section.isInstructor(principal))")
    public Site saveSite( Site site )
    {
        return entityManager.merge( site );
    }

}
