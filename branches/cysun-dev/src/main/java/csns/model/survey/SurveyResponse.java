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
package csns.model.survey;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import csns.model.qa.AnswerSheet;

@Entity
@Table(name = "survey_responses")
public class SurveyResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "answer_sheet_id", nullable = false, unique = true)
    private AnswerSheet answerSheet;

    public SurveyResponse()
    {
    }

    public SurveyResponse( Survey survey )
    {
        assert survey.isPublished();

        this.survey = survey;
        this.answerSheet = new AnswerSheet( survey.getQuestionSheet() );
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

    public AnswerSheet getAnswerSheet()
    {
        return answerSheet;
    }

    public void setAnswerSheet( AnswerSheet answerSheet )
    {
        this.answerSheet = answerSheet;
    }

}
