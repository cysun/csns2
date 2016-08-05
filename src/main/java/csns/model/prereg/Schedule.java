/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Mahdiye Jamali (mjamali@calstatela.edu).
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
package csns.model.prereg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import csns.model.academics.Department;
import csns.model.academics.Term;

@Entity
@Table(name = "prereg_schedules")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "code",
        column = @Column(name = "term", nullable = false)) })
    private Term term;

    private String description;

    @Column(name = "prereg_start")
    private Date preregStart;

    @Column(name = "prereg_end")
    private Date preregEnd;

    @OneToMany(mappedBy = "schedule",
        cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @OrderBy("course asc, sectionNumber asc")
    private List<Section> sections;

    @Column(name = "default_section_capacity", nullable = false)
    private int defaultSectionCapacity;

    @Column(name = "default_undergrad_reg_limit", nullable = false)
    private int defaultUndergradRegLimit;

    @Column(name = "default_grad_reg_limit", nullable = false)
    private int defaultGradRegLimit;

    @Column(nullable = false)
    private boolean deleted;

    public Schedule()
    {
        sections = new ArrayList<Section>();
        defaultSectionCapacity = 30;
        defaultUndergradRegLimit = 5;
        defaultGradRegLimit = 4;
        deleted = false;
    }

    public Schedule( Department department, Term term )
    {
        this();
        this.department = department;
        this.term = term;
    }

    public boolean isPreregEnded()
    {
        return preregEnd != null && (new Date()).after( preregEnd );
    }

    public boolean isPreregStarted()
    {
        return preregStart != null && (new Date()).after( preregStart );
    }

    public boolean isPreregOpen()
    {
        return isPreregStarted() && !isPreregEnded();
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

    public Term getTerm()
    {
        return term;
    }

    public void setTerm( Term term )
    {
        this.term = term;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public Date getPreregStart()
    {
        return preregStart;
    }

    public void setPreregStart( Date preregStart )
    {
        this.preregStart = preregStart;
    }

    public Date getPreregEnd()
    {
        return preregEnd;
    }

    public void setPreregEnd( Date preregEnd )
    {
        this.preregEnd = preregEnd;
    }

    public List<Section> getSections()
    {
        return sections;
    }

    public void setSections( List<Section> sections )
    {
        this.sections = sections;
    }

    public int getDefaultSectionCapacity()
    {
        return defaultSectionCapacity;
    }

    public void setDefaultSectionCapacity( int defaultSectionCapacity )
    {
        this.defaultSectionCapacity = defaultSectionCapacity;
    }

    public int getDefaultUndergradRegLimit()
    {
        return defaultUndergradRegLimit;
    }

    public void setDefaultUndergradRegLimit( int defaultUndergradRegLimit )
    {
        this.defaultUndergradRegLimit = defaultUndergradRegLimit;
    }

    public int getDefaultGradRegLimit()
    {
        return defaultGradRegLimit;
    }

    public void setDefaultGradRegLimit( int defaultGradRegLimit )
    {
        this.defaultGradRegLimit = defaultGradRegLimit;
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
