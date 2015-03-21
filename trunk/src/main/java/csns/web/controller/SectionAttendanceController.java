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
package csns.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import csns.model.academics.AttendanceEvent;
import csns.model.academics.AttendanceRecord;
import csns.model.academics.Section;
import csns.model.academics.dao.AttendanceEventDao;
import csns.model.academics.dao.AttendanceRecordDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;

@Controller
public class SectionAttendanceController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private AttendanceEventDao attendanceEventDao;

    @Autowired
    private AttendanceRecordDao attendanceRecordDao;

    private static final Logger logger = LoggerFactory.getLogger( SectionAttendanceController.class );

    @RequestMapping("/section/attendance")
    public String attendance( @RequestParam Long sectionId, ModelMap models )
    {
        models.put( "section", sectionDao.getSection( sectionId ) );
        return "section/attendance";
    }

    @RequestMapping("/section/attendance/toggle")
    @ResponseStatus(HttpStatus.OK)
    public void toggleAttendance( @RequestParam Long eventId,
        @RequestParam Long userId )
    {
        User user = userDao.getUser( userId );
        AttendanceEvent event = attendanceEventDao.getAttendanceEvent( eventId );
        AttendanceRecord record = attendanceRecordDao.getAttendanceRecord(
            event, user );

        if( record == null ) record = new AttendanceRecord( event, user );

        Boolean attended = record.getAttended();
        if( attended == null )
            record.setAttended( true );
        else if( attended )
            record.setAttended( false );
        else
            record.setAttended( null );

        record = attendanceRecordDao.saveAttendanceRecord( record );
        logger.info( SecurityUtils.getUser().getUsername()
            + " updated attendance record " + record.getId() );
    }

    @RequestMapping("/section/attendance/addEvent")
    public String addEvent( @RequestParam Long sectionId,
        @RequestParam String name )
    {
        if( StringUtils.hasText( name ) )
        {
            Section section = sectionDao.getSection( sectionId );
            section.getAttendanceEvents().add( new AttendanceEvent( name ) );
            section = sectionDao.saveSection( section );

            logger.info( SecurityUtils.getUser().getUsername()
                + " added attendance event [" + name + "] to section "
                + sectionId );
        }

        return "redirect:/section/attendance?sectionId=" + sectionId;
    }

    @RequestMapping("/section/attendance/editEvent")
    public String editEvent( @RequestParam Long sectionId,
        @RequestParam Long eventId, @RequestParam String name )
    {
        if( StringUtils.hasText( name ) )
        {
            AttendanceEvent event = attendanceEventDao.getAttendanceEvent( eventId );
            event.setName( name );
            attendanceEventDao.saveAttendanceEvent( event );

            logger.info( SecurityUtils.getUser().getUsername()
                + " updated attendance event " + eventId );
        }

        return "redirect:/section/attendance?sectionId=" + sectionId;
    }

    @RequestMapping("/section/attendance/removeEvent")
    public String removeEvent( @RequestParam Long sectionId,
        @RequestParam Long eventId )
    {
        Section section = sectionDao.getSection( sectionId );
        boolean removed = section.removeAttendanceEvent( eventId );
        if( removed )
        {
            section = sectionDao.saveSection( section );
            logger.info( SecurityUtils.getUser().getUsername()
                + " removed attendance event " + eventId + " from section "
                + sectionId );
        }

        return "redirect:/section/attendance?sectionId=" + sectionId;
    }

}
