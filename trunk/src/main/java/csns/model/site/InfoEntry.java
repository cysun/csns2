/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.site;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class InfoEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String value;

    public InfoEntry()
    {
    }

    public InfoEntry( String name, String value )
    {
        this.name = name;
        this.value = value;
    }

    public InfoEntry clone()
    {
        InfoEntry newInfoEntry = new InfoEntry();
        newInfoEntry.name = name;
        newInfoEntry.value = value;
        return newInfoEntry;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

}
