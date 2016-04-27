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
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

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
import csns.model.prereg.Registration;
import csns.model.prereg.Schedule;
import csns.model.prereg.Section;
import csns.model.prereg.dao.RegistrationDao;
import csns.model.prereg.dao.ScheduleDao;
import csns.model.prereg.dao.SectionDao;
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
    private RegistrationDao registrationDao;

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
        Registration registration = registrationDao.getRegistration( user,
            schedule );
        if( registration == null )
        {
            user = userDao.getUser( user.getId() );
            registration = registrationDao
                .saveRegistration( new Registration( user, schedule ) );
            logger.info( "Registration " + registration.getId()
                + " created by (self) " + user.getUsername() );
        }

        JSONArray jsonArray = new JSONArray();
        for( Section section : registration.getSections() )
            if( section.getLinkedTo().isEmpty() )
                jsonArray.put( section.getId() );

        models.put( "registration", registration );
        models.put( "selectedClasses", jsonArray.toString() );
        return "prereg/register";
    }

    @RequestMapping("/department/{dept}/prereg/registration/addClass")
    public ResponseEntity<String> addClass( @RequestParam Long registrationId,
        @RequestParam Long sectionId )
    {
        User user = SecurityUtils.getUser();
        Registration registration = registrationDao
            .getRegistration( registrationId );
        if( registration.getSchedule().isPreregOpen() || user.isFaculty() )
        {
            Section section = sectionDao.getSection( sectionId );
            registration.getSections().add( section );
            if( section.getLinkedBy() != null )
                registration.getSections().add( section.getLinkedBy() );
            registration.setDate( new Date() );
            registration = registrationDao.saveRegistration( registration );

            logger.info( user.getUsername() + " added section " + sectionId
                + " to registration " + registrationId );
        }
        return new ResponseEntity<String>( HttpStatus.OK );
    }

    @RequestMapping("/department/{dept}/prereg/registration/removeClass")
    public ResponseEntity<String> removeClass(
        @RequestParam Long registrationId, @RequestParam Long sectionId )
    {
        User user = SecurityUtils.getUser();
        Registration registration = registrationDao
            .getRegistration( registrationId );
        if( registration.getSchedule().isPreregOpen() || user.isFaculty() )
        {
            Section section = registration.removeSection( sectionId );
            if( section.getLinkedBy() != null )
                registration.removeSection( section.getLinkedBy().getId() );
            registration.setDate( new Date() );
            registration = registrationDao.saveRegistration( registration );

            logger.info( user.getUsername() + " removed section " + sectionId
                + " from registration " + registrationId );
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
        Registration registration = registrationDao
            .getRegistration( registrationId );
        if( registration.getSchedule().isPreregOpen() || user.isFaculty() )
        {
            registration.setComments( comments );
            registration = registrationDao.saveRegistration( registration );

            logger.info( user.getUsername()
                + " edited comments of registration " + registrationId );
        }

        response.setContentType( "text/plain" );
        response.getWriter().print( comments );
        return null;
    }

    @RequestMapping("/department/{dept}/prereg/registration/list")
    public String list( @RequestParam(required = false ) Long scheduleId,
        @RequestParam(required = false) Long sectionId, ModelMap models)
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
                registrationDao.getRegistrations( schedule ) );
        }
        return "prereg/registration/list";
    }

}
