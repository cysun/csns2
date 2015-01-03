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
package csns.model.academics.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Department getDepartment( Long id )
    {
        return entityManager.find( Department.class, id );
    }

    @Override
    public Department getDepartment( String abbreviation )
    {
        List<Department> departments = entityManager.createQuery(
            "from Department where abbreviation = :abbreviation",
            Department.class )
            .setParameter( "abbreviation", abbreviation.toLowerCase() )
            .getResultList();
        return departments.size() == 0 ? null : departments.get( 0 );
    }

    @Override
    public Department getDepartmentByName( String name )
    {
        List<Department> departments = entityManager.createQuery(
            "from Department where name = :name", Department.class )
            .setParameter( "name", name )
            .getResultList();
        return departments.size() == 0 ? null : departments.get( 0 );
    }

    @Override
    public Department getDepartmentByFullName( String fullName )
    {
        List<Department> departments = entityManager.createQuery(
            "from Department where fullName = :fullName", Department.class )
            .setParameter( "fullName", fullName )
            .getResultList();
        return departments.size() == 0 ? null : departments.get( 0 );
    }

    @Override
    public List<Department> getDepartments()
    {
        return entityManager.createQuery( "from Department order by id asc",
            Department.class ).getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('DEPT_ROLE_ADMIN_' + #department.abbreviation)")
    public Department saveDepartment( Department department )
    {
        return entityManager.merge( department );
    }

}
