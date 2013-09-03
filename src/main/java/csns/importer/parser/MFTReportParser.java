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
package csns.importer.parser;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import csns.importer.MFTReportImporter;
import csns.model.assessment.MFTScore;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Component
public class MFTReportParser {

    @Autowired
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger( MFTReportParser.class );

    public void parse( String text, MFTReportImporter importer )
    {
        Scanner scanner = new Scanner( text );
        scanner.useDelimiter( "\\s+|\\r\\n|\\r|\\n" );
        while( scanner.hasNext() )
        {
            // last name
            String lastName = scanner.next();
            while( !lastName.endsWith( "," ) )
                lastName += " " + scanner.next();

            // first name
            String firstName = scanner.next();

            // score
            Stack<String> stack = new Stack<String>();
            String s = scanner.next();
            while( !isScore( s ) )
                stack.push( s );
            int value = Integer.parseInt( s );

            // authorization code
            stack.pop();

            // cin
            String cin = null;
            if( !stack.empty() && isCin( stack.peek() ) ) cin = stack.pop();

            // middle name
            String middleName = stack.isEmpty() ? null : stack.pop();
            while( !stack.empty() )
                middleName = stack.pop() + " " + middleName;

            // user
            User user = null;
            if( cin != null )
                user = userDao.getUserByCin( cin );
            else
            {
                List<User> users = userDao.getUsers( lastName, firstName );
                if( users.size() == 1 ) user = users.get( 0 );
            }

            if( user != null )
            {
                MFTScore score = new MFTScore();
                score.setDepartment( importer.getDepartment() );
                score.setDate( importer.getDate() );
                score.setUser( user );
                score.setValue( value );
                importer.getScores().add( score );
            }
            else
            {
                User failedUser = new User();
                failedUser.setLastName( lastName );
                failedUser.setFirstName( firstName );
                failedUser.setMiddleName( middleName );
                failedUser.setCin( cin );
                importer.getFailedUsers().add( failedUser );
            }

            logger.debug( lastName + "," + firstName + "," + middleName + ","
                + cin + "," + value );
        }

        scanner.close();
    }

    public boolean isCin( String s )
    {
        return s.matches( "\\d{9}" );
    }

    private boolean isScore( String s )
    {
        return s.matches( "\\d{3}" );
    }

}
