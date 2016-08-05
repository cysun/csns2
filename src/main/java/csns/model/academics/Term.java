/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2016, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.academics;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embeddable;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Embeddable
public class Term implements Serializable, Comparable<Term> {

    private static final long serialVersionUID = 1L;

    private int code;

    private static final Logger logger = LoggerFactory.getLogger( Term.class );

    public Term()
    {
        setCode( Calendar.getInstance() );
    }

    public Term( int code )
    {
        this.code = code;
    }

    public Term( Calendar calendar )
    {
        setCode( calendar );
    }

    public Term( Date date )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        setCode( calendar );
    }

    public Term( int year, String season )
    {
        code = (year - 1900) * 10;

        season = season.toUpperCase();
        if( season.equals( "WINTER" ) || season.equals( "W" ) )
            code += 1;
        else if( season.equals( "SPRING" ) || season.equals( "S" ) )
            code += 3;
        else if( season.equals( "SUMMER" ) || season.equals( "X" ) )
            code += 6;
        else if( season.equals( "FALL" ) || season.equals( "F" ) )
            code += 9;
        else
        {
            logger.warn( season + " is not a valid season." );
            code += 9;
        }
    }

    @Override
    public boolean equals( Object other )
    {
        return this == other
            || other instanceof Term && ((Term) other).code == code;
    }

    @Override
    public int hashCode()
    {
        return Integer.valueOf( code ).hashCode();
    }

    @Override
    public int compareTo( Term term )
    {
        if( term == null )
            throw new IllegalArgumentException( "Cannot compare to NULL." );

        return code - term.code;
    }

    public boolean isValid()
    {
        return code != -1;
    }

    private void setCode( Calendar calendar )
    {
        // JDK's Calendar and Date implementations are known to be problematic.
        // In Java 8 it's recommended to use java.time, but since we are
        // targeting Java 7, here we use Joda-Time
        // (http://www.joda.org/joda-time/) to calculate the correct
        // (i.e. ISO 8601) week_of_year.
        LocalDateTime dateTime = LocalDateTime.fromCalendarFields( calendar );

        code = (dateTime.getYear() - 1900) * 10;

        int week = dateTime.getWeekOfWeekyear();
        if( week < 4 )
            code += 1; // Winter term: week 1-3
        else if( week < 22 )
            code += 3; // Spring term: week 4-21
        else if( week < 34 )
            code += 6; // Summer term: week 22-33
        else
            code += 9; // Fall term: week 34-
    }

    public Term next()
    {
        int yearCode = code / 10;
        int termSuffix = code % 10;

        switch( termSuffix )
        {
            case 1:
                termSuffix = 3;
                break;
            case 3:
                termSuffix = 6;
                break;
            case 6:
                termSuffix = 9;
                break;
            default:
                ++yearCode;
                termSuffix = 1;
        }

        return new Term( yearCode * 10 + termSuffix );
    }

    public Term previous()
    {
        int yearCode = code / 10;
        int termSuffix = code % 10;

        switch( termSuffix )
        {
            case 9:
                termSuffix = 6;
                break;
            case 6:
                termSuffix = 3;
                break;
            case 3:
                termSuffix = 1;
                break;
            default:
                --yearCode;
                termSuffix = 9;
        }

        return new Term( yearCode * 10 + termSuffix );
    }

    @Override
    public String toString()
    {
        String s;
        switch( code % 10 )
        {
            case 1:
                s = "Winter";
                break;
            case 3:
                s = "Spring";
                break;
            case 6:
                s = "Summer";
                break;
            case 9:
                s = "Fall";
                break;
            default:
                s = "UNKNOWN";
        }

        return s + " " + (code / 10 + 1900);
    }

    public String getShortString()
    {
        String s;
        switch( code % 10 )
        {
            case 1:
                s = "W";
                break;
            case 3:
                s = "S";
                break;
            case 6:
                s = "X";
                break;
            case 9:
                s = "F";
                break;
            default:
                s = "U";
        }

        int year = ((code / 10) + 1900) % 100;
        return s + (year < 10 ? "0" + year : year);
    }

    public void setShortString( String s )
    {
        int year = 2000 + Integer.parseInt( s.substring( 1, 3 ) );
        int newCode = (year - 1900) * 10;

        switch( s.substring( 0, 1 ).toUpperCase() )
        {
            case "W":
                newCode += 1;
                break;
            case "S":
                newCode += 3;
                break;
            case "X":
                newCode += 6;
                break;
            case "F":
                newCode += 9;
                break;
            default:
                newCode = -1;
                logger.warn( "Invalid term short string: " + s );
        }

        code = newCode;
    }

    public int getYear()
    {
        return code / 10 + 1900;
    }

    public String getTermName()
    {
        String s;
        switch( code % 10 )
        {
            case 1:
                s = "WINTER";
                break;
            case 3:
                s = "SPRING";
                break;
            case 6:
                s = "SUMMER";
                break;
            case 9:
                s = "FALL";
                break;
            default:
                s = "UNKNOWN";
        }

        return s;
    }

    public String getFullName()
    {
        return toString();
    }

    public boolean equals( Term term )
    {
        return term == null ? false : code == term.code;
    }

    public boolean before( Term term )
    {
        return term == null ? false : code < term.code;
    }

    public boolean after( Term term )
    {
        return term == null ? false : code > term.code;
    }

    public boolean before( Date date )
    {
        return before( new Term( date ) );
    }

    public boolean after( Date date )
    {
        return after( new Term( date ) );
    }

    public int getTermSuffix()
    {
        return code % 10;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode( int code )
    {
        this.code = code;
    }

}
