/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Mahdiye Jamali (mjamali@calstatela.edu).
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
import csns.model.prereg.Registration;
import csns.model.prereg.Schedule;
import csns.model.prereg.dao.RegistrationDao;

@Repository
public class RegistrationDaoImpl implements RegistrationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Registration getRegistration( Long id )
    {
        return entityManager.find( Registration.class, id );
    }

    @Override
    public Registration getRegistration( User student, Schedule schedule )
    {
        String query = "from Registration where student = :student "
            + "and schedule = :schedule";

        List<Registration> results = entityManager
            .createQuery( query, Registration.class )
            .setParameter( "student", student )
            .setParameter( "schedule", schedule )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    public List<Registration> getRegistrations( Schedule schedule )
    {
        String query = "from Registration r where r.schedule = :schedule "
            + "and r.sections is not empty order by r.date asc";

        return entityManager.createQuery( query, Registration.class )
            .setParameter( "schedule", schedule )
            .getResultList();
    }

    @Override
    @Transactional
    public Registration saveRegistration( Registration registration )
    {
        return entityManager.merge( registration );
    }

}
