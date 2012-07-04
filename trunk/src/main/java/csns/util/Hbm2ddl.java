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

import java.util.HashMap;

import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * According to Hibernate documentation, both Configuration and
 * Ejb3Configuration are deprecated and will be removed in Hibernate 5.
 * Hopefully by then SchemaExport can be initialized from persistence.xml
 * instead of hibernate.cfg.xml or hibernate.properties, and if so, we can
 * remove this class and run SchemaExport directly.
 */
@SuppressWarnings("deprecation")
public class Hbm2ddl {

    public static void main( String args[] )
    {
        if( args.length == 0 )
        {
            System.err.println( "java Hbm2ddl <outputFile>" );
            return;
        }

        System.out.print( "Export DDL to " + args[0] + " ... " );

        Configuration cfg = (new Ejb3Configuration()).configure( "csns2",
            new HashMap<String, Object>() ).getHibernateConfiguration();

        SchemaExport schemaExport = new SchemaExport( cfg );
        schemaExport.setOutputFile( args[0] )
            .setDelimiter( ";" )
            .setFormat( true )
            .setHaltOnError( true );

        // . output script to console (and file if outputFile is set): true
        // . export to database: false
        // . only drop the tables: false
        // . only create the tables: true
        schemaExport.execute( true, false, false, true );

        System.out.println( "Done." );
    }

}
