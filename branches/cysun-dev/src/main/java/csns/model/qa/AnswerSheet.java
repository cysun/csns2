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
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import csns.model.core.User;

@Entity
@Table(name = "answer_sheets")
public class AnswerSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_sheet_id", nullable = false)
    private QuestionSheet questionSheet;

    @OneToMany(mappedBy = "answerSheet",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER)
    @OrderColumn(name = "section_index")
    private List<AnswerSection> sections;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private Date date;

    public AnswerSheet()
    {
        sections = new ArrayList<AnswerSection>();
    }

    public AnswerSheet( QuestionSheet questionSheet )
    {
        this.questionSheet = questionSheet;

        sections = new ArrayList<AnswerSection>();
        for( int i = 0; i < questionSheet.getSections().size(); ++i )
        {
            AnswerSection answerSection = new AnswerSection( this, i );
            List<Question> questions = questionSheet.getSections()
                .get( i )
                .getQuestions();
            for( int j = 0; j < questions.size(); ++j )
            {
                Answer answer = questions.get( j ).createAnswer();
                answer.setSection( answerSection );
                answer.setIndex( j );
                answerSection.getAnswers().add( answer );
            }
            sections.add( answerSection );
        }
    }

    public int getNumOfSections()
    {
        return sections.size();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public QuestionSheet getQuestionSheet()
    {
        return questionSheet;
    }

    public void setQuestionSheet( QuestionSheet questionSheet )
    {
        this.questionSheet = questionSheet;
    }

    public List<AnswerSection> getSections()
    {
        return sections;
    }

    public void setSections( List<AnswerSection> sections )
    {
        this.sections = sections;
    }

    public User getAuthor()
    {
        return author;
    }

    public void setAuthor( User author )
    {
        this.author = author;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

}
