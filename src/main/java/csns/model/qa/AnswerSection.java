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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "answer_sections")
public class AnswerSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "answer_sheet_id", nullable = false)
    protected AnswerSheet answerSheet;

    @Column(name = "section_index", nullable = false)
    protected int index;

    @OneToMany(mappedBy = "section", cascade = { CascadeType.MERGE,
        CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @OrderColumn(name = "answer_index")
    protected List<Answer> answers;

    public AnswerSection()
    {
        this( null, 0 );
    }

    public AnswerSection( AnswerSheet answerSheet, int index )
    {
        this.answerSheet = answerSheet;
        this.index = index;
        this.answers = new ArrayList<Answer>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public AnswerSheet getAnswerSheet()
    {
        return answerSheet;
    }

    public void setAnswerSheet( AnswerSheet answerSheet )
    {
        this.answerSheet = answerSheet;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex( int index )
    {
        this.index = index;
    }

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public void setAnswers( List<Answer> answers )
    {
        this.answers = answers;
    }

}
