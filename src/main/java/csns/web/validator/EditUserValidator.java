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
package csns.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import csns.model.core.User;

/**
 * This validator is for account profile and editing users in user management.
 */
@Component
public class EditUserValidator extends AddUserValidator {

    @Override
    public void validate( Object target, Errors errors )
    {
        super.validate( target, errors );

        User user = (User) target;
        Long id = user.getId();

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "username",
            "error.field.required" );

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "primaryEmail",
            "error.field.required" );

        String username = user.getUsername();
        if( StringUtils.hasText( username ) )
        {
            User u = userDao.getUserByUsername( username );
            if( u != null && !u.getId().equals( id ) )
                errors.rejectValue( "username", "error.user.username.taken" );
        }

        String password1 = user.getPassword1();
        if( StringUtils.hasText( password1 ) )
        {
            if( password1.length() < 6 )
                errors.rejectValue( "password1", "error.user.password.short" );
            if( !password1.equals( user.getPassword2() ) )
                errors.rejectValue( "password2", "error.user.password.notmatch" );
        }
    }

}
