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
package csns.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import csns.model.core.User;

@Component
public class DefaultUrls {

    public String homeUrl( HttpServletRequest request )
    {
        String homeUrl = "/";

        Cookie cookie = WebUtils.getCookie( request, "default-dept" );
        if( cookie != null )
            homeUrl = "/department/" + cookie.getValue() + "/";

        return homeUrl;
    }

    public String homeUrl( User user )
    {
        String homeUrl = "/student/section/list";

        if( user.isFaculty() || user.isInstructor() )
            homeUrl = "/instructor/section/list";
        else if( user.isAdmin() || user.isDepartmentAdmin() )
            homeUrl = "/user/search";

        return homeUrl;
    }

}
