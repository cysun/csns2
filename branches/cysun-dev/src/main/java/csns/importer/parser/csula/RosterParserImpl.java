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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import org.springframework.stereotype.Component;

import csns.importer.ImportedUser;
import csns.importer.parser.RosterParser;

@Component
public class RosterParserImpl implements RosterParser {

    private Set<String> programs;

    private Set<String> degrees;

    private Set<String> standings;

    private Set<String> grades;

    public RosterParserImpl()
    {
        String p[] = { "ALB", "ALG", "BEB", "BEG", "EDB", "EDG", "ETB", "ETG",
            "HHSB", "HHSG", "NSSB", "NSSG", "XEB", "XEG", "UNB", "UNG" };
        programs = new HashSet<String>( Arrays.asList( p ) );

        String d[] = { "BS", "BA", "BM", "BP", "BXG", "MA", "MS", "MBA", "MM",
            "UNB", "UNX" };
        degrees = new HashSet<String>( Arrays.asList( d ) );

        String s[] = { "B1", "B2", "B3", "B4", "G1", "G2", "G3" };
        standings = new HashSet<String>( Arrays.asList( s ) );

        String g[] = { "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D",
            "D-", "F", "CR", "NC", "I", "IC", "RD", "RP", "W", "WU" };
        grades = new HashSet<String>( Arrays.asList( g ) );
    }

    @Override
    public List<ImportedUser> parse( String text )
    {
        Scanner scanner = new Scanner( text );
        scanner.useDelimiter( "\\s+|\\r\\n|\\r|\\n" );
        String first = scanner.next();
        scanner.close();

        return first.equals( "1" ) ? parse1( text ) : parse2( text );
    }

    /**
     * This parser handles the format under CSULA Baseline -> CSULA Student
     * Records -> Class Roster on GET. A sample record is as follows:
     * "1 123456789 Doe,John M 3.00 ETG CS MS G1". Note that some fields like
     * middle name and units may not be present, and some people's last name has
     * space in it.
     */
    private List<ImportedUser> parse1( String text )
    {
        List<ImportedUser> students = new ArrayList<ImportedUser>();

        Scanner scanner = new Scanner( text );
        scanner.useDelimiter( "\\s+|\\r\\n|\\r|\\n" );
        while( scanner.hasNext() )
        {
            String cin = scanner.next();
            if( !isCin( cin ) ) continue;

            String name = "";
            boolean nameFound = false;
            while( scanner.hasNext() )
            {
                String token = scanner.next();
                name += token + " ";
                if( token.matches( ".+,.*" ) )
                {
                    if( token.endsWith( "," ) && scanner.hasNext() )
                        name += scanner.next();
                    nameFound = true;
                    break;
                }
            }

            String grade = null;
            boolean gradeFound = false;
            boolean unitsFound = false;
            while( nameFound && scanner.hasNext() )
            {
                String token = scanner.next();
                if( isUnits( token ) )
                {
                    unitsFound = true;
                    continue;
                }
                if( isGrade( token ) )
                {
                    if( unitsFound ) // this must be a grade
                    {
                        grade = token;
                        gradeFound = true;
                        break;
                    }
                    else
                    // this could be a grade or a middle name
                    {
                        grade = token;
                        continue;
                    }
                }
                if( isProgram( token ) )
                {
                    if( grade != null ) gradeFound = true;
                    break;
                }

                name += token + " ";
            }

            if( nameFound )
            {
                ImportedUser student = new ImportedUser();
                student.setCin( cin );
                student.setName( name );
                if( gradeFound ) student.setGrade( grade );
                students.add( student );
            }
        }
        scanner.close();

        return students;
    }

    /**
     * This parser handles the format under Self Service -> Faculty Center -> My
     * Schedule on GET. A sample record is as follows:
     * "Doe,John M 302043188 3.00 Engr, Comp Sci, & Tech  CS MS". Again, not all
     * fields may be present.
     */
    private List<ImportedUser> parse2( String text )
    {
        List<ImportedUser> students = new ArrayList<ImportedUser>();
        Stack<String> stack = new Stack<String>();

        Scanner scanner = new Scanner( text );
        scanner.useDelimiter( "\\s+|\\r\\n|\\r|\\n" );
        while( scanner.hasNext() )
        {
            String name = "";
            do
            {
                String token = scanner.next();
                if( !isName( token ) )
                    stack.push( token );
                else
                {
                    name = token;
                    while( !stack.isEmpty() && !isDegree( stack.peek() ) )
                        name = stack.pop() + " " + name;
                    break;
                }
            } while( scanner.hasNext() );

            String cin = "";
            boolean cinFound = false;
            while( scanner.hasNext() )
            {
                cin = scanner.next();
                if( isCin( cin ) )
                {
                    cinFound = true;
                    break;
                }
                else
                    name += " " + cin;
            }

            if( cinFound )
            {
                ImportedUser student = new ImportedUser();
                student.setCin( cin );
                student.setName( name );
                students.add( student );
            }
        }
        scanner.close();

        return students;
    }

    public boolean isCin( String s )
    {
        return s.matches( "\\d{9}" );
    }

    public boolean isName( String s )
    {
        return s.matches( ".+,.+" );
    }

    public boolean isUnits( String s )
    {
        return s.matches( "\\d\\.00" );
    }

    public boolean isProgram( String s )
    {
        return programs.contains( s );
    }

    public boolean isDegree( String s )
    {
        return degrees.contains( s );
    }

    public boolean isStanding( String s )
    {
        return standings.contains( s );
    }

    public boolean isGrade( String s )
    {
        return grades.contains( s );
    }

}
