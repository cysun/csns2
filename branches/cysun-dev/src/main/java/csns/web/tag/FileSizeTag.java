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

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.jsp.tagext.SimpleTagSupport;

public class FileSizeTag extends SimpleTagSupport {

    private double value;

    @Override
    public void doTag() throws IOException
    {
        String unit = "B";

        if( value >= 1024 )
        {
            value /= 1024;
            unit = "KB";
        }

        if( value >= 1024 )
        {
            value /= 1024;
            unit = "MB";
        }

        if( value >= 1024 )
        {
            value /= 1024;
            unit = "GB";
        }

        getJspContext().getOut().print(
            (new DecimalFormat( "#.#" ).format( value )) + " " + unit );
    }

    public void setValue( double value )
    {
        this.value = value;
    }

}
