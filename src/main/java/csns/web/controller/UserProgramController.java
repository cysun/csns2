/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015-2017, Chengyu Sun (csun@calstatela.edu).
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import csns.helper.ProgramChecker;
import csns.model.academics.CourseMapping;
import csns.model.academics.Enrollment;
import csns.model.academics.Program;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.CourseMappingDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.ProgramDao;
import csns.model.advisement.AdvisementRecord;
import csns.model.advisement.PersonalProgram;
import csns.model.advisement.PersonalProgramBlock;
import csns.model.advisement.PersonalProgramEntry;
import csns.model.advisement.dao.AdvisementRecordDao;
import csns.model.advisement.dao.PersonalProgramBlockDao;
import csns.model.advisement.dao.PersonalProgramDao;
import csns.model.advisement.dao.PersonalProgramEntryDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;

@Controller
public class UserProgramController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private CourseMappingDao courseMappingDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private PersonalProgramDao personalProgramDao;

    @Autowired
    private PersonalProgramBlockDao personalProgramBlockDao;

    @Autowired
    private PersonalProgramEntryDao personalProgramEntryDao;

    @Autowired
    private AdvisementRecordDao advisementRecordDao;

    private static final Logger logger = LoggerFactory
        .getLogger( UserProgramController.class );

    @RequestMapping("/user/program")
    public String program( @RequestParam Long userId, ModelMap models )
    {
        User user = userDao.getUser( userId );
        if( user.getPersonalProgram() != null )
        {
            List<Enrollment> enrollments = enrollmentDao.getEnrollments( user );
            enrollments.removeAll( user.getPersonalProgram().getEnrollments() );
            ProgramChecker programChecker = new ProgramChecker(
                user.getPersonalProgram() );
            int entriesUpdated = programChecker
                .checkRequirements( enrollments );
            List<CourseMapping> courseMappings = courseMappingDao
                .getCourseMappings(
                    user.getPersonalProgram().getProgram().getDepartment() );
            if( courseMappings.size() > 0 ) entriesUpdated += programChecker
                .checkRequirements( courseMappings, enrollments );
            if( entriesUpdated > 0 )
            {
                personalProgramDao
                    .savePersonalProgram( user.getPersonalProgram() );
                logger.info( "Auto updated " + entriesUpdated
                    + " entries in the personal program of "
                    + user.getUsername() );
            }
            models.put( "enrollments", enrollments );
        }

        models.put( "user", user );
        models.put( "departments", departmentDao.getDepartments() );
        if( user.getMajor() != null ) models.put( "programs",
            programDao.getPublishedPrograms( user.getMajor() ) );

        return "user/program";
    }

    @RequestMapping("/user/major/set")
    public String setMajor( @RequestParam Long userId,
        @RequestParam(required = false) Long majorId )
    {
        User user = userDao.getUser( userId );
        if( majorId == null )
        {
            user.setMajor( null );
            user.setPersonalProgram( null );
            user = userDao.saveUser( user );
            logger.info( SecurityUtils.getUser().getUsername()
                + " removed major for " + user.getUsername() );
        }
        else
        {
            user.setMajor( departmentDao.getDepartment( majorId ) );
            if( user.getPersonalProgram() != null && !user.getPersonalProgram()
                .getProgram()
                .getDepartment()
                .getId()
                .equals( majorId ) ) user.setPersonalProgram( null );
            user = userDao.saveUser( user );
            logger.info( SecurityUtils.getUser().getUsername()
                + " set major to " + user.getMajor().getAbbreviation() + " for "
                + user.getUsername() );
        }
        // Program is the 4th tab
        return "redirect:../view?id=" + userId + "#3";
    }

    @RequestMapping("/user/program/set")
    public String setProgram( @RequestParam Long userId,
        @RequestParam(required = false) Long programId )
    {
        User user = userDao.getUser( userId );

        if( programId == null )
        {
            user.setPersonalProgram( null );
            user = userDao.saveUser( user );
            logger.info( SecurityUtils.getUser().getUsername()
                + " removed personal program for " + user.getUsername() );
        }
        else
        {
            Program program = programDao.getProgram( programId );
            PersonalProgram personalProgram = personalProgramDao
                .getPersonalProgram( user, program );
            if( personalProgram == null )
            {
                personalProgram = new PersonalProgram( program );
                personalProgram.setStudent( user );
                personalProgram.setDate( new Date() );
                personalProgram = personalProgramDao
                    .savePersonalProgram( personalProgram );
            }
            user.setPersonalProgram( personalProgram );
            user = userDao.saveUser( user );
            logger.info(
                SecurityUtils.getUser().getUsername() + " set personal program "
                    + personalProgram.getId() + " for " + user.getUsername() );
        }
        // Program is the 4th tab
        return "redirect:../view?id=" + userId + "#3";
    }

    @RequestMapping("/user/program/approve")
    public String approvePersonalProgram( @RequestParam Long programId )
    {
        PersonalProgram program = personalProgramDao
            .getPersonalProgram( programId );
        if( !program.isApproved() )
        {
            User advisor = SecurityUtils.getUser();
            program.setApproveDate( new Date() );
            program.setApprovedBy( advisor );
            program = personalProgramDao.savePersonalProgram( program );
            logger.info( "Personal program " + programId + " is approved by "
                + advisor.getUsername() );

            AdvisementRecord record = new AdvisementRecord(
                program.getStudent(), advisor );
            StringBuilder sb = (new StringBuilder())
                .append( "Study plan approved by " )
                .append( advisor.getName() )
                .append( " on " )
                .append( (new SimpleDateFormat( "M/d/yyyy" ))
                    .format( program.getApproveDate() ) )
                .append( "." );
            record.setComment( sb.toString() );
            record = advisementRecordDao.saveAdvisementRecord( record );
            logger.info( "Auto-generated advisement record " + record.getId() );
        }

        return "redirect:../view?id=" + program.getStudent().getId() + "#3";
    }

    private void unapprovePersonalProgram( PersonalProgram program )
    {
        if( program.isApproved() )
        {
            program.setApproveDate( null );
            program.setApprovedBy( null );
            program = personalProgramDao.savePersonalProgram( program );
            logger.info( "Perosnal program " + program.getId()
                + " un-approved by " + SecurityUtils.getUser().getUsername() );
        }
    }

    @RequestMapping("/user/program/entry/add")
    public String addPersonalProgramEntry( @RequestParam Long userId,
        @RequestParam Long blockId,
        @RequestParam(required = false) Long courseId,
        @RequestParam(required = false) Long enrollmentId )
    {
        PersonalProgramEntry entry = null;
        if( courseId != null )
            entry = new PersonalProgramEntry( courseDao.getCourse( courseId ) );
        if( enrollmentId != null ) entry = new PersonalProgramEntry(
            enrollmentDao.getEnrollment( enrollmentId ) );
        if( entry != null )
        {
            PersonalProgramBlock block = personalProgramBlockDao
                .getPersonalProgramBlock( blockId );
            block.getEntries().add( entry );
            personalProgramBlockDao.savePersonalProgramBlock( block );
            logger.info( SecurityUtils.getUser().getUsername()
                + " add a new entry to personal program block " + blockId );
            unapprovePersonalProgram(
                personalProgramDao.getPersonalProgram( block ) );
        }

        return "redirect:../../view?id=" + userId + "#3";
    }

    @RequestMapping("/user/program/entry/delete")
    public String deletePersonalProgramEntry( @RequestParam Long userId,
        @RequestParam Long entryId )
    {
        PersonalProgramEntry entry = personalProgramEntryDao
            .getPersonalProgramEntry( entryId );
        PersonalProgram program = personalProgramDao
            .getPersonalProgram( entry );
        personalProgramEntryDao.deletePersonalProgramEntry( entry );
        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted personal program entry " + entryId );
        unapprovePersonalProgram( program );
        return "redirect:../../view?id=" + userId + "#3";
    }

    @RequestMapping(value = "/user/program/entry/update",
        params = "enrollmentId")
    @ResponseBody
    public void updatePersonalProgramEntry( @RequestParam Long entryId,
        @RequestParam Long enrollmentId )
    {
        PersonalProgramEntry entry = personalProgramEntryDao
            .getPersonalProgramEntry( entryId );
        entry.setEnrollment( enrollmentDao.getEnrollment( enrollmentId ) );
        personalProgramEntryDao.savePersonalProgramEntry( entry );
        logger.info( SecurityUtils.getUser().getUsername() + " set enrollment "
            + enrollmentId + " to personal program entry " + entryId );
    }

    @RequestMapping("/user/program/entry/update")
    @ResponseBody
    public void updatePersonalProgramEntry( @RequestParam Long entryId,
        @RequestParam boolean prereqMet, @RequestParam boolean requirementMet )
    {
        PersonalProgramEntry entry = personalProgramEntryDao
            .getPersonalProgramEntry( entryId );
        entry.setPrereqMet( prereqMet );
        entry.setRequirementMet( requirementMet );
        personalProgramEntryDao.savePersonalProgramEntry( entry );
        logger.info(
            SecurityUtils.getUser().getUsername() + " updated program entry "
                + entryId + ": (" + prereqMet + "," + requirementMet + ")" );
    }

}
