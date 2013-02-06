/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.academics;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Where;

import csns.model.core.User;

@Entity
@Table(name = "sections", uniqueConstraints = @UniqueConstraint(columnNames = {
    "quarter", "course_id", "number" }))
public class Section implements Serializable, Comparable<Section> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "code",
        column = @Column(name = "quarter", nullable = false)) })
    private Quarter quarter;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private int number;

    @ManyToMany
    @JoinTable(name = "section_instructors",
        joinColumns = @JoinColumn(name = "section_id"),
        inverseJoinColumns = @JoinColumn(name = "instructor_id"))
    @OrderColumn(name = "instructor_order")
    private List<User> instructors;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "section", cascade = { CascadeType.MERGE,
        CascadeType.PERSIST })
    @OrderBy("name asc")
    @Where(clause = "deleted='f'")
    private List<Assignment> assignments;

    @Column(nullable = false)
    private boolean deleted;

    public Section()
    {
        number = 1;
        instructors = new ArrayList<User>();
        enrollments = new ArrayList<Enrollment>();
        deleted = false;
    }

    @Override
    public int compareTo( Section section )
    {
        if( section == null )
            throw new IllegalArgumentException( "Cannot compare to NULL." );

        if( id.equals( section.id ) ) return 0;

        int cmp = getQuarter().getCode() - section.getQuarter().getCode();
        if( cmp != 0 ) return cmp;

        cmp = getCourse().getCode().compareTo( section.getCourse().getCode() );
        if( cmp != 0 ) return cmp;

        return getNumber() - section.getNumber();
    }

    public Enrollment getEnrollment( User user )
    {
        for( Enrollment enrollment : enrollments )
            if( enrollment.getStudent().getId().equals( user.getId() ) )
                return enrollment;

        return null;
    }

    public boolean isEnrolled( User user )
    {
        return getEnrollment( user ) != null;
    }

    public boolean isInstructor( User user )
    {
        for( User instructor : instructors )
            if( instructor.getId().equals( user.getId() ) ) return true;

        return false;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Quarter getQuarter()
    {
        return quarter;
    }

    public void setQuarter( Quarter quarter )
    {
        this.quarter = quarter;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse( Course course )
    {
        this.course = course;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber( int number )
    {
        this.number = number;
    }

    public List<User> getInstructors()
    {
        return instructors;
    }

    public void setInstructors( List<User> instructors )
    {
        this.instructors = instructors;
    }

    public List<Enrollment> getEnrollments()
    {
        return enrollments;
    }

    public void setEnrollments( List<Enrollment> enrollments )
    {
        this.enrollments = enrollments;
    }

    public List<Assignment> getAssignments()
    {
        return assignments;
    }

    public void setAssignments( List<Assignment> assignments )
    {
        this.assignments = assignments;
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