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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.AcademicStanding;
import csns.model.academics.Department;
import csns.model.academics.Standing;
import csns.model.academics.Term;
import csns.model.academics.dao.AcademicStandingDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.StandingDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.util.ExcelReader;

@Controller
@SuppressWarnings("deprecation")
public class UserImportController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private StandingDao standingDao;

    @Autowired
    private AcademicStandingDao academicStandingDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory
        .getLogger( UserImportController.class );

    @RequestMapping(value = "/user/import", method = RequestMethod.GET)
    public String importUsers()
    {
        return "user/import";
    }

    @RequestMapping(value = "/user/import", method = RequestMethod.POST)
    public String importUsers(
        @RequestParam(value = "file") MultipartFile uploadedFile,
        ModelMap models ) throws IOException
    {
        int n = importUsers( null, uploadedFile );
        models.put( "message", "status.user.import" );
        models.put( "arguments", n );
        models.put( "backUrl", "/user/search" );
        return "status";
    }

    @RequestMapping(value = "/department/{dept}/user/import",
        method = RequestMethod.GET)
    public String importUsers( @PathVariable String dept, ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( dept ) );
        return "user/import";
    }

    @RequestMapping(value = "/department/{dept}/user/import",
        method = RequestMethod.POST)
    public String importUsers( @PathVariable String dept,
        @RequestParam(value = "file") MultipartFile uploadedFile,
        ModelMap models ) throws IOException
    {
        int n = importUsers( departmentDao.getDepartment( dept ),
            uploadedFile );
        models.put( "message", "status.user.import" );
        models.put( "arguments", n );
        models.put( "backUrl", "/department/" + dept + "/people" );
        return "status";
    }

    private int importUsers( Department department, MultipartFile uploadedFile )
        throws IOException
    {
        if( uploadedFile == null || uploadedFile.isEmpty() ) return 0;

        int n = 0;
        ExcelReader excelReader = new ExcelReader(
            uploadedFile.getInputStream() );

        Map<String, Department> departments = new HashMap<String, Department>();
        if( excelReader.hasColumn( "PLAN" ) )
            for( Department d : departmentDao.getDepartments() )
            departments.put( d.getAbbreviation().toUpperCase(), d );

        Map<String, Standing> standings = new HashMap<String, Standing>();
        if( excelReader.hasColumn( "STANDING" ) )
        {
            for( Standing s : standingDao.getStandings() )
                standings.put( s.getSymbol(), s );
            // We don't have B0-B4 standing, so all of them are B
            for( int i = 0; i <= 4; ++i )
                standings.put( "B" + i, standings.get( "B" ) );
        }

        while( excelReader.next() )
        {
            String cin = excelReader.get( "CIN" );
            User user = userDao.getUserByCin( cin );
            if( user == null ) user = createUser( excelReader );

            if( excelReader.hasColumn( "STANDING" ) )
            {
                if( excelReader.hasColumn( "PLAN" ) )
                {
                    String plan = excelReader.get( "PLAN" );
                    department = departments.get( plan.split( " " )[0] );
                    if( department == null )
                        logger.warn( "No department for: " + plan );
                }

                // department is either from the method argument or from plan
                if( department != null )
                {
                    String symbol = excelReader.get( "STANDING" );
                    Standing standing = standings.get( symbol );
                    if( standing == null )
                        logger.warn( "Unrecognized standing: )" + symbol );
                    else
                    {
                        Term term = new Term();
                        if( excelReader.hasColumn( "TERM" ) ) term.setCode(
                            Integer.parseInt( excelReader.get( "TERM" ) )
                                - 1000 );
                        user = updateStanding( user, department, standing,
                            term );
                    }
                }
            }

            ++n;
        }
        excelReader.close();
        return n;
    }

    private User createUser( ExcelReader excelReader )
    {
        User user = new User();
        String cin = excelReader.get( "CIN" );
        user.setCin( cin );
        user.setLastName( excelReader.get( "LAST NAME" ) );
        user.setFirstName( excelReader.get( "FIRST NAME" ) );
        user.setUsername( cin );
        String password = passwordEncoder.encodePassword( cin, null );
        user.setPassword( password );
        if( excelReader.hasColumn( "EMAIL" ) )
            user.setPrimaryEmail( excelReader.get( "EMAIL" ) );
        user.setTemporary( true );
        user = userDao.saveUser( user );

        logger.info( "New account created for user " + user.getName() );

        return user;
    }

    private User updateStanding( User user, Department department,
        Standing standing, Term term )
    {
        AcademicStanding academicStanding = academicStandingDao
            .getAcademicStanding( user, department, standing );

        if( academicStanding == null )
        {
            logger.debug( "New standing " + standing.getSymbol()
                + " for student " + user.getName() );
            academicStanding = new AcademicStanding( user, department, standing,
                term );
        }
        else
        {
            logger.debug( "Standing updated for student " + user.getName()
                + ": " + academicStanding.getStanding().getSymbol() + " "
                + academicStanding.getTerm().getShortString() + " -> "
                + term.getShortString() );
            academicStanding.setTerm( term );
        }
        academicStanding = academicStandingDao
            .saveAcademicStanding( academicStanding );

        AcademicStanding currentStanding = user
            .getCurrentStanding( department );
        if( currentStanding == null
            || currentStanding.compareTo( academicStanding ) < 0 )
        {
            if( currentStanding != null ) logger
                .debug( "Current standing updated for student " + user.getName()
                    + ": " + currentStanding.getStanding().getSymbol() + " -> "
                    + academicStanding.getStanding().getSymbol() );

            user.getCurrentStandings().put( department, academicStanding );
            user = userDao.saveUser( user );
        }

        return user;
    }

}
