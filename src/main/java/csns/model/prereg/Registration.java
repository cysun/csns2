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
import java.util.Date;
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
import javax.persistence.UniqueConstraint;

import csns.model.academics.AcademicStanding;
import csns.model.core.User;

@Entity
@Table(name = "prereg_registrations",
    uniqueConstraints = @UniqueConstraint(
        columnNames = { "student_id", "schedule_id" }) )
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "reg_limit", nullable = false)
    private int regLimit;

    @ManyToMany
    @JoinTable(name = "prereg_registration_sections",
        joinColumns = @JoinColumn(name = "registration_id") ,
        inverseJoinColumns = @JoinColumn(name = "section_id") )
    private Set<Section> sections;

    private String comments;

    @Column(nullable = false)
    private Date date;

    public Registration()
    {
        regLimit = 5;
        date = new Date();
        sections = new HashSet<Section>();
    }

    public Registration( User student, Schedule schedule )
    {
        this();
        this.student = student;
        this.schedule = schedule;

        AcademicStanding academicStanding = student
            .getCurrentStanding( schedule.getDepartment() );
        if( academicStanding != null
            && academicStanding.getStanding().getSymbol().startsWith( "G" ) )
            regLimit = schedule.getDefaultGradRegLimit();
        else
            regLimit = schedule.getDefaultUndergradRegLimit();
    }

    public int getNumOfClasses()
    {
        int n = sections.size();
        for( Section section : sections )
            if( section.getLinkedBy() != null ) --n;

        return n;
    }

    public Section removeSection( Long sectionId )
    {
        for( Section section : sections )
            if( section.getId().equals( sectionId ) )
            {
                sections.remove( section );
                return section;
            }

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

    public User getStudent()
    {
        return student;
    }

    public void setStudent( User student )
    {
        this.student = student;
    }

    public Schedule getSchedule()
    {
        return schedule;
    }

    public void setSchedule( Schedule schedule )
    {
        this.schedule = schedule;
    }

    public int getRegLimit()
    {
        return regLimit;
    }

    public void setRegLimit( int regLimit )
    {
        this.regLimit = regLimit;
    }

    public Set<Section> getSections()
    {
        return sections;
    }

    public void setSections( Set<Section> sections )
    {
        this.sections = sections;
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

}
