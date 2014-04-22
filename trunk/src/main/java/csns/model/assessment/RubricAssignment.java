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
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import csns.model.academics.Section;
import csns.model.core.User;

/**
 * RubricAssignment is not really an Assignment like a regular or an online
 * assignment, but it's convenient to model it as an "assignment" in the sense
 * that we assign an evaluation task to some people, and their rubric
 * evaluations are the "submissions" to this assignment.
 */
@Entity
@Table(name = "rubric_assignments")
public class RubricAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "rubric_id", nullable = false)
    private Rubric rubric;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "evaluated_by_instructors", nullable = false)
    private boolean evaluatedByInstructors;

    @Column(name = "evaluated_by_students", nullable = false)
    private boolean evaluatedByStudents;

    @ManyToMany
    @JoinTable(name = "rubric_external_evaluators",
        joinColumns = @JoinColumn(name = "rubric_assignment_id"),
        inverseJoinColumns = @JoinColumn(name = "evaluator_id"))
    private Set<User> externalEvaluators;

    @Column(name = "publish_date")
    private Calendar publishDate;

    @Column(name = "due_date")
    private Calendar dueDate;

    @Column(nullable = false)
    private boolean deleted;

    public RubricAssignment()
    {
        deleted = false;
        evaluatedByInstructors = true;
        evaluatedByStudents = false;
        externalEvaluators = new HashSet<User>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Rubric getRubric()
    {
        return rubric;
    }

    public void setRubric( Rubric rubric )
    {
        this.rubric = rubric;
    }

    public Section getSection()
    {
        return section;
    }

    public void setSection( Section section )
    {
        this.section = section;
    }

    public boolean isEvaluatedByInstructors()
    {
        return evaluatedByInstructors;
    }

    public void setEvaluatedByInstructors( boolean evaluatedByInstructors )
    {
        this.evaluatedByInstructors = evaluatedByInstructors;
    }

    public boolean isEvaluatedByStudents()
    {
        return evaluatedByStudents;
    }

    public void setEvaluatedByStudents( boolean evaluatedByStudents )
    {
        this.evaluatedByStudents = evaluatedByStudents;
    }

    public Set<User> getExternalEvaluators()
    {
        return externalEvaluators;
    }

    public void setExternalEvaluators( Set<User> externalEvaluators )
    {
        this.externalEvaluators = externalEvaluators;
    }

    public Calendar getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate( Calendar publishDate )
    {
        this.publishDate = publishDate;
    }

    public Calendar getDueDate()
    {
        return dueDate;
    }

    public void setDueDate( Calendar dueDate )
    {
        this.dueDate = dueDate;
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
