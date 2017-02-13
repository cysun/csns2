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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Program;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.ProgramDao;
import csns.model.advisement.PersonalProgram;
import csns.model.advisement.dao.PersonalProgramDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;

@Controller
public class UserProgramController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private PersonalProgramDao personalProgramDao;

    @Autowired
    private DepartmentDao departmentDao;

    private static final Logger logger = LoggerFactory
        .getLogger( UserProgramController.class );

    @RequestMapping("/user/program")
    public String program( @RequestParam Long userId, ModelMap models )
    {
        User user = userDao.getUser( userId );
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
            user = userDao.saveUser( user );
            logger.info( SecurityUtils.getUser().getUsername()
                + " removed major for " + user.getUsername() );
        }
        else
        {
            user.setMajor( departmentDao.getDepartment( majorId ) );
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
        User student = userDao.getUser( userId );

        if( programId == null )
        {
            student.setPersonalProgram( null );
            student = userDao.saveUser( student );
            logger.info( SecurityUtils.getUser().getUsername()
                + " removed personal program for " + student.getUsername() );
        }
        else
        {
            Program program = programDao.getProgram( programId );
            PersonalProgram personalProgram = new PersonalProgram( program );
            personalProgram.setStudent( student );
            personalProgram.setDate( new Date() );
            personalProgram = personalProgramDao
                .savePersonalProgram( personalProgram );

            student.setPersonalProgram( personalProgram );
            student = userDao.saveUser( student );
            logger.info( SecurityUtils.getUser().getUsername()
                + " set personal program " + personalProgram.getId() + " for "
                + student.getUsername() );
        }
        // Program is the 4th tab
        return "redirect:../view?id=" + userId + "#3";
    }

}
