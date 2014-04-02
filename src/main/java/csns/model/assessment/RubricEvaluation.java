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
package csns.model.assessment;

import java.util.Date;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import csns.model.academics.Assignment;
import csns.model.academics.Course;
import csns.model.core.User;

@Entity
@Table(name = "rubric_evaluations")
public class RubricEvaluation {

    public enum Type {
        INSTRUCTOR, PEER, EXTERNAL
    };

    @Id
    @GeneratedValue
    private Long id;

    private Type type;

    @ManyToOne
    @JoinColumn(name = "rubric_id")
    private Rubric rubric;

    @ManyToOne
    @JoinColumn(name = "evaluator_id")
    private User evaluator;

    @ManyToOne
    @JoinColumn(name = "evaluatee_id")
    private User evaluatee;

    @ElementCollection
    @CollectionTable(name = "rubric_evaluation_ratings",
        joinColumns = @JoinColumn(name = "evaluation_id"))
    @Column(name = "rating")
    @OrderColumn(name = "rating_order")
    private int ratings[];

    @Any(metaColumn = @Column(name = "rubricable_type"),
        fetch = FetchType.EAGER)
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
        @MetaValue(value = "CO", targetEntity = Course.class),
        @MetaValue(value = "AS", targetEntity = Assignment.class) })
    @JoinColumn(name = "rubricable_id")
    private Rubricable rubricable;

    private Date date;

    public RubricEvaluation()
    {
    }

    public RubricEvaluation( Rubric rubric )
    {
        this.rubric = rubric;
        this.ratings = new int[rubric.getIndicators().size()];
    }

    public Long getId()
    {
        return id;
    }

    public Type getType()
    {
        return type;
    }

    public void setType( Type type )
    {
        this.type = type;
    }

    public Rubric getRubric()
    {
        return rubric;
    }

    public void setRubric( Rubric rubric )
    {
        this.rubric = rubric;
    }

    public User getEvaluator()
    {
        return evaluator;
    }

    public void setEvaluator( User evaluator )
    {
        this.evaluator = evaluator;
    }

    public User getEvaluatee()
    {
        return evaluatee;
    }

    public void setEvaluatee( User evaluatee )
    {
        this.evaluatee = evaluatee;
    }

    public int[] getRatings()
    {
        return ratings;
    }

    public void setRatings( int[] ratings )
    {
        this.ratings = ratings;
    }

    public Rubricable getRubricable()
    {
        return rubricable;
    }

    public void setRubricable( Rubricable rubricable )
    {
        this.rubricable = rubricable;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

}
