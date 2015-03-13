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
package csns.model.academics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "programs")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToMany
    @JoinTable(name = "program_required_courses",
        joinColumns = @JoinColumn(name = "program_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"),
        uniqueConstraints = { @UniqueConstraint(columnNames = { "program_id",
            "course_id" }) })
    @OrderBy("code asc")
    private List<Course> requiredCourses;

    @ManyToMany
    @JoinTable(name = "program_elective_courses",
        joinColumns = @JoinColumn(name = "program_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"),
        uniqueConstraints = { @UniqueConstraint(columnNames = { "program_id",
            "course_id" }) })
    @OrderBy("code asc")
    private List<Course> electiveCourses;

    public Program()
    {
        requiredCourses = new ArrayList<Course>();
        electiveCourses = new ArrayList<Course>();
    }

    public Program( Department department )
    {
        this();
        this.department = department;
    }

    public Program clone()
    {
        Program program = new Program( department );

        program.name = "Copy of " + name;
        program.description = description;
        program.getRequiredCourses().addAll( requiredCourses );
        program.getElectiveCourses().addAll( electiveCourses );

        return program;
    }

    public boolean contains( Course course )
    {
        for( Course c : requiredCourses )
            if( c.getId().equals( course.getId() ) ) return true;

        for( Course c : electiveCourses )
            if( c.getId().equals( course.getId() ) ) return true;

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

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public List<Course> getRequiredCourses()
    {
        return requiredCourses;
    }

    public void setRequiredCourses( List<Course> requiredCourses )
    {
        this.requiredCourses = requiredCourses;
    }

    public List<Course> getElectiveCourses()
    {
        return electiveCourses;
    }

    public void setElectiveCourses( List<Course> electiveCourses )
    {
        this.electiveCourses = electiveCourses;
    }

}
