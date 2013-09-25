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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "question_sheets")
public class QuestionSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_sheet_id")
    @OrderColumn(name = "section_index")
    private List<QuestionSection> sections;

    @OneToMany(mappedBy = "questionSheet")
    private Set<AnswerSheet> answerSheets;

    public QuestionSheet()
    {
        sections = new ArrayList<QuestionSection>();
        sections.add( new QuestionSection() );
        answerSheets = new HashSet<AnswerSheet>();
    }

    public QuestionSheet clone()
    {
        QuestionSheet newQuestionSheet = new QuestionSheet();
        newQuestionSheet.description = description;

        newQuestionSheet.sections.clear();
        for( QuestionSection section : sections )
            newQuestionSheet.sections.add( section.clone() );

        return newQuestionSheet;
    }

    public int getNumOfSections()
    {
        return sections.size();
    }

    public void setNumOfSections( int n )
    {
        if( n < 1 ) n = 1;

        if( n > sections.size() )
        {
            for( int i = sections.size(); i < n; ++i )
                sections.add( new QuestionSection() );
        }

        if( n < sections.size() )
        {
            for( int i = sections.size(); i > n; --i )
                sections.remove( i - 1 );
        }
    }

    public int getTotalPoints()
    {
        int totalPoints = 0;
        for( QuestionSection section : sections )
            totalPoints += section.getTotalPoints();

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

    public List<QuestionSection> getSections()
    {
        return sections;
    }

    public void setSections( List<QuestionSection> sections )
    {
        this.sections = sections;
    }

    public Set<AnswerSheet> getAnswerSheets()
    {
        return answerSheets;
    }

    public void setAnswerSheets( Set<AnswerSheet> answerSheets )
    {
        this.answerSheets = answerSheets;
    }

}
