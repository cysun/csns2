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
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Assignment;
import csns.model.core.ResourceType;

@Component
public class AssignmentValidator implements Validator {

    @Autowired
    ResourceValidator resourceValidator;

    @Override
    public boolean supports( Class<?> clazz )
    {
        return Assignment.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "name",
            "error.field.required" );

        Assignment assignment = (Assignment) target;
        if( !StringUtils.hasText( assignment.getAlias() ) )
            assignment.setAlias( assignment.getName() );

        if( !assignment.isOnline() )
        {
            errors.pushNestedPath( "description" );
            ValidationUtils.invokeValidator( resourceValidator,
                assignment.getDescription(), errors );
            errors.popNestedPath();
        }
    }

    public void validate( Assignment assignment, MultipartFile uploadedFile,
        Errors errors )
    {
        validate( assignment, errors );

        if( !assignment.isOnline()
            && assignment.getDescription().getType() == ResourceType.FILE
            && assignment.getDescription().getFile() == null
            && (uploadedFile == null || uploadedFile.isEmpty()) )
            errors.rejectValue( "description.file", "error.field.required" );
    }

}
