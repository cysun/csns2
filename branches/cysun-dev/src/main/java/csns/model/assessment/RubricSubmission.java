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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import csns.model.core.User;

/**
 * RubricSubmission is the collection of all the rubric evaluations of a
 * student.
 */
@Entity
@Table(name = "rubric_submissions")
public class RubricSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private RubricAssignment assignment;

    @OneToMany(mappedBy = "submission", cascade = { CascadeType.MERGE,
        CascadeType.PERSIST })
    private List<RubricEvaluation> evaluations;

    // Although the following counts can be calculated from evaluations, it's
    // better to create separate fields for them for performance reasons. In
    // particular, the submission list page can be generated much more
    // efficiently without instantiating the evaluation collection in each
    // RubricSubmissoin.

    @Column(name = "instructor_evaluation_count", nullable = false)
    private int instructorEvaluationCount;

    @Column(name = "peer_evaluation_count", nullable = false)
    private int peerEvaluationCount;

    @Column(name = "external_evaluation_count", nullable = false)
    private int externalEvaluationCount;

    public RubricSubmission()
    {
        evaluations = new ArrayList<RubricEvaluation>();
        instructorEvaluationCount = 0;
        externalEvaluationCount = 0;
        peerEvaluationCount = 0;
    }

    public RubricSubmission( User student, RubricAssignment assignment )
    {
        this();
        this.student = student;
        this.assignment = assignment;
    }

    public RubricEvaluation getEvaluation( User user )
    {
        for( RubricEvaluation evaluation : evaluations )
            if( evaluation.getEvaluator().getId().equals( user.getId() ) )
                return evaluation;
        return null;
    }

    public List<RubricEvaluation> getInstructorEvaluations()
    {
        List<RubricEvaluation> instructorEvaluations = new ArrayList<RubricEvaluation>();
        for( RubricEvaluation evaluation : evaluations )
            if( evaluation.getType() == RubricEvaluation.Type.INSTRUCTOR )
                instructorEvaluations.add( evaluation );
        return instructorEvaluations;
    }

    public List<RubricEvaluation> getPeerEvaluations()
    {
        List<RubricEvaluation> peerEvaluations = new ArrayList<RubricEvaluation>();
        for( RubricEvaluation evaluation : evaluations )
            if( evaluation.getType() == RubricEvaluation.Type.PEER )
                peerEvaluations.add( evaluation );
        return peerEvaluations;
    }

    public List<RubricEvaluation> getExternalEvaluations()
    {
        List<RubricEvaluation> externalEvaluations = new ArrayList<RubricEvaluation>();
        for( RubricEvaluation evaluation : evaluations )
            if( evaluation.getType() == RubricEvaluation.Type.EXTERNAL )
                externalEvaluations.add( evaluation );
        return externalEvaluations;
    }

    public int incrementInstructorEvaluationCount()
    {
        return ++instructorEvaluationCount;
    }

    public int incrementPeerEvaluationCount()
    {
        return ++peerEvaluationCount;
    }

    public int incrementExternalEvaluationCount()
    {
        return ++externalEvaluationCount;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public User getStudent()
    {
        return student;
    }

    public void setStudent( User student )
    {
        this.student = student;
    }

    public RubricAssignment getAssignment()
    {
        return assignment;
    }

    public void setAssignment( RubricAssignment assignment )
    {
        this.assignment = assignment;
    }

    public List<RubricEvaluation> getEvaluations()
    {
        return evaluations;
    }

    public void setEvaluations( List<RubricEvaluation> evaluations )
    {
        this.evaluations = evaluations;
    }

    public int getInstructorEvaluationCount()
    {
        return instructorEvaluationCount;
    }

    public void setInstructorEvaluationCount( int instructorEvaluationCount )
    {
        this.instructorEvaluationCount = instructorEvaluationCount;
    }

    public int getPeerEvaluationCount()
    {
        return peerEvaluationCount;
    }

    public void setPeerEvaluationCount( int peerEvaluationCount )
    {
        this.peerEvaluationCount = peerEvaluationCount;
    }

    public int getExternalEvaluationCount()
    {
        return externalEvaluationCount;
    }

    public void setExternalEvaluationCount( int externalEvaluationCount )
    {
        this.externalEvaluationCount = externalEvaluationCount;
    }

}
