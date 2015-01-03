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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import csns.model.academics.Section;
import csns.model.core.User;

@Entity
@Table(name = "rubric_evaluations")
public class RubricEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        INSTRUCTOR, PEER, EXTERNAL
    };

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private RubricSubmission submission;

    @ManyToOne
    @JoinColumn(name = "evaluator_id")
    private User evaluator;

    @ElementCollection
    @CollectionTable(name = "rubric_evaluation_ratings",
        joinColumns = @JoinColumn(name = "evaluation_id"))
    @Column(name = "rating")
    @OrderColumn(name = "rating_order")
    private List<Integer> ratings;

    private String comments;

    private Date date;

    private boolean completed;

    private boolean deleted;

    private static final Logger logger = LoggerFactory.getLogger( RubricEvaluation.class );

    public RubricEvaluation()
    {
        ratings = new ArrayList<Integer>();
        completed = false;
        deleted = false;
    }

    public RubricEvaluation( RubricSubmission submission, User evaluator )
    {
        this();
        this.submission = submission;
        this.evaluator = evaluator;

        RubricAssignment assignment = submission.getAssignment();
        // I wish I could add nulls to the list, but ArrayList refuses to do it,
        // so we have to add -1 instead.
        for( int i = 0; i < assignment.getRubric().getIndicators().size(); ++i )
            ratings.add( -1 );

        Section section = assignment.getSection();
        if( section.isInstructor( evaluator ) )
            type = Type.INSTRUCTOR;
        else if( section.isEnrolled( evaluator ) )
            type = Type.PEER;
        else if( assignment.isExternalEvaluator( evaluator ) )
            type = Type.EXTERNAL;
        else
        {
            logger.error( "Cannot determine rubric evaluation type for "
                + evaluator.getUsername() );
            throw new RuntimeException( "Invalid Rubric Evaluaton" );
        }
    }

    public void setCompleted()
    {
        if( completed ) return;
        for( int rating : ratings )
            if( rating < 0 ) return;

        completed = true;

        switch( type )
        {
            case INSTRUCTOR:
                submission.incrementInstructorEvaluationCount();
                break;

            case PEER:
                submission.incrementPeerEvaluationCount();
                break;

            case EXTERNAL:
                submission.incrementExternalEvaluationCount();
                break;

            default:
                // We really shouldn't get here as there should have been an
                // exception in the constructor of RubricEvaluation if the
                // evaluation type cannot be determined.
                logger.warn( "Invalid rubric evaluation type." );
        }
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Type getType()
    {
        return type;
    }

    public void setType( Type type )
    {
        this.type = type;
    }

    public RubricSubmission getSubmission()
    {
        return submission;
    }

    public void setSubmission( RubricSubmission submission )
    {
        this.submission = submission;
    }

    public User getEvaluator()
    {
        return evaluator;
    }

    public void setEvaluator( User evaluator )
    {
        this.evaluator = evaluator;
    }

    public List<Integer> getRatings()
    {
        return ratings;
    }

    public void setRatings( List<Integer> ratings )
    {
        this.ratings = ratings;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted( boolean completed )
    {
        this.completed = completed;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted( boolean deleted )
    {
        this.deleted = deleted;
    }

}
