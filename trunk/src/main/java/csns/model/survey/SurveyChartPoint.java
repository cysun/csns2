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
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import csns.model.qa.RatingQuestion;

/**
 * Each SurveyChartPoint is basically a survey question (a RatingQuestion to be
 * exact). The reason we create this class instead of using RatingQuestion
 * directly is because we want to know the survey a question belongs to, and it
 * is very complicated and error-prone to use bidirectional association for
 * Survey-Section-Question (just trust me on this).
 */
@Entity
@Table(name = "survey_chart_points")
public class SurveyChartPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Column(name = "section_index", nullable = false)
    private int sectionIndex;

    @Column(name = "question_index", nullable = false)
    private int questionIndex;

    @Column(name = "values_set", nullable = false)
    private boolean valuesSet;

    private Double min, max, average, median;

    private static final Logger logger = LoggerFactory.getLogger( SurveyChartPoint.class );

    public SurveyChartPoint()
    {
        sectionIndex = 0;
        questionIndex = 0;
        valuesSet = false;
    }

    public SurveyChartPoint( Survey survey, int sectionIndex, int questionIndex )
    {
        this.survey = survey;
        this.sectionIndex = sectionIndex;
        this.questionIndex = questionIndex;
        this.valuesSet = false;

        setValues();
    }

    public Number getValue( String statType )
    {
        if( survey == null ) return null;

        statType = statType.toLowerCase();
        if( valuesSet )
        {
            switch( statType )
            {
                case "min":
                    return min;

                case "max":
                    return max;

                case "average":
                    return average;

                case "median":
                    return median;

                default:
                    logger.warn( "Invalid stat type: " + statType );
                    return average;
            }
        }

        RatingQuestion question = (RatingQuestion) survey.getQuestionSheet()
            .getSections()
            .get( sectionIndex )
            .getQuestions()
            .get( questionIndex );
        Map<String, Number> stats = question.getRatingStats();

        return stats != null ? stats.get( statType ) : null;
    }

    public boolean setValues()
    {
        if( valuesSet || survey == null || !survey.isClosed() ) return false;

        RatingQuestion question = (RatingQuestion) survey.getQuestionSheet()
            .getSections()
            .get( sectionIndex )
            .getQuestions()
            .get( questionIndex );
        Map<String, Number> stats = question.getRatingStats();

        if( stats != null )
        {
            min = stats.get( "min" ).doubleValue();
            max = stats.get( "max" ).doubleValue();
            average = stats.get( "average" ).doubleValue();
            median = stats.get( "median" ).doubleValue();
        }

        valuesSet = true;
        return true;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Survey getSurvey()
    {
        return survey;
    }

    public void setSurvey( Survey survey )
    {
        this.survey = survey;
    }

    public int getSectionIndex()
    {
        return sectionIndex;
    }

    public void setSectionIndex( int sectionIndex )
    {
        this.sectionIndex = sectionIndex;
    }

    public int getQuestionIndex()
    {
        return questionIndex;
    }

    public void setQuestionIndex( int questionIndex )
    {
        this.questionIndex = questionIndex;
    }

    public boolean isValuesSet()
    {
        return valuesSet;
    }

    public void setValuesSet( boolean valuesSet )
    {
        this.valuesSet = valuesSet;
    }

    public Double getAverage()
    {
        return average;
    }

    public void setAverage( Double average )
    {
        this.average = average;
    }

    public Double getMedian()
    {
        return median;
    }

    public void setMedian( Double median )
    {
        this.median = median;
    }

    public Double getMin()
    {
        return min;
    }

    public void setMin( Double min )
    {
        this.min = min;
    }

    public Double getMax()
    {
        return max;
    }

    public void setMax( Double max )
    {
        this.max = max;
    }

}
