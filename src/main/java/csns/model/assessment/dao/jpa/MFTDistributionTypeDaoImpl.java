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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import csns.model.academics.Department;
import csns.model.assessment.MFTDistributionType;
import csns.model.assessment.dao.MFTDistributionTypeDao;

@Repository
public class MFTDistributionTypeDaoImpl implements MFTDistributionTypeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MFTDistributionType getDistributionType( Long id )
    {
        return entityManager.find( MFTDistributionType.class, id );
    }

    @Override
    public MFTDistributionType getDistributionType( Department department,
        String alias )
    {
        String query = "from MFTDistributionType where department = :department "
            + "and alias = :alias";

        List<MFTDistributionType> distributionTypes = entityManager.createQuery(
            query, MFTDistributionType.class )
            .setParameter( "department", department )
            .setParameter( "alias", alias )
            .getResultList();
        return distributionTypes.size() == 0 ? null : distributionTypes.get( 0 );
    }

    @Override
    public List<MFTDistributionType> getDistributionTypes( Department department )
    {
        String query = "from MFTDistributionType where department = :department "
            + "order by id asc";

        return entityManager.createQuery( query, MFTDistributionType.class )
            .setParameter( "department", department )
            .getResultList();
    }

}
