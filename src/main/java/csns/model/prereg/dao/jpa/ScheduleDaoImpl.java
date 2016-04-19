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

import csns.model.academics.Department;
import csns.model.prereg.Schedule;
import csns.model.prereg.dao.ScheduleDao;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Schedule getSchedule( Long id )
    {
        return entityManager.find( Schedule.class, id );
    }

    @Override
    public List<Schedule> getSchedules( Department department )
    {
        String query = "from Schedule where department = :department "
            + "and deleted = false order by term desc";

        return entityManager.createQuery( query, Schedule.class )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    @Transactional
    public Schedule saveSchedule( Schedule schedule )
    {
        return entityManager.merge( schedule );
    }

}
