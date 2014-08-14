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
package csns.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import csns.helper.highcharts.Series;

@Entity
@Table(name = "survey_chart_series")
public class SurveyChartSeries implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chart_id")
    private SurveyChart chart;

    private String name;

    @Column(name = "stat_type")
    private String statType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "series_id")
    @OrderColumn(name = "point_index")
    private List<SurveyChartPoint> points;

    public SurveyChartSeries()
    {
        points = new ArrayList<SurveyChartPoint>();
    }

    public SurveyChartPoint getPoint( Long surveyId, int sectionIndex,
        int questionIndex )
    {
        if( surveyId <= 0 ) return getEmptyPoint();

        for( int i = points.size() - 1; i >= 0; --i )
        {
            SurveyChartPoint point = points.get( i );
            if( point.getSurvey() != null
                && point.getSurvey().getId().equals( surveyId )
                && point.getSectionIndex() == sectionIndex
                && point.getQuestionIndex() == questionIndex ) return point;
        }

        return null;
    }

    public SurveyChartPoint getEmptyPoint()
    {
        for( int i = points.size() - 1; i >= 0; --i )
            if( points.get( i ).getSurvey() == null ) return points.get( i );
        return null;
    }

    public List<Number> getValues()
    {
        List<Number> values = new ArrayList<Number>();
        for( SurveyChartPoint point : points )
            values.add( point.getValue( statType ) );
        return values;
    }

    public boolean setValues()
    {
        boolean valuesSet = false;
        for( SurveyChartPoint point : points )
            if( point.setValues() ) valuesSet = true;
        return valuesSet;
    }

    public Series getHighchartSeries( boolean showInLegend )
    {
        return new Series( name, getValues(), showInLegend );
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public SurveyChart getChart()
    {
        return chart;
    }

    public void setChart( SurveyChart chart )
    {
        this.chart = chart;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getStatType()
    {
        return statType;
    }

    public void setStatType( String statType )
    {
        this.statType = statType;
    }

    public List<SurveyChartPoint> getPoints()
    {
        return points;
    }

    public void setPoints( List<SurveyChartPoint> points )
    {
        this.points = points;
    }

}
