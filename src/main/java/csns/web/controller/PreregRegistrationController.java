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
package csns.web.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.model.prereg.ScheduleRegistration;
import csns.model.prereg.Registration;
import csns.model.prereg.Schedule;
import csns.model.prereg.Section;
import csns.model.prereg.SectionRegistration;
import csns.model.prereg.dao.ScheduleRegistrationDao;
import csns.model.prereg.dao.ScheduleDao;
import csns.model.prereg.dao.SectionDao;
import csns.model.prereg.dao.SectionRegistrationDao;
import csns.security.SecurityUtils;

@Controller
public class PreregRegistrationController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private ScheduleRegistrationDao scheduleRegistrationDao;

    @Autowired
    private SectionRegistrationDao sectionRegistrationDao;

    @Resource(name = "contentTypes")
    private Properties contentTypes;

    private static final Logger logger = LoggerFactory
        .getLogger( PreregRegistrationController.class );

    @RequestMapping("/department/{dept}/prereg/register")
    public String register( @RequestParam Long scheduleId, ModelMap models )
    {
        Schedule schedule = scheduleDao.getSchedule( scheduleId );
        if( !schedule.isPreregStarted() )
        {
            models.put( "message", "error.prereg.not.started" );
            return "error";
        }
        if( schedule.isPreregEnded() )
        {
            models.put( "message", "error.prereg.ended" );
            return "error";
        }

        User user = SecurityUtils.getUser();
        ScheduleRegistration scheduleRegistration = scheduleRegistrationDao
            .getScheduleRegistration( user, schedule );
        if( scheduleRegistration == null )
        {
            user = userDao.getUser( user.getId() );
            scheduleRegistration = scheduleRegistrationDao
                .saveScheduleRegistration(
                    new ScheduleRegistration( user, schedule ) );
            logger.info( "Schedule Registration " + scheduleRegistration.getId()
                + " created by (self) " + user.getUsername() );
        }

        JSONArray jsonArray = new JSONArray();
        for( SectionRegistration sectionRegistration : scheduleRegistration
            .getSectionRegistrations() )
            jsonArray.put( sectionRegistration.getSection().getId() );

        models.put( "registration", scheduleRegistration );
        models.put( "selectedClasses", jsonArray.toString() );
        return "prereg/register";
    }

    @RequestMapping("/department/{dept}/prereg/registration/addClass")
    public ResponseEntity<String> addClass( @RequestParam Long registrationId,
        @RequestParam Long sectionId )
    {
        User user = SecurityUtils.getUser();
        ScheduleRegistration scheduleRegistration = scheduleRegistrationDao
            .getScheduleRegistration( registrationId );
        if( scheduleRegistration.getSchedule().isPreregOpen() )
        {
            Section section = sectionDao.getSection( sectionId );
            SectionRegistration sectionRegistration = sectionRegistrationDao
                .getSectionRegistration( user, section );
            if( sectionRegistration == null )
            {
                sectionRegistration = new SectionRegistration(
                    scheduleRegistration, user, section );
                sectionRegistration = sectionRegistrationDao
                    .saveSectionRegistration( sectionRegistration );
                logger
                    .info( user.getUsername() + " created section registration "
                        + sectionRegistration.getId() );
            }
        }
        return new ResponseEntity<String>( HttpStatus.OK );
    }

    @RequestMapping("/department/{dept}/prereg/registration/removeClass")
    public ResponseEntity<String> removeClass(
        @RequestParam Long registrationId, @RequestParam Long sectionId )
    {
        User user = SecurityUtils.getUser();
        ScheduleRegistration scheduleRegistration = scheduleRegistrationDao
            .getScheduleRegistration( registrationId );
        if( scheduleRegistration.getSchedule().isPreregOpen() )
        {
            Section section = sectionDao.getSection( sectionId );
            SectionRegistration sectionRegistration = sectionRegistrationDao
                .getSectionRegistration( user, section );
            if( sectionRegistration != null )
            {
                sectionRegistrationDao
                    .deleteSectionRegistration( sectionRegistration );
                logger
                    .info( user.getUsername() + " deleted section registration "
                        + sectionRegistration.getId() );
            }
        }
        return new ResponseEntity<String>( HttpStatus.OK );
    }

    @RequestMapping(
        value = "/department/{dept}/prereg/registration/editComments")
    public String editComments( @RequestParam Long registrationId,
        @RequestParam String comments, HttpServletResponse response )
        throws IOException
    {
        User user = SecurityUtils.getUser();
        ScheduleRegistration scheduleRegistration = scheduleRegistrationDao
            .getScheduleRegistration( registrationId );
        if( scheduleRegistration.getSchedule().isPreregOpen() )
        {
            scheduleRegistration.setComments( comments );
            scheduleRegistration = scheduleRegistrationDao
                .saveScheduleRegistration( scheduleRegistration );
            logger.info( user.getUsername()
                + " edited comments of schedule registration "
                + registrationId );
        }

        response.setContentType( "text/plain" );
        response.getWriter().print( comments );
        return null;
    }

    @RequestMapping("/department/{dept}/prereg/registration/list")
    public String list( @RequestParam(required = false) Long scheduleId,
        @RequestParam(required = false) Long sectionId, ModelMap models )
    {
        if( sectionId != null )
        {
            Section section = sectionDao.getSection( sectionId );
            models.put( "section", section );
            models.put( "registrations", section.getRegistrations() );
        }
        else
        {
            Schedule schedule = scheduleDao.getSchedule( scheduleId );
            models.put( "schedule", schedule );
            models.put( "registrations",
                scheduleRegistrationDao.getScheduleRegistrations( schedule ) );
        }
        return "prereg/registration/list";
    }

    @RequestMapping("/department/{dept}/prereg/registration/delete")
    public String delete( @RequestParam("userId") Long userIds[],
        @RequestParam Long sectionId )
    {
        Section section = sectionDao.getSection( sectionId );
        for( Long userId : userIds )
        {
            User student = userDao.getUser( userId );
            SectionRegistration registration = sectionRegistrationDao
                .getSectionRegistration( student, section );
            sectionRegistrationDao.deleteSectionRegistration( registration );
            logger.info( SecurityUtils.getUser().getUsername()
                + " deleted section registration " + registration.getId() );
        }

        return "redirect:list?sectionId=" + sectionId;
    }

    @RequestMapping("/department/{dept}/prereg/registration/export")
    public String export( @RequestParam(required = false) Long scheduleId,
        @RequestParam(required = false) Long sectionId,
        HttpServletResponse response ) throws IOException
    {
        String fileName;
        List<Registration> registrations = new ArrayList<Registration>();
        if( sectionId != null )
        {
            Section section = sectionDao.getSection( sectionId );
            fileName = "Prereg " + section.getSchedule().getTerm() + " "
                + section.getCourse().getCode() + "-"
                + section.getSectionNumber() + ".xlsx";
            for( SectionRegistration registration : section.getRegistrations() )
                registrations.add( registration );
        }
        else
        {
            Schedule schedule = scheduleDao.getSchedule( scheduleId );
            fileName = "Prereg " + schedule.getTerm() + ".xlsx";
            for( ScheduleRegistration registration : scheduleRegistrationDao
                .getScheduleRegistrations( schedule ) )
                registrations.add( registration );
        }

        response.setContentType( contentTypes.getProperty( "xlsx" ) );
        response.setHeader( "Content-Disposition",
            "attachment; filename=\"" + fileName + "\"" );

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet( "Grades" );

        Row row = sheet.createRow( 0 );
        row.createCell( 0 ).setCellValue( "CIN" );
        row.createCell( 1 ).setCellValue( "Name" );
        row.createCell( 2 ).setCellValue( "Timestamp" );

        int rowIndex = 1;
        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        for( Registration registration : registrations )
        {
            row = sheet.createRow( rowIndex++ );
            row.createCell( 0 )
                .setCellValue( registration.getStudent().getCin() );
            row.createCell( 1 )
                .setCellValue( registration.getStudent().getLastName() + ", "
                    + registration.getStudent().getFirstName() );
            row.createCell( 2 )
                .setCellValue( dateFormat.format( registration.getDate() ) );
        }
        wb.write( response.getOutputStream() );
        wb.close();

        logger.info( SecurityUtils.getUser().getUsername() + " exported "
            + registrations.size() + " prereg registrations." );

        return null;
    }

}
