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
package csns.web.editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class CalendarPropertyEditor extends PropertyEditorSupport {

    private DateFormat dateFormat;

    private static final Logger logger = LoggerFactory.getLogger( CalendarPropertyEditor.class );

    public CalendarPropertyEditor()
    {
        this( "MM/dd/yyyy HH:mm:ss" );
    }

    public CalendarPropertyEditor( String format )
    {
        this.dateFormat = new SimpleDateFormat( format );
    }

    public void setAsText( String text ) throws IllegalArgumentException
    {
        if( !StringUtils.hasText( text ) )
        {
            setValue( null );
            return;
        }

        try
        {
            Calendar value = Calendar.getInstance();
            value.setTime( dateFormat.parse( text ) );
            setValue( value );
        }
        catch( ParseException ex )
        {
            logger.warn( "Cannot parse date: " + text );
            throw new IllegalArgumentException( "Cannot parse date: " + text );
        }
    }

    public String getAsText()
    {
        Calendar value = (Calendar) getValue();
        return value != null ? dateFormat.format( value.getTime() ) : "";
    }

}
