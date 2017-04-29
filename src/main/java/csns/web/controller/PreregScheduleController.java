/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016-2017, Chengyu Sun (csun@calstatela.edu).
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.prereg.Schedule;
import csns.model.prereg.Section;
import csns.model.prereg.dao.ScheduleRegistrationDao;
import csns.model.prereg.dao.ScheduleDao;
import csns.security.SecurityUtils;
import csns.util.ExcelReader;

@Controller
public class PreregScheduleController {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ScheduleRegistrationDao registrationDao;

    @Autowired
    private DepartmentDao departmentDao;

    private static final Logger logger = LoggerFactory
        .getLogger( PreregScheduleController.class );

    @RequestMapping("/department/{dept}/prereg/schedule/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        List<Schedule> schedules = scheduleDao.getSchedules( department );

        models.put( "department", department );
        models.put( "schedules", schedules );

        return "prereg/schedule/list";
    }

    @RequestMapping("/department/{dept}/prereg/schedule/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        Schedule schedule = scheduleDao.getSchedule( id );
        models.put( "schedule", schedule );
        models.put( "registrations",
            registrationDao.getScheduleRegistrations( schedule ) );
        return "prereg/schedule/view";
    }

    @RequestMapping("/department/{dept}/prereg/schedule/publish")
    @ResponseBody
    public String publish( @RequestParam Long id )
    {
        Schedule schedule = scheduleDao.getSchedule( id );
        if( !schedule.isPreregStarted() )
        {
            schedule.setPreregStart( new Date() );
            schedule = scheduleDao.saveSchedule( schedule );
            logger.info( SecurityUtils.getUser().getUsername()
                + " published schedule " + id );
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        return dateFormat.format( schedule.getPreregStart() );
    }

    @RequestMapping("/department/{dept}/prereg/schedule/close")
    @ResponseBody
    public String close( @RequestParam Long id ) throws IOException
    {
        Schedule schedule = scheduleDao.getSchedule( id );
        if( !schedule.isPreregEnded() )
        {
            schedule.setPreregEnd( new Date() );
            schedule = scheduleDao.saveSchedule( schedule );
            logger.info( SecurityUtils.getUser().getUsername()
                + " closed schedule " + id );
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        return dateFormat.format( schedule.getPreregEnd() );
    }

    @RequestMapping("/department/{dept}/prereg/schedule/remove")
    public String remove( @RequestParam Long id )
    {
        Schedule schedule = scheduleDao.getSchedule( id );
        schedule.setDeleted( true );
        schedule = scheduleDao.saveSchedule( schedule );

        logger.info(
            SecurityUtils.getUser().getUsername() + " removed schedule " + id );

        return "redirect:list";
    }

    @RequestMapping(value = "/department/{dept}/prereg/schedule/import",
        method = RequestMethod.GET)
    public String importSections( @RequestParam Long scheduleId,
        ModelMap models )
    {
        models.put( "schedule", scheduleDao.getSchedule( scheduleId ) );
        return "prereg/schedule/import";
    }

    @RequestMapping(value = "/department/{dept}/prereg/schedule/import",
        method = RequestMethod.POST)
    public String importSections( @PathVariable String dept,
        @RequestParam Long scheduleId,
        @RequestParam(value = "file") MultipartFile uploadedFile )
        throws IOException
    {
        if( uploadedFile == null || uploadedFile.isEmpty() )
            return "redirect:import?scheduleId=" + scheduleId;

        Department department = departmentDao.getDepartment( dept );
        Set<Course> courses = new HashSet<Course>();
        courses.addAll( department.getUndergraduateCourses() );
        courses.addAll( department.getGraduateCourses() );

        Schedule schedule = scheduleDao.getSchedule( scheduleId );
        Set<String> classNumbers = new HashSet<String>();
        for( Section section : schedule.getSections() )
            classNumbers.add( section.getClassNumber() );

        ExcelReader excelReader = new ExcelReader(
            uploadedFile.getInputStream() );

        List<Section> sections = new ArrayList<Section>();
        while( excelReader.next() )
        {
            String classNumber = excelReader.get( "Class Nbr" );
            if( classNumbers.contains( classNumber ) )
            {
                logger.debug( classNumber + " is already in the schedule." );
                continue;
            }

            String code = excelReader.get( "Subj" ) + excelReader.get( "Cat" );
            Course course = courseDao.getCourse( code );
            if( course == null )
            {
                logger.debug( code + " is not in the system." );
                continue;
            }

            if( !courses.contains( course ) )
            {
                logger.debug( code + " is not offered by the department." );
                continue;
            }

            Section section = new Section( schedule );
            section.setCourse( course );
            section.setSectionNumber(
                Integer.parseInt( excelReader.get( "Sect" ) ) );
            section.setType( excelReader.get( "Type" ) );
            section.setClassNumber( classNumber );
            section.setDays( excelReader.get( "Day" ) );
            section.setStartTime( excelReader.get( "Start" ) );
            section.setEndTime( excelReader.get( "End" ) );
            section.setLocation( excelReader.get( "Bldg/Room" ) );
            sections.add( section );
        }
        excelReader.close();

        Section previous = null;
        for( Section section : sections )
        {
            switch( section.getType() )
            {
                case "LEC":
                    previous = section;
                    continue;

                case "SUP":
                    previous = null;
                    continue;

                case "LAB":
                case "REC":
                    if( previous == null || previous != null
                        && !previous.getCourse().equals( section.getCourse() ) )
                    {
                        previous = null;
                        continue;
                    }
            }

            if( previous.getType().equals( "LEC" ) )
            {
                previous.setLinkedBy( section );
                section.getLinkedTo().add( previous );
            }
            else if( previous.getLinkedBy() == null )
            {
                Section lecture = previous.getLinkedTo().get( 0 );
                lecture.setLinkedBy( null );
                lecture.getLinkedTo().add( previous );
                previous.setLinkedBy( lecture );
                previous.getLinkedTo().clear();
                section.setLinkedBy( lecture );
            }
            else
            {
                section.setLinkedBy( previous.getLinkedBy() );
            }
            previous = section;
        }

        schedule.getSections().addAll( sections );
        schedule = scheduleDao.saveSchedule( schedule );

        logger.info( SecurityUtils.getUser().getUsername() + " imported "
            + sections.size() + " sections into schedule " + schedule.getId() );

        return "redirect:view?id=" + scheduleId;
    }

}
