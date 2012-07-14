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

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance
@Table(name = "answers")
@DiscriminatorColumn(name = "answer_type")
public abstract class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "answer_section_id", nullable = false)
    protected AnswerSection section;

    @Column(name = "answer_index")
    protected int index;

    @ManyToOne
    @JoinColumn(name = "question_id")
    protected Question question;

    public Answer()
    {
    }

    public Answer( Question question )
    {
        this.question = question;
    }

    public abstract int check();

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public AnswerSection getSection()
    {
        return section;
    }

    public void setSection( AnswerSection section )
    {
        this.section = section;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex( int index )
    {
        this.index = index;
    }

    public Question getQuestion()
    {
        return question;
    }

    public void setQuestion( Question question )
    {
        this.question = question;
    }

}
