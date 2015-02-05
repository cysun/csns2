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
package csns.web.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import csns.security.SecurityUtils;

public class ExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    public ModelAndView resolveException( HttpServletRequest request,
        HttpServletResponse response, Object handler, Exception exception )
    {
        if( !exception.getClass().getName().endsWith( "AccessDeniedException" ) )
        {
            String username = SecurityUtils.isAnonymous() ? "guest"
                : SecurityUtils.getUser().getUsername();
            logger.error( "Exception caused by " + username, exception );
        }

        return super.resolveException( request, response, handler, exception );
    }

}
