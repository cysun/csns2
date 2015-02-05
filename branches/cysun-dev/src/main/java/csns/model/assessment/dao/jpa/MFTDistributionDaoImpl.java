/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.assessment.dao.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Department;
import csns.model.assessment.MFTDistribution;
import csns.model.assessment.MFTDistributionType;
import csns.model.assessment.dao.MFTDistributionDao;

@Repository
public class MFTDistributionDaoImpl implements MFTDistributionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Integer> getYears( Department department )
    {
        String query = "select distinct year from MFTDistribution "
            + "where type.department = :department and deleted = false "
            + "order by year desc";

        return entityManager.createQuery( query, Integer.class )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    public MFTDistribution getDistribution( Long id )
    {
        return entityManager.find( MFTDistribution.class, id );
    }

    @Override
    public MFTDistribution getDistribution( Integer year,
        MFTDistributionType type )
    {
        String query = "from MFTDistribution where year = :year and type = :type";

        List<MFTDistribution> distributions = entityManager.createQuery( query,
            MFTDistribution.class )
            .setParameter( "year", year )
            .setParameter( "type", type )
            .getResultList();
        return distributions.size() == 0 ? null : distributions.get( 0 );
    }

    @Override
    public MFTDistribution getDistribution( Date date, MFTDistributionType type )
    {
        List<Integer> years = getYears( type.getDepartment() );
        if( years.size() == 0 ) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        int targetYear = calendar.get( Calendar.YEAR );
        int distYear = years.get( 0 );
        for( Integer year : years )
            if( Math.abs( year - targetYear ) < Math.abs( distYear - targetYear ) )
                distYear = year;

        return getDistribution( distYear, type );
    }

    @Override
    public List<MFTDistribution> getDistributions( Integer year,
        Department department )
    {
        String query = "from MFTDistribution where year = :year "
            + "and type.department = :department and deleted = false "
            + "order by type.id asc";

        return entityManager.createQuery( query, MFTDistribution.class )
            .setParameter( "department", department )
            .setParameter( "year", year )
            .getResultList();
    }

    @Override
    @Transactional
    public MFTDistribution saveDistribution( MFTDistribution distribution )
    {
        return entityManager.merge( distribution );
    }

}
