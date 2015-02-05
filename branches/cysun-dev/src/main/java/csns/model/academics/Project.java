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
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import csns.model.core.Resource;
import csns.model.core.User;

@Entity
@Table(name = "projects")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private String sponsor;

    @ManyToMany
    @JoinTable(name = "project_students",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id"))
    @OrderBy("lastName asc")
    private List<User> students;

    @ManyToMany
    @JoinTable(name = "project_advisors",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "advisor_id"))
    @OrderBy("lastName asc")
    private List<User> advisors;

    @ManyToMany
    @JoinTable(name = "project_liaisons",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "liaison_id"))
    @OrderBy("lastName asc")
    private List<User> liaisons;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "project_resources",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @OrderColumn(name = "resource_order")
    private List<Resource> resources;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private int year;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean deleted;

    public Project()
    {
        advisors = new ArrayList<User>();
        students = new ArrayList<User>();
        resources = new ArrayList<Resource>();

        year = Calendar.getInstance().get( Calendar.YEAR );
        published = false;
        deleted = false;
    }

    public boolean isMember( User user )
    {
        return isStudent( user ) || isAdvisor( user ) || isLiaison( user );
    }

    public boolean isStudent( User user )
    {
        if( user == null ) return false;

        for( User student : students )
            if( student.getId().equals( user.getId() ) ) return true;

        return false;
    }

    public boolean isAdvisor( User user )
    {
        if( user == null ) return false;

        for( User advisor : advisors )
            if( advisor.getId().equals( user.getId() ) ) return true;

        return false;
    }

    public boolean isLiaison( User user )
    {
        if( user == null ) return false;

        for( User liaison : liaisons )
            if( liaison.getId().equals( user.getId() ) ) return true;

        return false;
    }

    public Resource getResource( Long resourceId )
    {
        for( Resource resource : resources )
            if( resource.getId().equals( resourceId ) ) return resource;

        return null;
    }

    public Resource removeResource( Long resourceId )
    {
        Iterator<Resource> iterator = resources.iterator();
        while( iterator.hasNext() )
        {
            Resource resource = iterator.next();
            if( resource.getId().equals( resourceId ) )
            {
                iterator.remove();
                return resource;
            }
        }

        return null;
    }

    public void replaceResource( Resource resource )
    {
        for( int i = 0; i < resources.size(); ++i )
            if( resources.get( i ).getId().equals( resource.getId() ) )
            {
                resources.set( i, resource );
                return;
            }
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getSponsor()
    {
        return sponsor;
    }

    public void setSponsor( String sponsor )
    {
        this.sponsor = sponsor;
    }

    public List<User> getStudents()
    {
        return students;
    }

    public void setStudents( List<User> students )
    {
        this.students = students;
    }

    public List<User> getAdvisors()
    {
        return advisors;
    }

    public void setAdvisors( List<User> advisors )
    {
        this.advisors = advisors;
    }

    public List<User> getLiaisons()
    {
        return liaisons;
    }

    public void setLiaisons( List<User> liaisons )
    {
        this.liaisons = liaisons;
    }

    public List<Resource> getResources()
    {
        return resources;
    }

    public void setResources( List<Resource> resources )
    {
        this.resources = resources;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear( int year )
    {
        this.year = year;
    }

    public boolean isPublished()
    {
        return published;
    }

    public void setPublished( boolean published )
    {
        this.published = published;
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
