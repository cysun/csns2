/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2016, Chengyu Sun (csun@calstatela.edu).
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
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import csns.helper.GradeSheet;
import csns.model.academics.Enrollment;
import csns.model.academics.Section;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.GradeDao;
import csns.model.academics.dao.SectionDao;
import csns.model.assessment.RubricAssignment;
import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.RubricSubmission;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;

@Controller
@SuppressWarnings("deprecation")
public class SectionRosterController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "contentTypes")
    private Properties contentTypes;

    private static final Logger logger = LoggerFactory
        .getLogger( SectionRosterController.class );

    @RequestMapping("/section/roster")
    public String roster( @RequestParam Long id, ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        models.put( "gradeSheet", new GradeSheet( section ) );
        models.put( "grades", gradeDao.getGrades() );
        return "section/roster";
    }

    @RequestMapping(value = "/section/roster/add", params = "userId")
    public String add( @RequestParam Long sectionId, @RequestParam Long userId )
    {
        Section section = sectionDao.getSection( sectionId );
        User student = userDao.getUser( userId );
        Enrollment enrollment = enrollmentDao.getEnrollment( section, student );
        if( enrollment == null )
        {
            enrollmentDao.saveEnrollment( new Enrollment( section, student ) );
            logger.info( SecurityUtils.getUser().getUsername()
                + " added student " + userId + " to section " + sectionId );
        }

        return "redirect:/section/roster?id=" + sectionId;
    }

    @RequestMapping(value = "/section/roster/add", method = RequestMethod.GET)
    public String add( @RequestParam Long sectionId, ModelMap models )
    {
        models.put( "user", new User() );
        return "section/roster/add";
    }

    @RequestMapping(value = "/section/roster/add", method = RequestMethod.POST)
    public String add( @ModelAttribute User user, @RequestParam Long sectionId )
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
        {
            enrollmentDao.saveEnrollment( new Enrollment( section, student ) );
            logger
                .info( SecurityUtils.getUser().getUsername() + " added student "
                    + student.getId() + " to section " + sectionId );
        }

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
            logger.info(
                SecurityUtils.getUser().getUsername() + " removed student "
                    + student.getId() + " from section " + sectionId );
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
        response.setHeader( "Content-Disposition",
            "attachment; filename=" + section.getCourse().getCode() + "-"
                + section.getTerm().getShortString() + ".xlsx" );

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet( "Grades" );

        int n = section.getAssignments().size();
        Row row = sheet.createRow( 0 );
        row.createCell( 0 ).setCellValue( "CIN" );
        row.createCell( 1 ).setCellValue( "Name" );
        row.createCell( 2 ).setCellValue( "Email" );
        for( int i = 0; i < n; ++i )
            row.createCell( i + 3 )
                .setCellValue( section.getAssignments().get( i ).getAlias() );
        row.createCell( n + 3 ).setCellValue( "Grade" );

        int rowIndex = 1;
        Map<Enrollment, String[]> studentGrades = gradeSheet.getStudentGrades();
        for( Enrollment enrollment : studentGrades.keySet() )
        {
            row = sheet.createRow( rowIndex++ );
            row.createCell( 0 )
                .setCellValue( enrollment.getStudent().getCin() );
            row.createCell( 1 )
                .setCellValue( enrollment.getStudent().getLastName() + ", "
                    + enrollment.getStudent().getFirstName() );
            row.createCell( 2 )
                .setCellValue( enrollment.getStudent().getPrimaryEmail() );
            for( int i = 0; i < n; ++i )
            {
                Cell cell = row.createCell( i + 3 );
                String grade = studentGrades.get( enrollment )[i];
                if( StringUtils.hasText( grade )
                    && grade.matches( "-?\\d+(\\.\\d+)?" ) )
                    cell.setCellValue( Double.parseDouble( grade ) );
                else
                    cell.setCellValue( grade );
            }
            if( enrollment.getGrade() != null ) row.createCell( n + 3 )
                .setCellValue( enrollment.getGrade().getSymbol() );
        }

        List<RubricAssignment> rubricAssignments = section
            .getRubricAssignments();
        if( rubricAssignments != null && rubricAssignments.size() > 0 )
        {
            sheet = wb.createSheet( "Rubrics" );

            List<Enrollment> enrollments = section.getEnrollments();
            row = sheet.createRow( 0 );
            row.createCell( 0 ).setCellValue( "CIN" );
            row.createCell( 1 ).setCellValue( "Name" );

            rowIndex = 1;
            for( Enrollment enrollment : enrollments )
            {
                row = sheet.createRow( rowIndex++ );
                row.createCell( 0 )
                    .setCellValue( enrollment.getStudent().getCin() );
                row.createCell( 1 )
                    .setCellValue( enrollment.getStudent().getLastName() + ", "
                        + enrollment.getStudent().getFirstName() );
            }

            int currentColumn = 2;
            for( RubricAssignment rubricAssignment : rubricAssignments )
            {
                if( rubricAssignment.isEvaluatedByInstructors() )
                {
                    row = sheet.getRow( 0 );
                    row.createCell( currentColumn )
                        .setCellValue( rubricAssignment.getName() );

                    for( int i = 0; i < enrollments.size(); ++i )
                    {
                        row = sheet.getRow( i + 1 );
                        String cin = row.getCell( 0 ).getStringCellValue();
                        double eval = getAverageEval( rubricAssignment, cin,
                            RubricEvaluation.Type.INSTRUCTOR );
                        if( eval > 0 ) row.createCell( currentColumn )
                            .setCellValue( eval );
                    }

                    ++currentColumn;
                }

                if( rubricAssignment.isEvaluatedByStudents() )
                {
                    row = sheet.getRow( 0 );
                    row.createCell( currentColumn )
                        .setCellValue( rubricAssignment.getName() + "(Peer)" );

                    for( int i = 0; i < enrollments.size(); ++i )
                    {
                        row = sheet.getRow( i + 1 );
                        String cin = row.getCell( 0 ).getStringCellValue();
                        double eval = getAverageEval( rubricAssignment, cin,
                            RubricEvaluation.Type.PEER );
                        if( eval > 0 ) row.createCell( currentColumn )
                            .setCellValue( eval );
                    }

                    ++currentColumn;
                }

                if( rubricAssignment.isEvaluatedByExternal() )
                {
                    row = sheet.getRow( 0 );
                    row.createCell( currentColumn )
                        .setCellValue(
                            rubricAssignment.getName() + "(External)" );

                    for( int i = 0; i < enrollments.size(); ++i )
                    {
                        row = sheet.getRow( i + 1 );
                        String cin = row.getCell( 0 ).getStringCellValue();
                        double eval = getAverageEval( rubricAssignment, cin,
                            RubricEvaluation.Type.EXTERNAL );
                        if( eval > 0 ) row.createCell( currentColumn )
                            .setCellValue( eval );
                    }

                    ++currentColumn;
                }
            }
        }

        wb.write( response.getOutputStream() );
        wb.close();

        logger.info( SecurityUtils.getUser().getUsername()
            + " exported the roster of section " + id );

        return null;
    }

    private double getAverageEval( RubricAssignment assignment, String cin,
        RubricEvaluation.Type type )
    {
        int count = 0;
        double total = 0;

        for( RubricSubmission submission : assignment.getSubmissions() )
        {
            if( submission.getStudent().getCin().equals( cin ) )
            {
                for( RubricEvaluation evaluation : submission.getEvaluations() )
                {
                    if( evaluation.getType().equals( type )
                        && evaluation.isCompleted() )
                    {
                        ++count;
                        total += evaluation.getTotalRating();
                    }
                }
            }
        }

        return total / count;
    }
}
