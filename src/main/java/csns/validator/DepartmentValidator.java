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
package csns.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;

@Component
public class DepartmentValidator implements Validator {

    @Autowired
    DepartmentDao departmentDao;

    @Override
    public boolean supports( Class<?> clazz )
    {
        return Department.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        Department department = (Department) target;
        Long id = department.getId();
        String name = department.getName();
        String abbreviation = department.getAbbreviation();

        if( !StringUtils.hasText( name ) )
            errors.rejectValue( "name", "error.department.name.empty" );
        else if( id == null
            && departmentDao.getDepartmentByName( name ) != null )
            errors.rejectValue( "name", "error.department.name.taken" );

        if( !StringUtils.hasText( abbreviation ) )
            errors.rejectValue( "abbreviation",
                "error.department.abbreviation.empty" );
        else if( !Pattern.matches( "[a-z]+", abbreviation ) )
            errors.rejectValue( "abbreviation",
                "error.department.abbreviation.invalid" );
        else if( id == null
            && departmentDao.getDepartment( abbreviation ) != null )
            errors.rejectValue( "abbreviation",
                "error.department.abbreviation.taken" );

        if( department.getAdministrators().size() == 0 )
            errors.rejectValue( "administrators",
                "error.department.administrators.empty" );
    }

}
