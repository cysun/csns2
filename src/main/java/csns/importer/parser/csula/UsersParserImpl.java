/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Chengyu Sun (csun@calstatela.edu).
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
import csns.importer.parser.UserListParser;

/**
 * This parser handles data copy&pasted from an Excel file produced from some
 * data source. The format is expected to be "cin first_name last_name ...".
 * Currently we only process the first three fields.
 */
@Component("usersParser")
public class UsersParserImpl implements UserListParser {

    @Override
    public List<ImportedUser> parse( String text )
    {
        List<ImportedUser> importedUsers = new ArrayList<ImportedUser>();

        Scanner scanner = new Scanner( text );
        while( scanner.hasNextLine() )
        {
            ImportedUser user = parseLine( scanner.nextLine() );
            if( user != null ) importedUsers.add( user );
        }
        scanner.close();

        return importedUsers;
    }

    public ImportedUser parseLine( String line )
    {
        ImportedUser user = null;

        String tokens[] = line.trim().split( "\t" );
        if( tokens.length >= 3 && isCin( tokens[0] ) )
        {
            user = new ImportedUser();
            user.setCin( tokens[0] );
            user.setFirstName( tokens[1] );
            user.setLastName( tokens[2] );
        }

        return user;
    }

    public boolean isCin( String s )
    {
        return s.matches( "\\d{9}" );
    }

}
