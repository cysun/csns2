/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2017, Chengyu Sun (csun@calstatela.edu).
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import csns.model.assessment.CourseJournal;
import csns.model.core.File;
import csns.model.core.User;
import csns.model.forum.Forum;

@Entity
@Table(name = "courses")
public class Course implements Serializable, Comparable<Course> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "units", nullable = false)
    private int units;

    @Column(name = "unit_factor", nullable = false)
    private double unitFactor;

    @ManyToOne
    @JoinColumn(name = "coordinator_id")
    private User coordinator;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "description_id")
    private File description;

    @Column(name = "catalog_description")
    private String catalogDescription;

    @ManyToMany
    @JoinTable(name = "course_prerequisites",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_id"),
        uniqueConstraints = { @UniqueConstraint(
            columnNames = { "course_id", "prerequisite_id" }) })
    @OrderBy("code asc")
    private List<Course> prerequisites;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", unique = true)
    private CourseJournal journal;

    @OneToOne(mappedBy = "course")
    private Forum forum;

    @Column(nullable = false)
    private boolean obsolete;

    public Course()
    {
        prerequisites = new ArrayList<Course>();
        units = 3;
        unitFactor = 1.0;
        obsolete = false;
    }

    @Override
    public int compareTo( Course course )
    {
        if( course == null )
            throw new IllegalArgumentException( "Cannot compare to NULL." );

        return getCode().compareTo( course.getCode() );
    }

    public String getDept()
    {
        int index;
        for( index = 0; index < code.length(); ++index )
            if( Character.isDigit( code.charAt( index ) ) ) break;

        return code.substring( 0, index ).toLowerCase();
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

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getUnits()
    {
        return units;
    }

    public void setUnits( int units )
    {
        this.units = units;
    }

    public double getUnitFactor()
    {
        return unitFactor;
    }

    public void setUnitFactor( double unitFactor )
    {
        this.unitFactor = unitFactor;
    }

    public User getCoordinator()
    {
        return coordinator;
    }

    public void setCoordinator( User coordinator )
    {
        this.coordinator = coordinator;
    }

    public File getDescription()
    {
        return description;
    }

    public void setDescription( File description )
    {
        this.description = description;
    }

    public String getCatalogDescription()
    {
        return catalogDescription;
    }

    public void setCatalogDescription( String catalogDescription )
    {
        this.catalogDescription = catalogDescription;
    }

    public List<Course> getPrerequisites()
    {
        return prerequisites;
    }

    public void setPrerequisites( List<Course> prerequisites )
    {
        this.prerequisites = prerequisites;
    }

    public CourseJournal getJournal()
    {
        return journal;
    }

    public void setJournal( CourseJournal journal )
    {
        this.journal = journal;
    }

    public Forum getForum()
    {
        return forum;
    }

    public void setForum( Forum forum )
    {
        this.forum = forum;
    }

    public boolean isObsolete()
    {
        return obsolete;
    }

    public void setObsolete( boolean obsolete )
    {
        this.obsolete = obsolete;
    }

}
