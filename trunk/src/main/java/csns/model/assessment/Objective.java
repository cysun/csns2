/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015, Chengyu Sun (csun@calstatela.edu).
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * ABET Educational Objective.
 */
@Entity
@Table(name = "assessment_objectives")
public class Objective implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @Column(name = "objective_index", nullable = false)
    private int index;

    private String text;

    @ManyToMany
    @JoinTable(name = "assessment_objective_outcomes",
        joinColumns = @JoinColumn(name = "objective_id") ,
        inverseJoinColumns = @JoinColumn(name = "outcome_id") ,
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "objective_id", "outcome_id" }) })
    @OrderBy("index asc")
    private List<Outcome> outcomes;

    public Objective()
    {
        outcomes = new ArrayList<Outcome>();
    }

    public Objective( Program program, int index )
    {
        this();
        this.program = program;
        this.index = index;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex( int index )
    {
        this.index = index;
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public List<Outcome> getOutcomes()
    {
        return outcomes;
    }

    public void setOutcomes( List<Outcome> outcomes )
    {
        this.outcomes = outcomes;
    }

}
