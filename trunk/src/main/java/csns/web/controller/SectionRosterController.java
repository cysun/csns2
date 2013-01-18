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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.WebUtils;

import csns.helper.GradeSheet;
import csns.importer.ImportedUser;
import csns.importer.RosterImporter;
import csns.model.academics.Enrollment;
import csns.model.academics.Section;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.GradeDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Controller
@SessionAttributes("rosterImporter")
public class SectionRosterController {

    @Autowired
    UserDao userDao;

    @Autowired
    GradeDao gradeDao;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    EnrollmentDao enrollmentDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WebApplicationContext context;

    @Resource(name = "contentTypes")
    Properties contentTypes;

    @RequestMapping("/section/roster")
    public String roster( @RequestParam Long id, ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        models.put( "gradeSheet", new GradeSheet( section ) );
        models.put( "grades", gradeDao.getGrades() );
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
                importedStudent.setNewAccount( true );
                importedStudent.setNewEnrollment( true );
            }
            else if( !section.isEnrolled( student ) )
            {
                enrollmentDao.saveEnrollment( new Enrollment( section, student ) );
                importedStudent.setNewAccount( false );
                importedStudent.setNewEnrollment( true );
            }
            else
            {
                importedStudent.setNewAccount( false );
                importedStudent.setNewEnrollment( false );
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

    @RequestMapping(value = "/section/roster/add", params = "userId")
    public String add( @RequestParam Long sectionId, @RequestParam Long userId )
    {
        Section section = sectionDao.getSection( sectionId );
        User student = userDao.getUser( userId );
        Enrollment enrollment = enrollmentDao.getEnrollment( section, student );
        if( enrollment == null )
            enrollmentDao.saveEnrollment( new Enrollment( section, student ) );

        return "redirect:/section/roster?id=" + sectionId;
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
        Enrollment enrollment = enrollmentDao.getEnrollment( section, student );
        if( enrollment == null )
            enrollmentDao.saveEnrollment( new Enrollment( section, student ) );

        sessionStatus.setComplete();
        return "redirect:/section/roster?id=" + sectionId;
    }

    @RequestMapping("/section/roster/drop")
    public String drop( @RequestParam("userId") Long ids[],
        @RequestParam Long sectionId )
    {
        Section section = sectionDao.getSection( sectionId );
        List<User> students = userDao.getUsers( ids );
        for( User student : students )
        {
            Enrollment enrollment = enrollmentDao.getEnrollment( section,
                student );
            enrollmentDao.deleteEnrollment( enrollment );
        }

        return "redirect:/section/roster?id=" + sectionId;
    }

    @RequestMapping("/section/roster/export")
    public String export( @RequestParam Long id, HttpServletResponse response )
        throws IOException
    {
        Section section = sectionDao.getSection( id );
        GradeSheet gradeSheet = new GradeSheet( section );

        response.setContentType( contentTypes.getProperty( "xlsx" ) );
        response.setHeader( "Content-Disposition", "attachment; filename="
            + section.getCourse().getCode() + "-"
            + section.getQuarter().getShortString() + ".xlsx" );

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet( "Grades" );

        int n = section.getAssignments().size();
        Row row = sheet.createRow( 0 );
        row.createCell( 0 ).setCellValue( "Name" );
        for( int i = 0; i < n; ++i )
            row.createCell( i + 1 ).setCellValue(
                section.getAssignments().get( i ).getAlias() );
        row.createCell( n + 1 ).setCellValue( "Grade" );

        int rowIndex = 1;
        Map<Enrollment, String[]> studentGrades = gradeSheet.getStudentGrades();
        for( Enrollment enrollment : studentGrades.keySet() )
        {
            row = sheet.createRow( rowIndex++ );
            row.createCell( 0 ).setCellValue(
                enrollment.getStudent().getLastName() + ", "
                    + enrollment.getStudent().getFirstName() );
            for( int i = 0; i < n; ++i )
            {
                Cell cell = row.createCell( i + 1 );
                String grade = studentGrades.get( enrollment )[i];
                if( StringUtils.hasText( grade )
                    && grade.matches( "-?\\d+(\\.\\d+)?" ) )
                    cell.setCellValue( Double.parseDouble( grade ) );
                else
                    cell.setCellValue( grade );
            }
            if( enrollment.getGrade() != null )
                row.createCell( n + 1 ).setCellValue(
                    enrollment.getGrade().getSymbol() );
        }

        wb.write( response.getOutputStream() );

        return null;
    }

}
