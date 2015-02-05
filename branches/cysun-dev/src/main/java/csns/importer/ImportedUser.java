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
package csns.importer;

import csns.model.academics.Quarter;

/**
 * This class holds all the fields that could potentially be imported. Note that
 * not all fields are used by every importer.
 */
public class ImportedUser {

    String cin;

    String firstName, lastName, middleName;

    String grade, oldGrade;

    Quarter quarter;

    boolean isNewAccount;

    boolean isNewEnrollment;

    public ImportedUser()
    {
        isNewAccount = false;
        isNewEnrollment = false;
    }

    public void setName( String name )
    {
        name = name.trim();
        int index1 = name.indexOf( ',' );
        int index2 = name.indexOf( ' ', index1 );

        lastName = name.substring( 0, index1 );
        if( index2 > 0 )
        {
            firstName = name.substring( index1 + 1, index2 );
            middleName = name.substring( index2 + 1 );
        }
        else
        {
            firstName = name.substring( index1 + 1 );
            middleName = "";
        }
    }

    public String getCin()
    {
        return cin;
    }

    public void setCin( String cin )
    {
        this.cin = cin;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName( String middleName )
    {
        this.middleName = middleName;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade( String grade )
    {
        this.grade = grade;
    }

    public String getOldGrade()
    {
        return oldGrade;
    }

    public void setOldGrade( String oldGrade )
    {
        this.oldGrade = oldGrade;
    }

    public Quarter getQuarter()
    {
        return quarter;
    }

    public void setQuarter( Quarter quarter )
    {
        this.quarter = quarter;
    }

    public boolean isNewAccount()
    {
        return isNewAccount;
    }

    public void setNewAccount( boolean isNewAccount )
    {
        this.isNewAccount = isNewAccount;
    }

    public boolean isNewEnrollment()
    {
        return isNewEnrollment;
    }

    public void setNewEnrollment( boolean isNewEnrollment )
    {
        this.isNewEnrollment = isNewEnrollment;
    }

}
