/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import csns.model.core.User;
import csns.security.SecurityUtils;

@Component
public class DefaultUrls {

    public String homeUrl( HttpServletRequest request )
    {
        return SecurityUtils.isAuthenticated() ? userHomeUrl( request )
            : anonymousHomeUrl( request );
    }

    public String userHomeUrl( HttpServletRequest request )
    {
        User user = SecurityUtils.getUser();
        if( user.isSysadmin() ) return "/admin/department/list";
        if( user.isTemporary() ) return "/register";

        Cookie cookie = WebUtils.getCookie( request, "default-home" );
        if( cookie != null ) return cookie.getValue();

        String homeUrl;
        if( user.isAdmin() )
            homeUrl = "/user/search";
        else if( user.isFaculty() || user.isInstructor() )
            homeUrl = "/section/taught";
        else if( user.isEvaluator() )
            homeUrl = "/section/evaluated";
        else
            homeUrl = "/section/taken";

        return homeUrl;
    }

    public String anonymousHomeUrl( HttpServletRequest request )
    {
        Cookie cookie = WebUtils.getCookie( request, "default-dept" );
        return cookie != null ? "/department/" + cookie.getValue() + "/" : "/";
    }

}
