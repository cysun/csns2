/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013-2017, Chengyu Sun (csun@calstatela.edu).
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
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import csns.importer.ImportedUser;
import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Enrollment;
import csns.model.academics.Term;
import csns.model.academics.Section;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.GradeDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.util.ExcelReader;
import csns.web.editor.CoursePropertyEditor;
import csns.web.editor.TermPropertyEditor;
import csns.web.editor.UserPropertyEditor;

@Controller
@SessionAttributes("importedUsers")
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

    private static final Logger logger = LoggerFactory
        .getLogger( DepartmentSectionControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Term.class,
            (TermPropertyEditor) context.getBean( "termPropertyEditor" ) );
        binder.registerCustomEditor( User.class,
            (UserPropertyEditor) context.getBean( "userPropertyEditor" ) );
        binder.registerCustomEditor( Course.class,
            (CoursePropertyEditor) context.getBean( "coursePropertyEditor" ) );
    }

    @RequestMapping("/department/{dept}/section/import0")
    public String import0( @PathVariable String dept, @RequestParam Term term,
        ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        List<Course> courses = new ArrayList<Course>();
        courses.addAll( department.getUndergraduateCourses() );
        courses.addAll( department.getGraduateCourses() );
        List<User> instructors = new ArrayList<User>();
        instructors.addAll( department.getFaculty() );
        instructors.addAll( department.getInstructors() );

        models.put( "department", department );
        models.put( "term", term );
        models.put( "courses", courses );
        models.put( "instructors", instructors );
        return "department/section/import0";
    }

    @RequestMapping("/department/{dept}/section/import1")
    public String import1( @PathVariable String dept, @RequestParam Term term,
        @RequestParam Course course, @RequestParam User instructor,
        ModelMap models )
    {
        models.put( "department", departmentDao.getDepartment( dept ) );
        models.put( "term", term );
        models.put( "course", course );
        models.put( "instructor", instructor );
        models.put( "sections",
            sectionDao.getSectionsByInstructor( instructor, term, course ) );
        return "department/section/import1";
    }

    @RequestMapping(value = "/department/{dept}/section/import2",
        method = RequestMethod.POST)
    public String import2( @PathVariable String dept, @RequestParam Term term,
        @RequestParam Course course, @RequestParam User instructor,
        @RequestParam int number,
        @RequestParam(value = "file") MultipartFile uploadedFile,
        ModelMap models ) throws IOException
    {
        List<ImportedUser> importedUsers = new ArrayList<ImportedUser>();
        ExcelReader excelReader = new ExcelReader(
            uploadedFile.getInputStream() );
        while( excelReader.next() )
        {
            ImportedUser importedUser = new ImportedUser();
            importedUser.setCin( excelReader.get( "ID" ) );
            importedUser.setName( excelReader.get( "Name" ) );
            importedUser.setGrade( excelReader.get( "Official Grade" ) );
            importedUsers.add( importedUser );
        }
        excelReader.close();

        if( number > 0 )
        {
            Section section = sectionDao.getSection( term, course, number );
            for( ImportedUser importedUser : importedUsers )
            {
                User user = userDao.getUserByCin( importedUser.getCin() );
                if( user == null )
                {
                    importedUser.setNewAccount( true );
                    importedUser.setNewEnrollment( true );
                }
                else
                {
                    Enrollment enrollment = section.getEnrollment( user );
                    if( enrollment == null )
                        importedUser.setNewEnrollment( true );
                    else if( enrollment.getGrade() != null ) importedUser
                        .setOldGrade( enrollment.getGrade().getSymbol() );
                }
            }
        }

        models.put( "department", departmentDao.getDepartment( dept ) );
        models.put( "term", term );
        models.put( "course", course );
        models.put( "instructor", instructor );
        models.put( "number", number );
        models.put( "importedUsers", importedUsers );
        return "department/section/import2";
    }

    @RequestMapping("/department/{dept}/section/import-cancel")
    @ResponseBody
    public void cancelImport( SessionStatus sessionStatus )
    {
        sessionStatus.setComplete();
        logger.debug( "Import canceled" );
    }

    @RequestMapping("/department/{dept}/section/import-confirm")
    @ResponseBody
    public Long confirmImport(
        @ModelAttribute(
            name = "importedUsers") List<ImportedUser> importedUsers,
        @PathVariable String dept, @RequestParam Term term,
        @RequestParam Course course, @RequestParam User instructor,
        @RequestParam int number, SessionStatus sessionStatus )
    {
        Section section = number > 0
            ? sectionDao.getSection( term, course, number )
            : sectionDao.addSection( term, course, instructor );

        int count = 0;
        for( ImportedUser importedUser : importedUsers )
        {
            String cin = importedUser.getCin();
            User user = userDao.getUserByCin( cin );
            if( user == null )
            {
                user = new User();
                user.setCin( cin );
                user.setLastName( importedUser.getLastName() );
                user.setFirstName( importedUser.getFirstName() );
                user.setMiddleName( importedUser.getMiddleName() );
                user.setUsername( cin );
                String password = passwordEncoder.encodePassword( cin, null );
                user.setPassword( password );
                user.setPrimaryEmail( cin + "@localhost" );
                user.setTemporary( true );
                user = userDao.saveUser( user );
            }
            Enrollment enrollment = section.getEnrollment( user );
            if( enrollment == null )
                enrollment = new Enrollment( section, user );
            if( importedUser.getGrade() != null
                && (enrollment.getGrade() == null || !enrollment.getGrade()
                    .getSymbol()
                    .equalsIgnoreCase( importedUser.getGrade() )) )
            {
                enrollment
                    .setGrade( gradeDao.getGrade( importedUser.getGrade() ) );
                enrollmentDao.saveEnrollment( enrollment );
                ++count;
            }
        }

        logger.info( SecurityUtils.getUser().getUsername() + " imported "
            + count + " records into section " + section.getId() );

        sessionStatus.setComplete();
        return section.getId();
    }

}
