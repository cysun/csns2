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
package csns.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import csns.security.SecurityUtils;

@Component
public class RegistrationFilter extends OncePerRequestFilter {

    private boolean isPassThrough( String path )
    {
        return path.startsWith( "/img/" ) || path.startsWith( "/css/" )
            || path.startsWith( "/js/" ) || path.equals( "/favicon.ico" )
            || path.equals( "/register" );
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain )
        throws ServletException, IOException
    {
        String contextPath = request.getContextPath();
        String path = request.getRequestURI().substring( contextPath.length() );

        if( SecurityUtils.isAuthenticated()
            && SecurityUtils.getUser().isTemporary() && !isPassThrough( path ) )
        {
            response.sendRedirect( contextPath + "/register" );
            return;
        }

        filterChain.doFilter( request, response );
    }

}
