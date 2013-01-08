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
package csns.importer.parser.csula;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import csns.importer.ImportedUser;
import csns.importer.parser.RosterParser;

@Component
public class RosterParserImpl implements RosterParser {

    @Override
    public List<ImportedUser> parse( String text )
    {
        Scanner scanner = new Scanner( text );
        scanner.useDelimiter( "\\s*\\r\\n|\\s*\\r|\\s*\\n" );
        String first = scanner.next().trim();
        scanner.close();

        return first.equals( "1" ) ? parse1( text ) : parse2( text );
    }

    /**
     * This parser handles the format under CSULA Baseline -> CSULA Student
     * Records -> Class Roster on GET.
     */
    public List<ImportedUser> parse1( String text )
    {
        List<ImportedUser> students = new ArrayList<ImportedUser>();

        Scanner scanner = new Scanner( text );
        scanner.useDelimiter( "\\s*\\r\\n|\\s*\\r|\\s*\\n" );
        while( scanner.hasNext() )
        {
            String cin = scanner.next();
            while( !cin.matches( "\\d{9}" ) && scanner.hasNext() )
                cin = scanner.next();

            if( cin.matches( "\\d{9}" ) )
            {
                ImportedUser student = new ImportedUser();
                student.setCin( cin );
                student.setName( scanner.next() );
                students.add( student );
            }
        }
        scanner.close();

        return students;
    }

    /**
     * This parser handles the format under Self Service -> Faculty Center -> My
     * Schedule on GET.
     */
    public List<ImportedUser> parse2( String text )
    {
        List<ImportedUser> students = new ArrayList<ImportedUser>();

        Scanner scanner = new Scanner( text );
        scanner.useDelimiter( "\\s*\\r\\n|\\s*\\r|\\s*\\n" );
        while( scanner.hasNext() )
        {
            ImportedUser student = new ImportedUser();
            student.setName( scanner.next() );
            student.setCin( scanner.next() );
            students.add( student );
            if( scanner.hasNext() ) scanner.next();
            if( scanner.hasNext() ) scanner.next();
            if( scanner.hasNext() ) scanner.next();
        }
        scanner.close();

        return students;
    }

}
