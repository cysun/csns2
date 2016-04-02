/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015-2016, Chengyu Sun (csun@calstatela.edu).
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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/**
 * ABET Student Outcome (a.k.a Student Learning Outcome or SLO).
 */
@Entity
@Table(name = "assessment_program_outcomes")
public class ProgramOutcome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @Column(name = "outcome_index", nullable = false)
    private int index;

    private String text;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "assessment_program_outcome_measures",
        joinColumns = @JoinColumn(name = "outcome_id") ,
        inverseJoinColumns = @JoinColumn(name = "measure_id") )
    @OrderColumn(name = "measure_index")
    private List<ProgramMeasure> measures;

    public ProgramOutcome()
    {
    }

    public ProgramOutcome( Program program )
    {
        this.program = program;
        this.index = program.getOutcomes().size();
    }

    public ProgramOutcome( Program program, String text )
    {
        this( program );
        this.text = text;
    }

    public ProgramMeasure getMeasure( Long measureId )
    {
        for( ProgramMeasure measure : measures )
            if( measure.getId().equals( measureId ) ) return measure;

        return null;
    }

    public void setMeasure( ProgramMeasure measure )
    {
        for( int i = 0; i < measures.size(); ++i )
            if( measures.get( i ).getId().equals( measure.getId() ) )
            {
                measures.set( i, measure );
                break;
            }
    }

    public ProgramMeasure removeMeasure( Long measureId )
    {
        for( int i = 0; i < measures.size(); ++i )
            if( measures.get( i ).getId().equals( measureId ) )
                return measures.remove( i );

        return null;
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

    public List<ProgramMeasure> getMeasures()
    {
        return measures;
    }

    public void setMeasures( List<ProgramMeasure> measures )
    {
        this.measures = measures;
    }

}
