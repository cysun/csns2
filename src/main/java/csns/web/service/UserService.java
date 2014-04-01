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
package csns.web.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Controller
@SuppressWarnings("deprecation")
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger( UserService.class );

    @RequestMapping("/service/user/login")
    public String login( @RequestParam String username,
        @RequestParam String password, ModelMap models )
    {
        User user = userDao.getUserByUsername( username );
        if( user == null
            || !passwordEncoder.encodePassword( password, null ).equals(
                user.getPassword() ) )
        {
            logger.info( "Username or password does not match for " + username );
            user = null;
        }
        else
        {
            logger.info( "Credentials verified for " + username );
            if( user.getAccessKey() == null )
            {
                user.setAccessKey( UUID.randomUUID().toString() );
                user = userDao.saveUser( user );
                logger.info( "Access key generated for " + username );
            }
        }
        models.put( "user", user );
        return "jsonView";
    }

}
