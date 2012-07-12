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

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;

import csns.model.academics.Course;
import csns.model.academics.dao.CourseDao;
import csns.model.core.User;
import csns.web.editor.UserPropertyEditor;
import csns.web.validator.CourseValidator;

@Controller
@SessionAttributes("course")
public class CourseController {

    @Autowired
    CourseDao courseDao;

    @Autowired
    CourseValidator courseValidator;

    @Autowired
    WebApplicationContext context;

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( User.class,
            (UserPropertyEditor) context.getBean( "userPropertyEditor" ) );
    }

    @RequestMapping(value = "/course/search")
    public String search( @RequestParam(required = false) String term,
        ModelMap models )
    {
        List<Course> courses = null;
        if( StringUtils.hasText( term ) )
            courses = courseDao.searchCourses( term, -1 );
        models.addAttribute( "courses", courses );
        return "course/search";
    }

    @RequestMapping(value = "/course/autocomplete")
    public String autocomplete( @RequestParam String term,
        HttpServletResponse response ) throws JSONException, IOException
    {
        JSONArray jsonArray = new JSONArray();
        List<Course> courses = courseDao.searchCourses( term, 10 );
        for( Course course : courses )
        {
            String label = course.getCode() + " " + course.getName();

            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", course.getId().toString() );
            json.put( "value", course.getCode() );
            json.put( "label", label );
            jsonArray.put( json );
        }

        response.setContentType( "application/json" );
        jsonArray.write( response.getWriter() );
        return null;
    }

    @RequestMapping(value = "/course/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "course", courseDao.getCourse( id ) );
        return "course/view";
    }

    @RequestMapping(value = "/course/add", method = RequestMethod.GET)
    public String add( ModelMap models )
    {
        models.put( "course", new Course() );
        return "course/add";
    }

    @RequestMapping(value = "/course/add", method = RequestMethod.POST)
    public String add( @ModelAttribute Course course,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        courseValidator.validate( course, bindingResult );
        if( bindingResult.hasErrors() ) return "course/add";

        course = courseDao.saveCourse( course );
        sessionStatus.setComplete();
        return "redirect:view?id=" + course.getId();
    }

    @RequestMapping(value = "/course/edit", method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "course", courseDao.getCourse( id ) );
        return "course/edit";
    }

    @RequestMapping(value = "/course/edit", method = RequestMethod.POST)
    public String edit( @ModelAttribute Course course,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        courseValidator.validate( course, bindingResult );
        if( bindingResult.hasErrors() ) return "course/edt";

        course = courseDao.saveCourse( course );
        sessionStatus.setComplete();
        return "redirect:view?id=" + course.getId();
    }

    @RequestMapping(value = "/course/delete")
    public String delete( @RequestParam Long id )
    {
        Course course = courseDao.getCourse( id );
        course.setObsolete( true );
        courseDao.saveCourse( course );
        return "redirect:search";
    }

}
