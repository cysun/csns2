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
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import csns.model.academics.Department;

/**
 * This is not be confused with the Program in the academics package. That
 * Program is used for curriculum purposes and is, generally speaking, somewhat
 * informal, and this Program is for assessment purposes.
 */
@Entity(name = "AssessmentProgram")
@Table(name = "assessment_programs")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private String name;

    private String vision;

    private String mission;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    @OrderColumn(name = "objective_index")
    private List<ProgramObjective> objectives;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    @OrderColumn(name = "outcome_index")
    private List<ProgramOutcome> outcomes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "program_id")
    @OrderColumn(name = "section_index")
    private List<ProgramSection> sections;

    @Column(nullable = false)
    private boolean deleted;

    public Program()
    {
        name = "";

        objectives = new ArrayList<ProgramObjective>();
        outcomes = new ArrayList<ProgramOutcome>();

        sections = new ArrayList<ProgramSection>();
        sections.add( new ProgramSection( "Documentation" ) );
        sections.add( new ProgramSection( "Other Assessment Artifacts" ) );

        deleted = false;
    }

    public Program( Department department )
    {
        this();
        this.department = department;
    }

    public ProgramSection getSection( Long sectionId )
    {
        for( ProgramSection section : sections )
            if( section.getId().equals( sectionId ) ) return section;

        return null;
    }

    public ProgramSection removeSection( Long sectionId )
    {
        for( int i = 0; i < sections.size(); ++i )
            if( sections.get( i ).getId().equals( sectionId ) )
                return sections.remove( i );

        return null;
    }

    public void reIndexObjectives()
    {
        for( int i = 0; i < objectives.size(); ++i )
            objectives.get( i ).setIndex( i );
    }

    public void reIndexOutcomes()
    {
        for( int i = 0; i < outcomes.size(); ++i )
            outcomes.get( i ).setIndex( i );
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        if( name != null ) this.name = name;
    }

    public String getVision()
    {
        return vision;
    }

    public void setVision( String vision )
    {
        this.vision = vision;
    }

    public String getMission()
    {
        return mission;
    }

    public void setMission( String mission )
    {
        this.mission = mission;
    }

    public List<ProgramObjective> getObjectives()
    {
        return objectives;
    }

    public void setObjectives( List<ProgramObjective> objectives )
    {
        this.objectives = objectives;
    }

    public List<ProgramOutcome> getOutcomes()
    {
        return outcomes;
    }

    public void setOutcomes( List<ProgramOutcome> outcomes )
    {
        this.outcomes = outcomes;
    }

    public List<ProgramSection> getSections()
    {
        return sections;
    }

    public void setSections( List<ProgramSection> sections )
    {
        this.sections = sections;
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
