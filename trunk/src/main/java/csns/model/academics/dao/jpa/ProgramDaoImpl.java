/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015, Chengyu Sun (csun@calstatela.edu).
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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Department;
import csns.model.academics.Program;
import csns.model.academics.dao.ProgramDao;

@Repository
public class ProgramDaoImpl implements ProgramDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Program getProgram( Long id )
    {
        return entityManager.find( Program.class, id );
    }

    @Override
    public List<Program> getPrograms( Department department )
    {
        String query = "from Program where department = :department "
            + "order by name asc";

        return entityManager.createQuery( query, Program.class )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    public List<Program> searchPrograms( String term, int maxResults )
    {
        TypedQuery<Program> query = entityManager.createNamedQuery(
            "program.search", Program.class );
        if( maxResults > 0 ) query.setMaxResults( maxResults );
        return query.setParameter( "term", term ).getResultList();
    }

    @Override
    @Transactional
    public Program saveProgram( Program program )
    {
        return entityManager.merge( program );
    }

}
