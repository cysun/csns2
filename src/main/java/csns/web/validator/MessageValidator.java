/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
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

import csns.model.core.AbstractMessage;
import csns.util.MyAntiSamy;

@Component
public class MessageValidator implements Validator {

    @Autowired
    private MyAntiSamy antiSamy;

    @Override
    public boolean supports( Class<?> clazz )
    {
        return AbstractMessage.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        AbstractMessage message = (AbstractMessage) target;

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "subject",
            "error.field.required" );

        String content = message.getContent();
        if( !StringUtils.hasText( content ) )
            errors.rejectValue( "content", "error.field.required" );
        else if( !antiSamy.validate( message.getContent() ) )
            errors.rejectValue( "content", "error.html.invalid" );
    }

}
