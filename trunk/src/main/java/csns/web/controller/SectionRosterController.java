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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.WebUtils;

import csns.importer.ImportedUser;
import csns.importer.RosterImporter;
import csns.model.academics.Enrollment;
import csns.model.academics.Section;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.web.helper.GradeSheet;

@Controller
@SessionAttributes("rosterImporter")
public class SectionRosterController {

    @Autowired
    UserDao userDao;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    EnrollmentDao enrollmentDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WebApplicationContext context;

    @RequestMapping("/section/roster")
    public String roster( @RequestParam Long id, ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        models.put( "gradeSheet", new GradeSheet( section ) );
        return "section/roster";
    }

    @RequestMapping(value = "/section/roster/import",
        method = RequestMethod.GET)
    public String importRoster( @RequestParam Long sectionId, ModelMap models )
    {
        RosterImporter rosterImporter = (RosterImporter) context.getBean( "rosterImporter" );
        rosterImporter.setSection( sectionDao.getSection( sectionId ) );
        models.put( "rosterImporter", rosterImporter );
        return "section/roster/import0";
    }

    @RequestMapping(value = "/section/roster/import",
        method = RequestMethod.POST)
    public String importRoster( @ModelAttribute RosterImporter rosterImporter,
        @RequestParam("_page") int currentPage, HttpServletRequest request,
        SessionStatus sessionStatus, ModelMap models )
    {
        Map<Integer, String> views = new HashMap<Integer, String>();
        views.put( 0, "section/roster/import0" );
        views.put( 1, "section/roster/import1" );
        views.put( 2, "section/roster/import2" );

        if( request.getParameter( "_finish" ) == null )
        {
            int targetPage = WebUtils.getTargetPage( request, "_target",
                currentPage );
            return views.get( targetPage );
        }

        Section section = sectionDao.getSection( rosterImporter.getSection()
            .getId() );
        for( ImportedUser importedStudent : rosterImporter.getImportedStudents() )
        {
            String cin = importedStudent.getCin();
            User student = userDao.getUserByCin( cin );
            if( student == null )
            {
                student = new User();
                student.setCin( cin );
                student.setLastName( importedStudent.getLastName() );
                student.setFirstName( importedStudent.getFirstName() );
                student.setMiddleName( importedStudent.getMiddleName() );
                student.setUsername( cin );
                String password = passwordEncoder.encodePassword( cin, null );
                student.setPassword( password );
                student.setPrimaryEmail( cin + "@localhost" );
                student.setTemporary( true );
                student = userDao.saveUser( student );
                enrollmentDao.saveEnrollment( new Enrollment( section, student ) );
                importedStudent.setAccountCreated( true );
                importedStudent.setAddedToSection( true );
            }
            else if( !section.isEnrolled( student ) )
            {
                enrollmentDao.saveEnrollment( new Enrollment( section, student ) );
                importedStudent.setAccountCreated( false );
                importedStudent.setAddedToSection( true );
            }
            else
            {
                importedStudent.setAccountCreated( false );
                importedStudent.setAddedToSection( false );
            }
        }

        models.put( "rosterImporter", rosterImporter );
        sessionStatus.setComplete();
        return views.get( 2 );
    }

    @RequestMapping(value = "/section/roster/add", method = RequestMethod.GET)
    public String add( @RequestParam Long sectionId, ModelMap models )
    {
        models.put( "user", new User() );
        return "section/roster/add";
    }

    @RequestMapping(value = "/section/roster/add", method = RequestMethod.POST)
    public String add( @ModelAttribute User user, @RequestParam Long sectionId,
        SessionStatus sessionStatus )
    {
        String cin = user.getCin();
        User student = userDao.getUserByCin( cin );
        if( student == null )
        {
            student = user;
            student.setUsername( cin );
            String password = passwordEncoder.encodePassword( cin, null );
            student.setPassword( password );
            student.setPrimaryEmail( cin + "@localhost" );
            student.setTemporary( true );
            student = userDao.saveUser( student );
        }

        Section section = sectionDao.getSection( sectionId );
        enrollmentDao.saveEnrollment( new Enrollment( section, student ) );
        sessionStatus.setComplete();
        return "redirect:/section/roster?id=" + sectionId;
    }

}
