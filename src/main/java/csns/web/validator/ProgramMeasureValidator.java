/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Chengyu Sun (csun@calstatela.edu).
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
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import csns.model.assessment.ProgramMeasure;
import csns.model.core.ResourceType;

@Component
public class ProgramMeasureValidator implements Validator {

    @Autowired
    ResourceValidator resourceValidator;

    @Override
    public boolean supports( Class<?> clazz )
    {
        return ProgramMeasure.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "type",
            "error.field.required" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "name",
            "error.field.required" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "description",
            "error.field.required" );

        ProgramMeasure measure = (ProgramMeasure) target;

        errors.pushNestedPath( "resource" );
        ValidationUtils.invokeValidator( resourceValidator,
            measure.getResource(), errors );
        errors.popNestedPath();
    }

    public void validate( ProgramMeasure measure, MultipartFile uploadedFile,
        Errors errors )
    {
        validate( measure, errors );

        if( measure.getResource().getType() == ResourceType.FILE
            && measure.getResource().getFile() == null
            && (uploadedFile == null || uploadedFile.isEmpty()) )
            errors.rejectValue( "resource.file", "error.field.required" );
    }

}
