/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.assessment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import csns.model.academics.Assignment;
import csns.model.academics.Department;
import csns.model.academics.Section;
import csns.model.core.Publishable;
import csns.model.core.User;

@Entity
@Table(name = "rubrics")
public class Rubric implements Publishable, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "scale", nullable = false)
    private int scale;

    /* Each rubric has a number of performance indicators. */
    @OneToMany(mappedBy = "rubric")
    @OrderColumn(name = "indicator_order")
    private List<RubricIndicator> indicators;

    /* There are two types of rubrics: personal and department. */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "publish_date")
    private Calendar publishDate;

    @Column(name = "public", nullable = false)
    private boolean isPublic;

    // Sections that use this rubric.
    @ManyToMany(mappedBy = "rubrics")
    private Set<Section> sections;

    // Assignments that use this rubric.
    @ManyToMany(mappedBy = "rubrics")
    private Set<Assignment> assignments;

    private boolean deleted;

    public Rubric()
    {
        scale = 5;
        indicators = new ArrayList<RubricIndicator>();
        deleted = false;
    }

    // for Publishable
    @Override
    public boolean isPublished()
    {
        return publishDate != null
            && Calendar.getInstance().after( publishDate );
    }

    // for Publishable
    @Override
    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
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

    public int getScale()
    {
        return scale;
    }

    public void setScale( int scale )
    {
        this.scale = scale;
    }

    public List<RubricIndicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( List<RubricIndicator> indicators )
    {
        this.indicators = indicators;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public User getCreator()
    {
        return creator;
    }

    public void setCreator( User creator )
    {
        this.creator = creator;
    }

    // for Publishable
    @Override
    public Calendar getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate( Calendar publishDate )
    {
        this.publishDate = publishDate;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public void setPublic( boolean isPublic )
    {
        this.isPublic = isPublic;
    }

    public Set<Section> getSections()
    {
        return sections;
    }

    public void setSections( Set<Section> sections )
    {
        this.sections = sections;
    }

    public Set<Assignment> getAssignments()
    {
        return assignments;
    }

    public void setAssignments( Set<Assignment> assignments )
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
