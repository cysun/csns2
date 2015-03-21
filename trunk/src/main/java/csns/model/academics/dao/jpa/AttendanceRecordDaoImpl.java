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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.AttendanceEvent;
import csns.model.academics.AttendanceRecord;
import csns.model.academics.dao.AttendanceRecordDao;
import csns.model.core.User;

@Repository
public class AttendanceRecordDaoImpl implements AttendanceRecordDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AttendanceRecord getAttendanceRecord( Long id )
    {
        return entityManager.find( AttendanceRecord.class, id );
    }

    @Override
    public AttendanceRecord getAttendanceRecord( AttendanceEvent event,
        User user )
    {
        String query = "from AttendanceRecord where event = :event and user = :user";

        List<AttendanceRecord> results = entityManager.createQuery( query,
            AttendanceRecord.class )
            .setParameter( "event", event )
            .setParameter( "user", user )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    @Transactional
    public AttendanceRecord saveAttendanceRecord( AttendanceRecord record )
    {
        return entityManager.merge( record );
    }

}
