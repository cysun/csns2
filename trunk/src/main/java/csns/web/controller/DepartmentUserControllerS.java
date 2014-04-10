/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.WebUtils;

import csns.importer.ImportedUser;
import csns.importer.StudentsImporter;
import csns.model.academics.AcademicStanding;
import csns.model.academics.Department;
import csns.model.academics.Standing;
import csns.model.academics.dao.AcademicStandingDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.StandingDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.web.editor.StandingPropertyEditor;

@Controller
@SessionAttributes({ "importer", "standings" })
@SuppressWarnings("deprecation")
public class DepartmentUserControllerS {

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

    @Autowired
    private WebApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger( DepartmentUserControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Standing.class,
            (StandingPropertyEditor) context.getBean( "standingPropertyEditor" ) );
    }

    @RequestMapping(value = "/department/{dept}/user/import",
        method = RequestMethod.GET)
    public String importStudents( @PathVariable String dept, ModelMap models )
    {
        StudentsImporter importer = (StudentsImporter) context.getBean( "studentsImporter" );
        importer.setDepartment( departmentDao.getDepartment( dept ) );
        models.put( "importer", importer );
        models.put( "standings", standingDao.getStandings() );
        return "department/user/import0";
    }

    @RequestMapping(value = "/department/{dept}/user/import",
        method = RequestMethod.POST)
    public String importStudents(
        @ModelAttribute("importer") StudentsImporter importer,
        @PathVariable String dept, @RequestParam("_page") int currentPage,
        HttpServletRequest request, SessionStatus sessionStatus, ModelMap models )
    {
        if( request.getParameter( "_finish" ) == null )
        {
            int targetPage = WebUtils.getTargetPage( request, "_target",
                currentPage );
            if( targetPage == 1 && currentPage < targetPage )
            {
                for( ImportedUser importedStudent : importer.getImportedStudents() )
                {
                    User user = userDao.getUserByCin( importedStudent.getCin() );
                    if( user == null ) importedStudent.setNewAccount( true );
                }
            }
            return "department/user/import" + targetPage;
        }

        // received _finish, so do the import.

        // need to get a managed instance of department, otherwise
        // user.getCurrentStanding(department) won't work correctly.
        Department department = departmentDao.getDepartment( importer.getDepartment()
            .getId() );
        Standing standing = importer.getStanding();
        for( ImportedUser importedStudent : importer.getImportedStudents() )
        {
            String cin = importedStudent.getCin();
            User student = userDao.getUserByCin( cin );
            if( student == null )
            {
                student = new User();
                student.setCin( cin );
                student.setLastName( importedStudent.getLastName() );
                student.setFirstName( importedStudent.getFirstName() );
                student.setUsername( cin );
                String password = passwordEncoder.encodePassword( cin, null );
                student.setPassword( password );
                student.setPrimaryEmail( cin + "@localhost" );
                student.setTemporary( true );
                student = userDao.saveUser( student );
                logger.info( "New account created for student "
                    + student.getName() );
            }

            AcademicStanding academicStanding = academicStandingDao.getAcademicStanding(
                student, department, standing );

            if( academicStanding == null )
            {
                logger.debug( "New standing " + standing.getSymbol()
                    + " for student " + student.getName() );
                academicStanding = new AcademicStanding( student, department,
                    standing, importedStudent.getQuarter() );
            }
            else
            {
                logger.debug( "Standing updated for student "
                    + student.getName() + ": "
                    + academicStanding.getStanding().getSymbol() + " "
                    + academicStanding.getQuarter().getShortString() + " -> "
                    + importedStudent.getQuarter().getShortString() );
                academicStanding.setQuarter( importedStudent.getQuarter() );
            }
            academicStanding = academicStandingDao.saveAcademicStanding( academicStanding );

            AcademicStanding currentStanding = student.getCurrentStanding( department );
            if( currentStanding == null
                || currentStanding.compareTo( academicStanding ) < 0 )
            {
                if( currentStanding != null )
                    logger.debug( "Current standing updated for student "
                        + student.getName() + ": "
                        + currentStanding.getStanding().getSymbol() + " -> "
                        + academicStanding.getStanding().getSymbol() );

                student.getCurrentStandings()
                    .put( department, academicStanding );
                userDao.saveUser( student );
            }
        }

        sessionStatus.setComplete();

        models.put( "backUrl", "/department/" + dept + "/people" );
        return "status";
    }

}
