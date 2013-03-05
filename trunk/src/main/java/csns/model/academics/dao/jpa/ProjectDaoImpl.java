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
package csns.model.academics.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Department;
import csns.model.academics.Project;
import csns.model.academics.dao.ProjectDao;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Project getProject( Long id )
    {
        return entityManager.find( Project.class, id );
    }

    @Override
    public List<Project> getProjects( Department department, int year )
    {
        String query = "from Project where department = :department "
            + "and year = :year and deleted = false order by title asc";

        return entityManager.createQuery( query, Project.class )
            .setParameter( "department", department )
            .setParameter( "year", year )
            .getResultList();
    }

    @Override
    public List<Integer> getProjectYears( Department department )
    {
        String query = "select distinct year from Project "
            + "where department = :department order by year desc";

        return entityManager.createQuery( query, Integer.class )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    public List<Project> searchProjects( String term, int maxResults )
    {
        TypedQuery<Project> query = entityManager.createNamedQuery(
            "project.search", Project.class );
        if( maxResults > 0 ) query.setMaxResults( maxResults );
        return query.setParameter( "term", term ).getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("authenticated and (principal.faculty or #project.id != null and #project.isMember(principal))")
    public Project saveProject( Project project )
    {
        return entityManager.merge( project );
    }

}
