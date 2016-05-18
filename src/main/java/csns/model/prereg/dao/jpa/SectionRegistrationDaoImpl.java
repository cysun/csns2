/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.prereg.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.core.User;
import csns.model.prereg.Section;
import csns.model.prereg.SectionRegistration;
import csns.model.prereg.dao.SectionRegistrationDao;

@Repository
public class SectionRegistrationDaoImpl implements SectionRegistrationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SectionRegistration getSectionRegistration( Long id )
    {
        return entityManager.find( SectionRegistration.class, id );
    }

    @Override
    public SectionRegistration getSectionRegistration( User student,
        Section section )
    {
        String query = "from SectionRegistration where student = :student "
            + "and section = :section";

        List<SectionRegistration> results = entityManager
            .createQuery( query, SectionRegistration.class )
            .setParameter( "student", student )
            .setParameter( "section", section )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    @Transactional
    public SectionRegistration saveSectionRegistration(
        SectionRegistration registration )
    {
        return entityManager.merge( registration );
    }

    @Override
    @Transactional
    public void deleteSectionRegistration( SectionRegistration registration )
    {
        entityManager.remove( registration );
    }

}
