/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Chengyu Sun (csun@calstatela.edu).
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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import csns.model.core.User;

@Entity
@Table(name = "prereg_section_registrations",
    uniqueConstraints = @UniqueConstraint(
        columnNames = { "student_id", "section_id" }) )
public class SectionRegistration implements Serializable, Registration {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_registration_id")
    private ScheduleRegistration scheduleRegistration;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false)
    private Date date;

    @Column(name = "prereq_met")
    private Boolean prereqMet;

    public SectionRegistration()
    {
        date = new Date();
    }

    public SectionRegistration( ScheduleRegistration scheduleRegistration,
        User student, Section section )
    {
        this();
        this.scheduleRegistration = scheduleRegistration;
        this.student = student;
        this.section = section;
    }

    @Override
    public String getComments()
    {
        return scheduleRegistration.getComments();
    }

    @Override
    public List<SectionRegistration> getSectionRegistrations()
    {
        return scheduleRegistration.getSectionRegistrations();
    }

    public String getSectionString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "[" )
            .append( section.getCourse().getCode() )
            .append( "-" )
            .append( section.getSectionNumber() );
        if( section.getLinkedBy() != null ) sb.append( " " )
            .append( section.getLinkedBy().getCourse().getCode() )
            .append( "-" )
            .append( section.getLinkedBy().getSectionNumber() );
        sb.append( "]" );

        return sb.toString();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public ScheduleRegistration getScheduleRegistration()
    {
        return scheduleRegistration;
    }

    public void setScheduleRegistration(
        ScheduleRegistration scheduleRegistration )
    {
        this.scheduleRegistration = scheduleRegistration;
    }

    public User getStudent()
    {
        return student;
    }

    public void setStudent( User student )
    {
        this.student = student;
    }

    public Section getSection()
    {
        return section;
    }

    public void setSection( Section section )
    {
        this.section = section;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public Boolean getPrereqMet()
    {
        return prereqMet;
    }

    public void setPrereqMet( Boolean prereqMet )
    {
        this.prereqMet = prereqMet;
    }

}
