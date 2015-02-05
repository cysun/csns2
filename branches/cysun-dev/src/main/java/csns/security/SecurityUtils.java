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

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import csns.model.core.User;

public class SecurityUtils {

    private static AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    /**
     * <security:http> adds an AnonymousAuthenticationFilter which creates an
     * Authentication token for anonymous users. The problem is that
     * Authentication.isAuthenticated() will then return true even for anonymous
     * users, so we have to use a AuthenticationTrustResolver to check for
     * anonymous/authenticated.
     */
    public static boolean isAnonymous()
    {
        return authenticationTrustResolver.isAnonymous( SecurityContextHolder.getContext()
            .getAuthentication() );
    }

    public static boolean isAuthenticated()
    {
        return !isAnonymous();
    }

    public static User getUser()
    {
        return isAuthenticated() ? (User) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal() : null;
    }

}
