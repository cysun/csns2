/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.dao.CourseDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.forum.dao.ForumDao;

@Controller
public class AutoCompleteController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ForumDao forumDao;

    @RequestMapping(value = "/autocomplete/user")
    public String users( @RequestParam String term, HttpServletResponse response )
        throws JSONException, IOException
    {
        JSONArray jsonArray = new JSONArray();
        List<User> users = userDao.searchUsersByPrefix( term, 10 );
        for( User user : users )
        {
            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", user.getId().toString() );
            json.put( "value", user.getName() );
            json.put( "label", user.getCin() + " " + user.getName() );
            if( user.getProfileThumbnail() != null )
                json.put( "thumbnail", user.getProfileThumbnail()
                    .getId()
                    .toString() );
            jsonArray.put( json );
        }

        response.setContentType( "application/json" );
        jsonArray.write( response.getWriter() );
        return null;
    }

    @RequestMapping(value = "/autocomplete/course")
    public String courses( @RequestParam String term,
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

    @RequestMapping(value = "/autocomplete/forum")
    public String forums( @RequestParam String term,
        HttpServletResponse response ) throws JSONException, IOException
    {
        JSONArray jsonArray = new JSONArray();
        List<Forum> forums = forumDao.searchForums( term, 10 );
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

}
