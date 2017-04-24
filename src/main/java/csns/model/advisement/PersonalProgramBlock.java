/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016-2017, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.advisement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import csns.model.academics.Course;
import csns.model.academics.ProgramBlock;

@Entity
@Table(name = "personal_program_blocks")
public class PersonalProgramBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_block_id")
    private ProgramBlock programBlock;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "block_id")
    private List<PersonalProgramEntry> entries;

    public PersonalProgramBlock()
    {
        entries = new ArrayList<PersonalProgramEntry>();
    }

    public PersonalProgramBlock( ProgramBlock programBlock )
    {
        this();
        this.programBlock = programBlock;

        for( Course course : programBlock.getCourses() )
            entries.add( new PersonalProgramEntry( course ) );
    }

    public boolean isRequirementsMet()
    {
        int classesCompleted = 0;
        double unitsCompleted = 0;
        for( PersonalProgramEntry entry : entries )
            if( entry.getEnrollment() != null || entry.isRequirementMet() )
            {
                ++classesCompleted;
                Course course = entry.getCourse();
                unitsCompleted += course.getUnits() * course.getUnitFactor();
            }

        return programBlock.isRequireAll() && classesCompleted == entries.size()
            || !programBlock.isRequireAll() && classesCompleted > 0
                && unitsCompleted >= programBlock.getUnitsRequired();
    }

    public Map<Course, PersonalProgramEntry> getEntryMap()
    {
        Map<Course, PersonalProgramEntry> entryMap = new HashMap<Course, PersonalProgramEntry>();
        for( PersonalProgramEntry entry : entries )
            entryMap.put( entry.getCourse(), entry );
        return entryMap;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public ProgramBlock getProgramBlock()
    {
        return programBlock;
    }

    public void setProgramBlock( ProgramBlock programBlock )
    {
        this.programBlock = programBlock;
    }

    public List<PersonalProgramEntry> getEntries()
    {
        return entries;
    }

    public void setEntries( List<PersonalProgramEntry> entries )
    {
        this.entries = entries;
    }

}
