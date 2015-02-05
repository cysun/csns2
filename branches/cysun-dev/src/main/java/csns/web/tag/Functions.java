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
package csns.web.tag;

import org.apache.taglibs.standard.tag.common.core.Util;

import csns.model.qa.ChoiceAnswer;
import csns.model.qa.ChoiceQuestion;

public class Functions {

    public static boolean isSelectionCorrect( ChoiceQuestion question, Integer i )
    {
        return question.getCorrectSelections().contains( i );
    }

    public static boolean isChoiceSelected( ChoiceAnswer answer, Integer i )
    {
        return answer.getSelections().contains( i );
    }

    /**
     * There is a bug in the current JSTL implementation of fn:endsWith() which
     * would cause the function to return false if the substring appears more
     * than once in the string. This function is basically a replacement of
     * fn:endsWith().
     */
    public static boolean endsWith( String s, String suffix )
    {
        return s.endsWith( suffix );
    }

    public static String textToHtml( String text )
    {
        if( text == null || text.length() == 0 ) return "";
        return Util.escapeXml( text ).replaceAll( "\r?\n", "<br />" );
    }

}
