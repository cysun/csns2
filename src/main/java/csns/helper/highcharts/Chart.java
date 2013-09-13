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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A Java class that can be easily serialized by Jackson into a Highcharts
 * configuration object (http://api.highcharts.com/highcharts).
 */
public class Chart {

    @JsonProperty("chart")
    Map<String, String> options;

    Title title;

    Axis xAxis, yAxis;

    List<Series> series;

    public Chart( String titleText, String xTitleText, String yTitleText )
    {
        options = new LinkedHashMap<String, String>();
        options.put( "type", "column" );

        title = new Title( titleText );
        xAxis = xTitleText == null ? new Axis() : new Axis( xTitleText );
        yAxis = yTitleText == null ? new Axis() : new Axis( yTitleText );
        series = new ArrayList<Series>();
    }

    public Map<String, String> getOptions()
    {
        return options;
    }

    public void setOptions( Map<String, String> options )
    {
        this.options = options;
    }

    public Title getTitle()
    {
        return title;
    }

    public void setTitle( Title title )
    {
        this.title = title;
    }

    public Axis getxAxis()
    {
        return xAxis;
    }

    public void setxAxis( Axis xAxis )
    {
        this.xAxis = xAxis;
    }

    public Axis getyAxis()
    {
        return yAxis;
    }

    public void setyAxis( Axis yAxis )
    {
        this.yAxis = yAxis;
    }

    public List<Series> getSeries()
    {
        return series;
    }

    public void setSeries( List<Series> series )
    {
        this.series = series;
    }

    public static void main( String args[] ) throws JsonProcessingException
    {
        Chart chart = new Chart( "Fruit Consumption", null, "Fruit Eaten" );

        String categories[] = { "Apples", "Bananas", "Oranges" };
        chart.getxAxis().setCategories( Arrays.asList( categories ) );

        Integer s1[] = { 1, 0, 4 };
        chart.getSeries().add( new Series( "Jane", Arrays.asList( s1 ) ) );
        Integer s2[] = { 5, 7, 3 };
        chart.getSeries().add( new Series( "John", Arrays.asList( s2 ) ) );

        ObjectMapper mapper = new ObjectMapper();
        System.out.println( mapper.writeValueAsString( chart ) );
    }

}
