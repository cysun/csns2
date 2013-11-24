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
package csns.importer.parser.csula;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import csns.importer.ImportedUser;
import csns.importer.parser.StudentsParser;
import csns.model.academics.Quarter;

@Component
public class StudentsParserImpl implements StudentsParser {

    /**
     * This parser handles data copy&pasted from an Excel file produced from GET
     * data. The format is expected to be <quarter cin first_name last_name ...>
     * where quarter is a 4-digit code. Currently we only process the first four
     * fields.
     */
    @Override
    public List<ImportedUser> parse( String text )
    {
        List<ImportedUser> students = new ArrayList<ImportedUser>();

        Scanner scanner = new Scanner( text );
        while( scanner.hasNextLine() )
        {
            ImportedUser student = parseLine( scanner.nextLine() );
            if( student != null ) students.add( student );
        }
        scanner.close();

        return students;
    }

    public ImportedUser parseLine( String line )
    {
        ImportedUser student = null;

        String tokens[] = line.trim().split( "\t" );
        if( tokens.length >= 4 && isQuarter( tokens[0] ) && isCin( tokens[1] ) )
        {
            student = new ImportedUser();
            // GET quarter code is different from CSNS quarter code
            student.setQuarter( new Quarter(
                Integer.parseInt( tokens[0] ) - 1000 ) );
            student.setCin( tokens[1] );
            student.setFirstName( tokens[2] );
            student.setLastName( tokens[3] );
        }

        return student;
    }

    private boolean isQuarter( String s )
    {
        return s.matches( "\\d{4}" );
    }

    public boolean isCin( String s )
    {
        return s.matches( "\\d{9}" );
    }

}
