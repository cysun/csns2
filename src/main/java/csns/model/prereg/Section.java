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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import csns.model.academics.Course;

@Entity(name = "PreregSection")
@Table(name = "prereg_sections")
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "section_number", nullable = false)
    private Integer sectionNumber;

    private String type;

    @Column(name = "class_number")
    private String classNumber;

    private String days;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    private String location;

    private String notes;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "linked_by")
    private Section linkedBy;

    @OneToMany(mappedBy = "linkedBy")
    private List<Section> linkedTo;

    @OneToMany(mappedBy = "section")
    @OrderBy("date asc")
    private List<SectionRegistration> registrations;

    public Section()
    {
        capacity = 30;
        linkedTo = new ArrayList<Section>();
        registrations = new ArrayList<SectionRegistration>();
    }

    public Section( Schedule schedule )
    {
        this();
        this.schedule = schedule;
        this.capacity = schedule.getDefaultSectionCapacity();
    }

    // Used for <form:select> in edit.jsp
    public String getShortText()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( course.getCode() )
            .append( "-" )
            .append( sectionNumber )
            .append( " (" )
            .append( type )
            .append( ")" );
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

    public Schedule getSchedule()
    {
        return schedule;
    }

    public void setSchedule( Schedule schedule )
    {
        this.schedule = schedule;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse( Course course )
    {
        this.course = course;
    }

    public Integer getSectionNumber()
    {
        return sectionNumber;
    }

    public void setSectionNumber( Integer sectionNumber )
    {
        this.sectionNumber = sectionNumber;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getClassNumber()
    {
        return classNumber;
    }

    public void setClassNumber( String classNumber )
    {
        this.classNumber = classNumber;
    }

    public String getDays()
    {
        return days;
    }

    public void setDays( String days )
    {
        this.days = days;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime( String startTime )
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime( String endTime )
    {
        this.endTime = endTime;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation( String location )
    {
        this.location = location;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes( String notes )
    {
        this.notes = notes;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity( int capacity )
    {
        this.capacity = capacity;
    }

    public Section getLinkedBy()
    {
        return linkedBy;
    }

    public void setLinkedBy( Section linkedBy )
    {
        this.linkedBy = linkedBy;
    }

    public List<Section> getLinkedTo()
    {
        return linkedTo;
    }

    public void setLinkedTo( List<Section> linkedTo )
    {
        this.linkedTo = linkedTo;
    }

    public List<SectionRegistration> getRegistrations()
    {
        return registrations;
    }

    public void setRegistrations( List<SectionRegistration> registrations )
    {
        this.registrations = registrations;
    }

}
