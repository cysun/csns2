/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2017, Chengyu Sun (csun@calstatela.edu).
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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Enrollment;
import csns.model.academics.Grade;
import csns.model.academics.Section;
import csns.model.academics.Term;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.GradeDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.util.ExcelReader;

@Controller
@SuppressWarnings("deprecation")
public class UserGradeImportController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory
        .getLogger( UserGradeImportController.class );

    @RequestMapping(value = "/user/importGrades", method = RequestMethod.POST)
    public String importGrades(
        @RequestParam(value = "file") MultipartFile uploadedFile,
        ModelMap models ) throws IOException
    {
        UserGradeImportResults results = new UserGradeImportResults();
        models.put( "results", results );

        if( uploadedFile == null || uploadedFile.isEmpty() )
            return "gradeImportResults";

        // colIndexes is a map of <colName,colIndex>
        ExcelReader excelReader = new ExcelReader(
            uploadedFile.getInputStream() );
        String cols[] = excelReader.nextRow();
        Map<String, Integer> colIndexes = new HashMap<String, Integer>();
        for( int i = 0; i < cols.length; ++i )
            colIndexes.put( cols[i].trim().toUpperCase(), i );
        logger.info( "Header Row: " + cols.toString() );

        while( excelReader.hasNextRow() )
        {
            cols = excelReader.nextRow();
            String cin = cols[colIndexes.get( "CIN" )];
            if( cin == null || cin.trim().length() == 0 ) break;

            results.entriesProcessed++;

            User user = results.users.get( cin );
            if( user == null )
            {
                user = userDao.getUserByCin( cin );
                if( user == null )
                {
                    user = createUser( colIndexes, cols, results );
                    results.accountsCreated++;
                }
                results.users.put( cin, user );
            }

            String code = cols[colIndexes.get( "SUBJECT" )].trim()
                + cols[colIndexes.get( "CATALOG" )].trim();
            if( results.nocourses.contains( code ) ) continue;
            Course course = results.courses.get( code );
            if( course == null )
            {
                course = courseDao.getCourse( code );
                if( course != null )
                    results.courses.put( code, course );
                else
                {
                    results.nocourses.add( code );
                    continue;
                }
            }

            Term term = new Term();
            term.setCode(
                Integer.parseInt( cols[colIndexes.get( "TERM" )] ) - 1000 );
            Grade grade = results.grades
                .get( cols[colIndexes.get( "GRADE" )].trim() );
            Enrollment enrollment = enrollmentDao.getEnrollment( course, term,
                user );
            if( enrollment != null )
            {
                if( grade != enrollment.getGrade() )
                {
                    enrollment.setGrade( grade );
                    enrollment = enrollmentDao.saveEnrollment( enrollment );
                    results.gradesUpdated++;
                }
            }
            else
            {
                Section section = results.specialSections
                    .get( code + term.getCode() );
                if( section == null )
                {
                    section = sectionDao.addSection( term, course, null );
                    results.specialSections.put( code + term.getCode(),
                        section );
                    enrollment = new Enrollment( section, user, grade );
                    enrollment = enrollmentDao.saveEnrollment( enrollment );
                    results.gradesAdded++;
                }
            }
        }

        excelReader.close();

        return "user/gradeImportResults";
    }

    private User createUser( Map<String, Integer> colIndexes, String cols[],
        UserGradeImportResults results )
    {
        User user = new User();
        String cin = cols[colIndexes.get( "CIN" )];
        user.setCin( cin );

        String name = cols[colIndexes.get( "NAME" )];
        String tokens[] = name.split( "," );
        user.setLastName( tokens[0].trim() );
        if( tokens[1].trim().contains( " " ) )
        {
            tokens = tokens[1].split( " " );
            user.setFirstName( tokens[0].trim() );
            user.setMiddleName( tokens[1].trim() );
        }
        else
            user.setFirstName( tokens[1] );

        user.setUsername( cin );
        String password = passwordEncoder.encodePassword( cin, null );
        user.setPassword( password );

        if( colIndexes.containsKey( "EMAIL" ) )
            user.setPrimaryEmail( cols[colIndexes.get( "EMAIL" )] );
        user.setTemporary( true );

        if( colIndexes.containsKey( "ACAD PLAN" ) )
        {
            String dept = cols[colIndexes.get( "ACAD PLAN" )].split( " " )[0];
            user.setMajor( results.departments.get( dept.toUpperCase() ) );
        }

        user = userDao.saveUser( user );

        logger.info( "New account created for user " + user.getName() );

        return user;
    }

    public class UserGradeImportResults {

        int entriesProcessed = 0;

        int accountsCreated = 0;

        int gradesAdded = 0;

        int gradesUpdated = 0;

        Map<String, User> users;

        Map<String, Course> courses;

        Set<String> nocourses;

        Map<String, Section> specialSections;

        Map<String, Department> departments;

        Map<String, Grade> grades;

        public UserGradeImportResults()
        {
            users = new HashMap<String, User>();
            courses = new HashMap<String, Course>();
            nocourses = new LinkedHashSet<String>();
            specialSections = new HashMap<String, Section>();

            departments = new HashMap<String, Department>();
            for( Department d : departmentDao.getDepartments() )
                departments.put( d.getAbbreviation().toUpperCase(), d );

            grades = new HashMap<String, Grade>();
            for( Grade grade : gradeDao.getGrades() )
                grades.put( grade.getSymbol(), grade );
        }

        public int getEntriesProcessed()
        {
            return entriesProcessed;
        }

        public int getAccountsCreated()
        {
            return accountsCreated;
        }

        public int getGradesAdded()
        {
            return gradesAdded;
        }

        public int getGradesUpdated()
        {
            return gradesUpdated;
        }

        public Set<String> getNocourses()
        {
            return nocourses;
        }

    }

}
