/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015, Chengyu Sun (csun@calstatela.edu).
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

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;

public class AuthenticationFailureHandler extends
    SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure( HttpServletRequest request,
        HttpServletResponse response, AuthenticationException exception )
        throws IOException, ServletException
    {
        String username = request.getParameter( "j_username" );
        if( StringUtils.hasText( username ) )
            logger.info( "Failed login attempt of " + username + " from "
                + request.getRemoteAddr() );

        super.onAuthenticationFailure( request, response, exception );
    }

}
