/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012, Chengyu Sun (csun@calstatela.edu).
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.AcademicStanding;
import csns.model.academics.Department;
import csns.model.academics.Quarter;
import csns.model.academics.Standing;
import csns.model.academics.dao.AcademicStandingDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.StandingDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;

@Controller
public class UserStandingController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private StandingDao standingDao;

    @Autowired
    private AcademicStandingDao academicStandingDao;

    private static final Logger logger = LoggerFactory.getLogger( UserStandingController.class );

    @RequestMapping("/user/standings")
    public String standings( @RequestParam Long userId,
        HttpServletRequest request, ModelMap models )
    {
        User user = userDao.getUser( userId );
        List<AcademicStanding> academicStandings = academicStandingDao.getAcademicStandings( user );
        List<AcademicStanding> currentStandings = new ArrayList<AcademicStanding>();
        currentStandings.addAll( user.getCurrentStandings().values() );
        Collections.sort( currentStandings );
        models.put( "user", user );
        models.put( "academicStandings", academicStandings );
        models.put( "currentStandings", currentStandings );
        models.put( "standings", standingDao.getStandings() );

        User currentUser = SecurityUtils.getUser();
        for( AcademicStanding currentStanding : currentStandings )
            if( currentUser.isFaculty( currentStanding.getDepartment()
                .getAbbreviation() ) )
            {
                models.put( "isFaculty", true );
                break;
            }

        // The dept request attribute is set by DepartmentFilter.
        String dept = (String) request.getAttribute( "dept" );
        if( dept != null )
            models.put( "department", departmentDao.getDepartment( dept ) );
        else
            logger.warn( "Cannot find the dept attribute." );

        return "user/standings";
    }

    @RequestMapping("/user/standing/edit")
    public String edit( @RequestParam Long userId,
        @RequestParam Long departmentId, @RequestParam Long standingId,
        @RequestParam int year, @RequestParam int quarterSuffix )
    {
        User student = userDao.getUser( userId );
        Department department = departmentDao.getDepartment( departmentId );
        Standing standing = standingDao.getStanding( standingId );
        AcademicStanding academicStanding = academicStandingDao.getAcademicStanding(
            student, department, standing );
        Quarter quarter = new Quarter( (year - 1900) * 10 + quarterSuffix );

        if( academicStanding == null )
            academicStanding = new AcademicStanding( student, department,
                standing, quarter );
        else
            academicStanding.setQuarter( quarter );
        academicStanding = academicStandingDao.saveAcademicStanding( academicStanding );

        AcademicStanding currentStanding = student.getCurrentStanding( department );
        if( currentStanding == null
            || currentStanding.compareTo( academicStanding ) < 0 )
        {
            student.getCurrentStandings().put( department, academicStanding );
            userDao.saveUser( student );
        }

        // Standing is the 2nd tab
        return "redirect:/user/view?id=" + userId + "#1";
    }

    @RequestMapping("/user/standing/delete")
    public String delete( @RequestParam Long id )
    {
        AcademicStanding academicStanding = academicStandingDao.getAcademicStanding( id );
        User student = academicStanding.getStudent();
        Department department = academicStanding.getDepartment();
        if( student.getCurrentStanding( department ) == academicStanding )
            student.getCurrentStandings().remove( department );

        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted academic standing " + academicStanding );

        academicStandingDao.deleteAcademicStanding( academicStanding );

        if( student.getCurrentStanding( department ) == null )
        {
            academicStanding = academicStandingDao.getLatestAcademicStanding(
                student, department );
            if( academicStanding != null )
            {
                student.getCurrentStandings()
                    .put( department, academicStanding );
                userDao.saveUser( student );
            }
        }

        // Standing is the 2nd tab
        return "redirect:/user/view?id=" + student.getId() + "#1";
    }

}
