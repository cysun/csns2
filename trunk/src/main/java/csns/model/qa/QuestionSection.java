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
package csns.model.qa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "question_sections")
public class QuestionSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    protected String description;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "question_section_id")
    @OrderColumn(name = "question_index")
    protected List<Question> questions;

    public QuestionSection()
    {
        questions = new ArrayList<Question>();
    }

    public QuestionSection clone()
    {
        QuestionSection newSection = new QuestionSection();

        newSection.description = description;
        for( Question question : questions )
            newSection.questions.add( question.clone() );

        return newSection;
    }

    public Question getQuestion( Long questionId )
    {
        for( Question question : questions )
            if( question.getId().equals( questionId ) ) return question;

        return null;
    }

    public Question removeQuestion( Long questionId )
    {
        for( int i = 0; i < questions.size(); ++i )
            if( questions.get( i ).getId().equals( questionId ) )
                return questions.remove( i );

        return null;
    }

    public void replaceQuestion( Question question )
    {
        for( int i = 0; i < questions.size(); ++i )
            if( questions.get( i ).getId().equals( question.getId() ) )
            {
                questions.set( i, question );
                break;
            }
    }

    public int getTotalPoints()
    {
        int totalPoints = 0;
        for( Question question : questions )
            totalPoints += question.getPointValue();

        return totalPoints;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public List<Question> getQuestions()
    {
        return questions;
    }

    public void setQuestions( List<Question> questions )
    {
        this.questions = questions;
    }

}
