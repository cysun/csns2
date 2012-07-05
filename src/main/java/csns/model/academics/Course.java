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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import csns.model.core.File;
import csns.model.core.User;

@Entity
@Table(name = "courses")
public class Course implements Serializable, Comparable<Course> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "min_units", nullable = false)
    private int minUnits;

    @Column(name = "max_units", nullable = false)
    private int maxUnits;

    @ManyToOne
    @JoinColumn(name = "coordinator_id")
    private User coordinator;

    @OneToOne
    @JoinColumn(name = "syllabus_id")
    private File syllabus;

    @Column(nullable = false)
    private boolean obsolete;

    public Course()
    {
        minUnits = maxUnits = 4;
        obsolete = false;
    }

    @Override
    public int compareTo( Course course )
    {
        if( course == null )
            throw new IllegalArgumentException( "Cannot compare to NULL." );

        return getCode().compareTo( course.getCode() );
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
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

    public int getMinUnits()
    {
        return minUnits;
    }

    public void setMinUnits( int minUnits )
    {
        this.minUnits = minUnits;
    }

    public int getMaxUnits()
    {
        return maxUnits;
    }

    public void setMaxUnits( int maxUnits )
    {
        this.maxUnits = maxUnits;
    }

    public User getCoordinator()
    {
        return coordinator;
    }

    public void setCoordinator( User coordinator )
    {
        this.coordinator = coordinator;
    }

    public File getSyllabus()
    {
        return syllabus;
    }

    public void setSyllabus( File syllabus )
    {
        this.syllabus = syllabus;
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