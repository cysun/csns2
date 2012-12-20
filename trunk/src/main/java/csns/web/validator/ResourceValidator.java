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
import org.springframework.validation.Validator;

import csns.model.core.Resource;
import csns.model.core.ResourceType;

@Component
public class ResourceValidator implements Validator {

    @Override
    public boolean supports( Class<?> clazz )
    {
        return Resource.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        Resource resource = (Resource) target;

        if( resource.getType() != ResourceType.NONE )
        {
            switch( resource.getType() )
            {
                case TEXT:
                    if( !StringUtils.hasText( resource.getText() ) )
                        errors.rejectValue( "text", "error.field.required" );
                    break;

                case URL:
                    if( !StringUtils.hasText( resource.getUrl() ) )
                        errors.rejectValue( "url", "error.field.required" );
                    break;

                default:
                    // file upload is not validated.
            }
        }
    }

}
