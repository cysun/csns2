/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Embeddable
public class Quarter implements Serializable, Comparable<Quarter> {

    private static final long serialVersionUID = 1L;

    private int code;

    private static final Logger logger = LoggerFactory.getLogger( Quarter.class );

    public Quarter()
    {
        setCode( Calendar.getInstance() );
    }

    public Quarter( int code )
    {
        this.code = code;
    }

    public Quarter( Calendar calendar )
    {
        setCode( calendar );
    }

    public Quarter( Date date )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        setCode( calendar );
    }

    public Quarter( int year, String term )
    {
        code = (year - 1900) * 10;

        term = term.toUpperCase();
        if( term.equals( "WINTER" ) || term.equals( "W" ) )
            code += 1;
        else if( term.equals( "SPRING" ) || term.equals( "S" ) )
            code += 3;
        else if( term.equals( "SUMMER" ) || term.equals( "X" ) )
            code += 6;
        else if( term.equals( "FALL" ) || term.equals( "F" ) )
            code += 9;
        else
        {
            logger.warn( term + " is not a valid quarter." );
            code += 9;
        }
    }

    @Override
    public boolean equals( Object other )
    {
        return this == other || other instanceof Quarter
            && ((Quarter) other).code == code;
    }

    @Override
    public int hashCode()
    {
        return Integer.valueOf( code ).hashCode();
    }

    @Override
    public int compareTo( Quarter quarter )
    {
        if( quarter == null )
            throw new IllegalArgumentException( "Cannot compare to NULL." );

        return code - quarter.code;
    }

    public boolean isValid()
    {
        return code != -1;
    }

    private void setCode( Calendar calendar )
    {
        code = (calendar.get( Calendar.YEAR ) - 1900) * 10;

        int week = calendar.get( Calendar.WEEK_OF_YEAR );
        if( week < 13 )
            code += 1; // Winter quarter: week 1-12
        else if( week < 25 )
            code += 3; // Spring quarter: week 13-24
        else if( week < 38 )
            code += 6; // Summer quarter: week 25-37
        else
            code += 9; // Fall quarter: week 38-
    }

    public Quarter next()
    {
        int yearCode = code / 10;
        int quarterSuffix = code % 10;

        switch( quarterSuffix )
        {
            case 1:
                quarterSuffix = 3;
                break;
            case 3:
                quarterSuffix = 6;
                break;
            case 6:
                quarterSuffix = 9;
                break;
            default:
                ++yearCode;
                quarterSuffix = 1;
        }

        return new Quarter( yearCode * 10 + quarterSuffix );
    }

    public Quarter previous()
    {
        int yearCode = code / 10;
        int quarterSuffix = code % 10;

        switch( quarterSuffix )
        {
            case 9:
                quarterSuffix = 6;
                break;
            case 6:
                quarterSuffix = 3;
                break;
            case 3:
                quarterSuffix = 1;
                break;
            default:
                --yearCode;
                quarterSuffix = 9;
        }

        return new Quarter( yearCode * 10 + quarterSuffix );
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
                logger.warn( "Invalid quarter short string: " + s );
        }

        code = newCode;
    }

    public int getYear()
    {
        return code / 10 + 1900;
    }

    public String getQuarterName()
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

    public boolean equals( Quarter quarter )
    {
        return quarter == null ? false : code == quarter.code;
    }

    public boolean before( Quarter quarter )
    {
        return quarter == null ? false : code < quarter.code;
    }

    public boolean after( Quarter quarter )
    {
        return quarter == null ? false : code > quarter.code;
    }

    public boolean before( Date date )
    {
        return before( new Quarter( date ) );
    }

    public boolean after( Date date )
    {
        return after( new Quarter( date ) );
    }

    public int getQuarterSuffix()
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
