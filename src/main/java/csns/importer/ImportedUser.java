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

/**
 * This class holds all the fields that could potentially be imported. Note that
 * not all fields are used by every importer.
 */
public class ImportedUser {

    String cin;

    String firstName;

    String lastName;

    String middleName;

    boolean accountCreated;

    boolean addedToSection;

    public ImportedUser()
    {
        accountCreated = false;
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

    public boolean isAccountCreated()
    {
        return accountCreated;
    }

    public void setAccountCreated( boolean accountCreated )
    {
        this.accountCreated = accountCreated;
    }

    public boolean isAddedToSection()
    {
        return addedToSection;
    }

    public void setAddedToSection( boolean addedToSection )
    {
        this.addedToSection = addedToSection;
    }

}
