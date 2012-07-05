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
package csns.util;

import java.util.Properties;

/**
 * The sole purpose of this class is to create a bean that passes on some
 * properties set in build.properties to other beans.
 */
public class ApplicationProperties {

    private Properties properties;

    public ApplicationProperties()
    {
    }

    public String getProperty( String name )
    {
        return properties.getProperty( name );
    }

    public void setProperties( Properties properties )
    {
        this.properties = properties;
    }

}
