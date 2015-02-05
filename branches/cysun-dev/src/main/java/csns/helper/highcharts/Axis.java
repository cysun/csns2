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
package csns.helper.highcharts;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Axis {

    Title title;

    List<String> categories;

    Integer min, max;

    public Axis()
    {
    }

    public Axis( String titleText )
    {
        title = new Title( titleText );
    }

    public void setCategories( int beginYear, int endYear )
    {
        categories = new ArrayList<String>();
        for( int i = beginYear; i <= endYear; ++i )
            categories.add( Integer.valueOf( i ).toString() );
    }

    public void setTitleText( String titleText )
    {
        title = new Title( titleText );
    }

    public Title getTitle()
    {
        return title;
    }

    public void setTitle( Title title )
    {
        this.title = title;
    }

    public List<String> getCategories()
    {
        return categories;
    }

    public void setCategories( List<String> categories )
    {
        this.categories = categories;
    }

    public Integer getMin()
    {
        return min;
    }

    public void setMin( Integer min )
    {
        this.min = min;
    }

    public Integer getMax()
    {
        return max;
    }

    public void setMax( Integer max )
    {
        this.max = max;
    }

}
