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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import csns.model.academics.Course;
import csns.model.academics.dao.CourseDao;

@Component
public class CourseValidator implements Validator {

    @Autowired
    CourseDao courseDao;

    @Override
    public boolean supports( Class<?> clazz )
    {
        return Course.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        Course course = (Course) target;

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "name",
            "error.field.required" );

        String code = course.getCode();
        if( !StringUtils.hasText( code ) )
            errors.rejectValue( "code", "error.field.required" );
        else if( !Pattern.matches( "[A-Z]+\\d+[A-Z]?", code ) )
            errors.rejectValue( "code", "error.course.code.invalid" );
        else
        {
            Course c = courseDao.getCourse( code );
            if( c != null && !c.getId().equals( course.getId() ) )
                errors.rejectValue( "code", "error.course.exists" );
        }
    }

}
