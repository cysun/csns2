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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import csns.model.core.User;

@Entity
@Table(name = "academic_standings",
    uniqueConstraints = @UniqueConstraint(columnNames = { "student_id",
        "department_id", "standing_id" }))
public class AcademicStanding implements Serializable,
    Comparable<AcademicStanding> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "standing_id")
    private Standing standing;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "code",
        column = @Column(name = "quarter")) })
    private Quarter quarter;

    public AcademicStanding()
    {
    }

    public AcademicStanding( User student, Department department,
        Standing standing, Quarter quarter )
    {
        this.student = student;
        this.department = department;
        this.standing = standing;
        this.quarter = quarter;
    }

    @Override
    public int compareTo( AcademicStanding academicStanding )
    {
        if( academicStanding == null )
            throw new IllegalArgumentException( "Cannot compare to NULL." );

        int cmp = department.getName().compareTo(
            academicStanding.department.getName() );
        if( cmp == 0 ) cmp = quarter.compareTo( academicStanding.quarter );
        if( cmp == 0 ) cmp = standing.compareTo( academicStanding.standing );

        return cmp;
    }

    @Override
    public String toString()
    {
        return "[" + student.getCin() + ", " + department.getAbbreviation()
            + ", " + standing.getSymbol() + ", " + quarter.getShortString()
            + "]";
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

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public Standing getStanding()
    {
        return standing;
    }

    public void setStanding( Standing standing )
    {
        this.standing = standing;
    }

    public Quarter getQuarter()
    {
        return quarter;
    }

    public void setQuarter( Quarter quarter )
    {
        this.quarter = quarter;
    }

}
