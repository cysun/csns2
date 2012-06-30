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
package csns.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import csns.model.core.User;

@Component
public class AuthenticationSuccessHandler extends
    SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess( HttpServletRequest request,
        HttpServletResponse response, Authentication authentication )
        throws ServletException, IOException
    {
        User user = (User) authentication.getPrincipal();
        logger.info( user.getUsername() + " signed in." );

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest( request, response );
        if( savedRequest != null )
        {
            super.onAuthenticationSuccess( request, response, authentication );
            return;
        }

        String targetUrl;
        if( user.isFaculty() || user.isInstructor() )
            targetUrl = "/instructor/section/list";
        else if( user.isDepartmentAdmin() )
            targetUrl = "/" + user.getDepartments( "ROLE_ADMIN" ).get( 0 )
                + "/admin/";
        else if( user.isAdmin() )
            targetUrl = "/admin/department/list";
        else
            targetUrl = "/student/section/list";

        getRedirectStrategy().sendRedirect( request, response, targetUrl );
    }

}
