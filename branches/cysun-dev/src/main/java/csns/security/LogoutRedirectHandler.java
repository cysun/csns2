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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import csns.model.core.User;
import csns.util.DefaultUrls;

@Component
public class LogoutRedirectHandler implements LogoutSuccessHandler {

    @Autowired
    DefaultUrls defaultUrls;

    private final static Logger logger = LoggerFactory.getLogger( LogoutRedirectHandler.class );

    @Override
    public void onLogoutSuccess( HttpServletRequest request,
        HttpServletResponse response, Authentication authentication )
        throws IOException, ServletException
    {
        // authentication could be null if the session already expired or the
        // user clicked the logout link twice.
        if( authentication != null )
        {
            User user = (User) authentication.getPrincipal();
            logger.info( user.getUsername() + " signed out." );
        }

        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl( defaultUrls.anonymousHomeUrl( request ) );
        logoutSuccessHandler.onLogoutSuccess( request, response, authentication );
    }

}
