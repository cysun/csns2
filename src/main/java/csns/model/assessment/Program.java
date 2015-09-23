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
    private List<Objective> objectives;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    @OrderColumn(name = "outcome_index")
    private List<Outcome> outcomes;

    @Column(nullable = false)
    private boolean deleted;

    public Program()
    {
        objectives = new ArrayList<Objective>();
        outcomes = new ArrayList<Outcome>();
        deleted = false;
    }

    public Program( Department department )
    {
        this();
        this.department = department;
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
        this.name = name;
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

    public List<Objective> getObjectives()
    {
        return objectives;
    }

    public void setObjectives( List<Objective> objectives )
    {
        this.objectives = objectives;
    }

    public List<Outcome> getOutcomes()
    {
        return outcomes;
    }

    public void setOutcomes( List<Outcome> outcomes )
    {
        this.outcomes = outcomes;
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
