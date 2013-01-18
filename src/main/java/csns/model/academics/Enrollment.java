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
@Table(name = "enrollments",
    uniqueConstraints = @UniqueConstraint(columnNames = { "section_id",
        "student_id" }))
public class Enrollment implements Serializable, Comparable<Enrollment> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;

    private String comments;

    @Column(name = "grade_mailed", nullable = false)
    private boolean gradeMailed;

    public Enrollment()
    {
        gradeMailed = false;
    }

    public Enrollment( Section section, User student )
    {
        this();
        this.section = section;
        this.student = student;
    }

    @Override
    public int compareTo( Enrollment enrollment )
    {
        if( enrollment == null )
            throw new IllegalArgumentException( "Cannot compare to NULL." );

        int cmp = section.compareTo( enrollment.section );
        if( cmp != 0 ) return cmp;

        return student.compareTo( enrollment.student );
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Section getSection()
    {
        return section;
    }

    public void setSection( Section section )
    {
        this.section = section;
    }

    public User getStudent()
    {
        return student;
    }

    public void setStudent( User student )
    {
        this.student = student;
    }

    public Grade getGrade()
    {
        return grade;
    }

    public void setGrade( Grade grade )
    {
        this.grade = grade;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public boolean isGradeMailed()
    {
        return gradeMailed;
    }

    public void setGradeMailed( boolean gradeMailed )
    {
        this.gradeMailed = gradeMailed;
    }

}
