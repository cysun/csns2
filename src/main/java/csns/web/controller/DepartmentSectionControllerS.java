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

import java.util.List;

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

import csns.importer.GradesImporter;
import csns.importer.ImportedUser;
import csns.model.academics.Course;
import csns.model.academics.Enrollment;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.GradeDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.web.editor.CoursePropertyEditor;
import csns.web.editor.QuarterPropertyEditor;
import csns.web.editor.UserPropertyEditor;

@Controller
@SessionAttributes("importer")
@SuppressWarnings("deprecation")
public class DepartmentSectionControllerS {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger( DepartmentSectionControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Quarter.class,
            (QuarterPropertyEditor) context.getBean( "quarterPropertyEditor" ) );
        binder.registerCustomEditor( User.class,
            (UserPropertyEditor) context.getBean( "userPropertyEditor" ) );
        binder.registerCustomEditor( Course.class,
            (CoursePropertyEditor) context.getBean( "coursePropertyEditor" ) );
    }

    @RequestMapping(value = "/department/{dept}/section/import",
        method = RequestMethod.GET)
    public String importSection( @PathVariable String dept,
        @RequestParam Quarter quarter, ModelMap models )
    {
        GradesImporter importer = (GradesImporter) context.getBean( "gradesImporter" );
        importer.setDepartment( departmentDao.getDepartment( dept ) );
        importer.setSection( new Section() );
        importer.getSection().setQuarter( quarter );
        models.put( "importer", importer );
        return "department/section/import0";
    }

    @RequestMapping(value = "/department/{dept}/section/import",
        method = RequestMethod.POST)
    public String importSection(
        @ModelAttribute("importer") GradesImporter importer,
        @PathVariable String dept, @RequestParam("_page") int currentPage,
        HttpServletRequest request, SessionStatus sessionStatus )
    {
        if( request.getParameter( "_finish" ) == null )
        {
            int targetPage = WebUtils.getTargetPage( request, "_target",
                currentPage );

            if( targetPage == 1 && currentPage < targetPage )
            {
                importer.clear();
                Section section = importer.getSection();
                List<Section> sections = sectionDao.getSectionsByInstructor(
                    section.getInstructors().get( 0 ), section.getQuarter(),
                    section.getCourse() );
                for( Section s : sections )
                    importer.getSectionNumbers().add( s.getNumber() );
                section.setNumber( importer.getSectionNumbers().size() == 0
                    ? -1 : importer.getSectionNumbers().get( 0 ) );
            }

            if( targetPage == 2 )
            {
                int number = importer.getSection().getNumber();
                if( number > 0 )
                {
                    Quarter quarter = importer.getSection().getQuarter();
                    Course course = importer.getSection().getCourse();
                    Section section = sectionDao.getSection( quarter, course,
                        number );
                    for( ImportedUser student : importer.getImportedStudents() )
                    {
                        User user = userDao.getUserByCin( student.getCin() );
                        if( user == null )
                        {
                            student.setNewAccount( true );
                            student.setNewEnrollment( true );
                        }
                        else
                        {
                            Enrollment enrollment = section.getEnrollment( user );
                            if( enrollment == null )
                                student.setNewEnrollment( true );
                            else if( enrollment.getGrade() != null )
                                student.setOldGrade( enrollment.getGrade()
                                    .getSymbol() );
                        }
                    }
                }
            }

            return "department/section/import" + targetPage;
        }

        // received _finish, so do the import.
        Quarter quarter = importer.getSection().getQuarter();
        Course course = importer.getSection().getCourse();
        int number = importer.getSection().getNumber();
        Section section = number > 0 ? sectionDao.getSection( quarter, course,
            number ) : sectionDao.addSection( quarter, course,
            importer.getSection().getInstructors().get( 0 ) );

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
                student.setMiddleName( importedStudent.getMiddleName() );
                student.setUsername( cin );
                String password = passwordEncoder.encodePassword( cin, null );
                student.setPassword( password );
                student.setPrimaryEmail( cin + "@localhost" );
                student.setTemporary( true );
                student = userDao.saveUser( student );
            }
            Enrollment enrollment = section.getEnrollment( student );
            if( enrollment == null )
                enrollment = new Enrollment( section, student );
            if( importedStudent.getGrade() != null
                && (enrollment.getGrade() == null || !enrollment.getGrade()
                    .getSymbol()
                    .equalsIgnoreCase( importedStudent.getGrade() )) )
            {
                enrollment.setGrade( gradeDao.getGrade( importedStudent.getGrade() ) );
                enrollmentDao.saveEnrollment( enrollment );
            }
        }

        logger.info( SecurityUtils.getUser().getUsername()
            + " imported section " + section.getQuarter().getShortString()
            + " " + section.getCourse().getCode() + "-" + section.getNumber() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/section?id="
            + section.getId();
    }

}
