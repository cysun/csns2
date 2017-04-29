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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
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

    @RequestMapping(
        value = { "/user/importGrades",
            "/department/{dept}/user/importGrades" },
        method = RequestMethod.POST)
    public String importGrades(
        @RequestParam(value = "file") MultipartFile uploadedFile,
        ModelMap models ) throws IOException
    {
        UserGradeImportResults results = new UserGradeImportResults();
        models.put( "results", results );

        if( uploadedFile == null || uploadedFile.isEmpty() )
            return "gradeImportResults";

        ExcelReader excelReader = new ExcelReader(
            uploadedFile.getInputStream() );

        String previous = null;
        while( excelReader.next() )
        {
            String cin = excelReader.get( "ID" );
            if( cin.length() == 0 ) break;

            results.entriesProcessed++;

            String gradeSymbol = excelReader.get( "GRADE" );
            if( gradeSymbol.length() == 0 )
            {
                results.emptyGrades++;
                continue;
            }
            Grade grade = results.grades.get( gradeSymbol );
            if( grade == null )
            {
                logger.warn( "Cannot recognize grade: " + gradeSymbol );
                continue;
            }

            String code = excelReader.get( "SUBJECT" )
                + excelReader.get( "CATALOG" );
            if( results.nocourses.contains( code ) ) continue;

            Term term = new Term();
            term.setCode(
                Integer.parseInt( excelReader.get( "TERM" ) ) - 1000 );
            String current = cin + "-" + code + "-" + term.getShortString();
            if( previous != null && previous.equals( current ) )
            {
                results.nogrades.add( Arrays.toString( excelReader.getRow() ) );
                previous = current;
                continue;
            }

            previous = current;

            User user = results.users.get( cin );
            if( user == null )
            {
                user = userDao.getUserByCin( cin );
                if( user == null )
                {
                    user = createUser( excelReader, results );
                    results.accountsCreated++;
                }
                results.users.put( cin, user );
            }

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

            List<Enrollment> enrollments = enrollmentDao.getEnrollments( course,
                term, user );
            if( enrollments.size() == 0 )
            {
                Section section = results.specialSections
                    .get( code + term.getCode() );
                if( section == null )
                {
                    section = sectionDao.getSpecialSection( term, course );
                    if( section == null )
                    {
                        section = sectionDao.addSection( term, course, null );
                        results.nosections.add( code + "-" + section.getNumber()
                            + ", " + term.getShortString() );
                    }
                    results.specialSections.put( code + term.getCode(),
                        section );
                }
                enrollmentDao
                    .saveEnrollment( new Enrollment( section, user, grade ) );
                results.gradesAdded++;
            }
            else if( enrollments.size() == 1 )
            {
                Enrollment enrollment = enrollments.get( 0 );
                Grade oldGrade = enrollment.getGrade();
                if( oldGrade == null
                    || !oldGrade.getId().equals( grade.getId() ) )
                {
                    enrollment.setGrade( grade );
                    enrollmentDao.saveEnrollment( enrollment );
                    results.gradesUpdated++;
                    logger.info( current + ": "
                        + (oldGrade == null ? "null" : oldGrade.getSymbol())
                        + " -> " + grade.getSymbol() );
                }
            }
            else
            {
                results.nogrades.add( Arrays.toString( excelReader.getRow() ) );
            }
        }

        excelReader.close();

        return "user/gradeImportResults";
    }

    private User createUser( ExcelReader excelReader,
        UserGradeImportResults results )
    {
        User user = new User();
        String cin = excelReader.get( "ID" );
        user.setCin( cin );

        String name = excelReader.get( "NAME" );
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

        if( excelReader.hasColumn( "EMAIL" ) )
            user.setPrimaryEmail( excelReader.get( "EMAIL" ) );
        user.setTemporary( true );

        if( excelReader.hasColumn( "ACAD PLAN" ) )
        {
            String dept = excelReader.get( "ACAD PLAN" ).split( " " )[0];
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

        int emptyGrades = 0;

        Map<String, User> users;

        Map<String, Course> courses;

        Map<String, Section> specialSections;

        Set<String> nocourses;

        List<String> nosections;

        List<String> nogrades;

        Map<String, Department> departments;

        Map<String, Grade> grades;

        public UserGradeImportResults()
        {
            users = new HashMap<String, User>();
            courses = new HashMap<String, Course>();
            specialSections = new HashMap<String, Section>();
            nocourses = new LinkedHashSet<String>();
            nosections = new ArrayList<String>();
            nogrades = new ArrayList<String>();

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

        public int getEmptyGrades()
        {
            return emptyGrades;
        }

        public Set<String> getNocourses()
        {
            return nocourses;
        }

        public List<String> getNosections()
        {
            return nosections;
        }

        public List<String> getNogrades()
        {
            return nogrades;
        }

    }

}
