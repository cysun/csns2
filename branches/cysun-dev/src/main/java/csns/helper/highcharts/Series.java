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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Series {

    String name;

    List<? extends Number> data;

    Boolean showInLegend;

    public Series()
    {
    }

    public Series( String name, List<? extends Number> data )
    {
        this( name, data, null );
    }

    public Series( String name, List<? extends Number> data,
        Boolean showInLegend )
    {
        this.name = name;
        this.data = data;
        this.showInLegend = showInLegend;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public List<? extends Number> getData()
    {
        return data;
    }

    public void setData( List<? extends Number> data )
    {
        this.data = data;
    }

    public Boolean getShowInLegend()
    {
        return showInLegend;
    }

    public void setShowInLegend( Boolean showInLegend )
    {
        this.showInLegend = showInLegend;
    }

}
