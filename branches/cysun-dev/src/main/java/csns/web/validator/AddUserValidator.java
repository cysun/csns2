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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import csns.model.core.User;
import csns.model.core.dao.UserDao;

/**
 * This validator is for adding new users in user management.
 */
@Component
public class AddUserValidator implements Validator {

    @Autowired
    UserDao userDao;

    @Override
    public boolean supports( Class<?> clazz )
    {
        return User.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        User user = (User) target;
        Long id = user.getId();

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "firstName",
            "error.field.required" );

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "lastName",
            "error.field.required" );

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "cin",
            "error.field.required" );

        String cin = user.getCin();
        if( StringUtils.hasText( cin ) )
        {
            User u = userDao.getUserByCin( cin );
            if( u != null && !u.getId().equals( id ) )
                errors.rejectValue( "cin", "error.user.cin.taken" );
        }

        String primaryEmail = user.getPrimaryEmail();
        if( StringUtils.hasText( primaryEmail ) )
        {
            User u = userDao.getUserByEmail( primaryEmail );
            if( u != null && !u.getId().equals( id ) )
                errors.rejectValue( "primaryEmail", "error.user.email.taken" );
        }
    }

}
