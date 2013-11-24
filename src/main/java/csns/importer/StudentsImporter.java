/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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
package csns.importer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import csns.importer.parser.StudentsParser;
import csns.model.academics.Department;
import csns.model.academics.Standing;

@Component
@Scope("prototype")
public class StudentsImporter {

    @Autowired
    StudentsParser usersParser;

    Department department;

    Standing standing;

    String text;

    List<ImportedUser> importedStudents;

    public StudentsImporter()
    {
        importedStudents = new ArrayList<ImportedUser>();
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public Standing getStanding()
    {
        return standing;
    }

    public void setStanding( Standing standing )
    {
        this.standing = standing;
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        if( StringUtils.hasText( text ) )
        {
            this.text = text;
            importedStudents = usersParser.parse( text );
        }
    }

    public List<ImportedUser> getImportedStudents()
    {
        return importedStudents;
    }

    public void setImportedStudents( List<ImportedUser> importedStudents )
    {
        this.importedStudents = importedStudents;
    }

}
