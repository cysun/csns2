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
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;

import csns.helper.GradeSheet;
import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Enrollment;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.QuarterDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.web.editor.CoursePropertyEditor;
import csns.web.editor.QuarterPropertyEditor;

@Controller
@SessionAttributes("importer")
public class DepartmentSectionController {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private QuarterDao quarterDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private WebApplicationContext context;

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Quarter.class,
            (QuarterPropertyEditor) context.getBean( "quarterPropertyEditor" ) );
        binder.registerCustomEditor( Course.class,
            (CoursePropertyEditor) context.getBean( "coursePropertyEditor" ) );
    }

    @RequestMapping("/department/{dept}/sections")
    public String sections( @PathVariable String dept,
        @RequestParam(required = false) Quarter quarter, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        List<Quarter> quarters = quarterDao.getSectionQuarters( department );

        Quarter currentQuarter = new Quarter();
        if( quarter == null ) quarter = currentQuarter;
        if( !quarters.contains( currentQuarter ) )
            quarters.add( 0, currentQuarter );
        Quarter nextQuarter = currentQuarter.next();
        if( !quarters.contains( nextQuarter ) ) quarters.add( 0, nextQuarter );

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
