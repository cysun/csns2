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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    public SurveyChartPoint()
    {
        sectionIndex = 0;
        questionIndex = 0;
    }

    public SurveyChartPoint( Survey survey, int sectionIndex, int questionIndex )
    {
        this.survey = survey;
        this.sectionIndex = sectionIndex;
        this.questionIndex = questionIndex;
    }

    public Number getValue( String statType )
    {
        RatingQuestion question = (RatingQuestion) survey.getQuestionSheet()
            .getSections()
            .get( sectionIndex )
            .getQuestions()
            .get( questionIndex );
        return question.getRatingStats().get( statType.toLowerCase() );
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

}
