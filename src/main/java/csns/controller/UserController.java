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
package csns.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Controller
@SessionAttributes("user")
public class UserController {

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "/user/search")
    public String search( @RequestParam(required = false) String term,
        ModelMap models )
    {
        List<User> users = null;
        if( StringUtils.hasText( term ) ) users = userDao.searchUsers( term );
        models.addAttribute( "users", users );
        return "user/search";
    }

    @RequestMapping(value = "/user/autocomplete")
    public String autocomplete( @RequestParam String term,
        HttpServletResponse response ) throws JSONException, IOException
    {
        JSONArray jsonArray = new JSONArray();
        List<User> users = userDao.searchUsersByPrefix( term );
        for( User user : users )
        {
            String label = user.isCinEncrypted() ? user.getName()
                : user.getCin() + " " + user.getName();

            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", user.getId().toString() );
            json.put( "value", user.getName() );
            json.put( "label", label );
            jsonArray.put( json );
        }

        response.setContentType( "application/json" );
        jsonArray.write( response.getWriter() );
        return null;
    }

}
