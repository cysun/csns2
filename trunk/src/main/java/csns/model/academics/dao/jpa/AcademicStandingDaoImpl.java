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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.AcademicStanding;
import csns.model.academics.Department;
import csns.model.academics.Standing;
import csns.model.academics.dao.AcademicStandingDao;
import csns.model.core.User;

@Repository
public class AcademicStandingDaoImpl implements AcademicStandingDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AcademicStanding getAcademicStanding( Long id )
    {
        return entityManager.find( AcademicStanding.class, id );
    }

    @Override
    public AcademicStanding getAcademicStanding( User student,
        Department department, Standing standing )
    {
        String query = "from AcademicStanding where student = :student "
            + "and department = :department and standing = :standing";

        List<AcademicStanding> results = entityManager.createQuery( query,
            AcademicStanding.class )
            .setParameter( "student", student )
            .setParameter( "department", department )
            .setParameter( "standing", standing )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    public AcademicStanding getLatestAcademicStanding( User student,
        Department department )
    {
        String query = "from AcademicStanding where student = :student "
            + "and department = :department "
            + "order by quarter.code desc, standing.id desc";

        List<AcademicStanding> results = entityManager.createQuery( query,
            AcademicStanding.class )
            .setParameter( "student", student )
            .setParameter( "department", department )
            .setMaxResults( 1 )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    public List<AcademicStanding> getAcademicStandings( User student )
    {
        String query = "from AcademicStanding where student = :student "
            + "order by department.name asc, standing.id desc";

        return entityManager.createQuery( query, AcademicStanding.class )
            .setParameter( "student", student )
            .getResultList();
    }

    @Override
    @Transactional
    public AcademicStanding saveAcademicStanding(
        AcademicStanding academicStanding )
    {
        return entityManager.merge( academicStanding );
    }

    @Override
    @Transactional
    public void deleteAcademicStanding( AcademicStanding academicStanding )
    {
        entityManager.remove( academicStanding );
    }

}
