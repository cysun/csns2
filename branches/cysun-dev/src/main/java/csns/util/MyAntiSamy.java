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
package csns.util;

import java.io.IOException;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class MyAntiSamy {

    private AntiSamy antiSamy;

    private Policy policy;

    private static final Logger logger = LoggerFactory.getLogger( MyAntiSamy.class );

    public MyAntiSamy( String policyFile ) throws PolicyException, IOException
    {
        antiSamy = new AntiSamy();
        policy = Policy.getInstance( new ClassPathResource( policyFile ).getInputStream() );
    }

    public CleanResults scan( String html )
    {
        CleanResults cleanResults = null;
        try
        {
            cleanResults = antiSamy.scan( html, policy );
            if( cleanResults.getNumberOfErrors() > 0 )
            {
                logger.warn( cleanResults.getNumberOfErrors()
                    + " violations found after scanning." );
                for( String errorMessage : cleanResults.getErrorMessages() )
                    logger.warn( errorMessage );
            }
        }
        catch( ScanException | PolicyException e )
        {
            logger.warn( e.getMessage(), e );
        }

        return cleanResults;
    }

    public String filter( String html )
    {
        CleanResults cleanResults = scan( html );
        return cleanResults != null ? cleanResults.getCleanHTML() : "";
    }

    public boolean validate( String html )
    {
        CleanResults cleanResults = scan( html );
        return cleanResults != null && cleanResults.getNumberOfErrors() == 0;
    }

}
