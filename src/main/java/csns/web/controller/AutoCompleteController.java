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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.forum.dao.ForumDao;
import csns.model.survey.Survey;
import csns.model.survey.dao.SurveyDao;

@Controller
public class AutoCompleteController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ForumDao forumDao;

    @Autowired
    private SurveyDao surveyDao;

    @RequestMapping(value = "/autocomplete/user")
    public String users( @RequestParam("term") String text,
        HttpServletResponse response ) throws JSONException, IOException
    {
        JSONArray jsonArray = new JSONArray();
        List<User> users = userDao.searchUsersByPrefix( text, 10 );
        for( User user : users )
        {
            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", user.getId().toString() );
            json.put( "value", user.getName() );
            json.put( "label", user.getCin() + " " + user.getName() );
            if( user.getProfileThumbnail() != null ) json.put( "thumbnail",
                user.getProfileThumbnail().getId().toString() );
            jsonArray.put( json );
        }

        response.setContentType( "application/json" );
        jsonArray.write( response.getWriter() );
        return null;
    }

    @RequestMapping(value = "/autocomplete/course")
    public String courses( @RequestParam("term") String text,
        HttpServletResponse response ) throws JSONException, IOException
    {
        JSONArray jsonArray = new JSONArray();
        List<Course> courses = courseDao.searchCourses( text, false, 10 );
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

    @RequestMapping(value = "/autocomplete/forum")
    public String forums( @RequestParam("term") String text,
        HttpServletResponse response ) throws JSONException, IOException
    {
        JSONArray jsonArray = new JSONArray();
        List<Forum> forums = forumDao.searchForums( text, 10 );
        for( Forum forum : forums )
        {
            String label;
            if( forum.getCourse() != null )
                label = forum.getCourse().getCode() + " "
                    + forum.getCourse().getName();
            else if( forum.getDepartment() != null )
                label = forum.getDepartment().getAbbreviation().toUpperCase()
                    + " - " + forum.getName();
            else
                label = forum.getName();

            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", forum.getId().toString() );
            json.put( "value", label );
            json.put( "label", label );
            jsonArray.put( json );
        }

        response.setContentType( "application/json" );
        jsonArray.write( response.getWriter() );
        return null;
    }

    @RequestMapping(value = "/department/{dept}/survey/autocomplete")
    public String autocomplete( @PathVariable String dept,
        @RequestParam("term") String text, HttpServletResponse response )
        throws JSONException, IOException
    {
        Department department = departmentDao.getDepartment( dept );
        JSONArray jsonArray = new JSONArray();
        List<Survey> surveys = surveyDao.searchSurveysByPrefix( department,
            text, 20 );
        for( Survey survey : surveys )
        {
            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", survey.getId().toString() );
            json.put( "value", survey.getName() );
            json.put( "label", survey.getName() );
            jsonArray.put( json );
        }

        response.setContentType( "application/json" );
        jsonArray.write( response.getWriter() );
        return null;
    }

}
