/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2016 Chengyu Sun (csun@calstatela.edu).
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.Persistence;

import org.hibernate.engine.jdbc.internal.DDLFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

/**
 * Generates DDL from JPA annotations.
 */
public class SchemaGeneration {

    public static void main( String args[] ) throws IOException
    {
        // Write the generated schema to a string
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> properties = new HashMap<>();
        properties.put( "javax.persistence.schema-generation.scripts.action",
            "create" );
        properties.put(
            "javax.persistence.schema-generation.scripts.create-target",
            stringWriter );
        Persistence.generateSchema( "csns2", properties );

        // If there is a command line argument, consider it the output file name
        BufferedWriter out = null;
        if( args.length > 0 )
            out = new BufferedWriter( new FileWriter( args[0] ) );

        // Use Hibernate's SQL formatter to format each statement
        Formatter formatter = new DDLFormatterImpl();
        Scanner scanner = new Scanner( stringWriter.toString() );
        while( scanner.hasNextLine() )
        {
            String line = formatter.format( scanner.nextLine() ) + ";";
            System.out.println( line );
            if( out != null )
            {
                out.write( line );
                out.newLine();
            }
        }
        scanner.close();
        if( out != null ) out.close();
    }

}
