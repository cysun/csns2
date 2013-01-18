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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import csns.helper.GradeSheet;
import csns.importer.GradesImporter;
import csns.importer.ImportedUser;
import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Enrollment;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.GradeDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.web.editor.CoursePropertyEditor;
import csns.web.editor.QuarterPropertyEditor;
import csns.web.editor.UserPropertyEditor;

@Controller
@SessionAttributes("importer")
public class DepartmentSectionController {

    @Autowired
    UserDao userDao;

    @Autowired
    GradeDao gradeDao;

    @Autowired
    EnrollmentDao enrollmentDao;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WebApplicationContext context;

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

    @RequestMapping("/department/{dept}/sections")
    public String sections( @PathVariable String dept,
        @RequestParam(required = false) Quarter quarter, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        Quarter currentQuarter = new Quarter();
        if( quarter == null ) quarter = currentQuarter;

        List<Quarter> quarters = new ArrayList<Quarter>();
        quarters.add( currentQuarter.next() );
        for( int i = 0; i < 12; ++i )
        {
            quarters.add( currentQuarter );
            currentQuarter = currentQuarter.previous();
        }

        models.put( "department", department );
        models.put( "quarter", quarter );
        models.put( "quarters", quarters );
        models.put( "sections", sectionDao.getSections( department, quarter ) );
        return "department/sections";
    }

    @RequestMapping("/department/{dept}/section")
    public String section( @PathVariable String dept, @RequestParam Long id,
        ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        models.put( "department", departmentDao.getDepartment( dept ) );
        models.put( "gradeSheet", new GradeSheet( section ) );
        return "section/view";
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

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/section?id="
            + section.getId();
    }

    @RequestMapping("/department/{dept}/section/grades")
    public String grades( @RequestParam Quarter quarter,
        @RequestParam Course course, @RequestParam int number,
        HttpServletResponse response ) throws IOException
    {
        Section section = sectionDao.getSection( quarter, course, number );
        if( section != null )
        {
            List<Enrollment> enrollments = section.getEnrollments();
            Collections.sort( enrollments );
            response.setContentType( "text/plain" );
            PrintWriter out = response.getWriter();
            for( int i = 0; i < enrollments.size(); ++i )
            {
                User student = enrollments.get( i ).getStudent();
                String grade = enrollments.get( i ).getGrade() == null ? ""
                    : enrollments.get( i ).getGrade().getSymbol();
                out.println( (i + 1) + " " + student.getCin() + " "
                    + student.getLastName() + "," + student.getFirstName()
                    + " " + grade );
            }
        }
        return null;
    }

}
