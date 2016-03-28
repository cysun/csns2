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
package csns.model.assessment;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import csns.model.core.Resource;

@Entity
@Table(name = "assessment_program_sections")
public class ProgramSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "assessment_program_resources",
        joinColumns = @JoinColumn(name = "section_id") ,
        inverseJoinColumns = @JoinColumn(name = "resource_id") )
    @OrderColumn(name = "resource_index")
    private List<Resource> resources;

    @Column(nullable = false)
    private boolean hidden;

    public ProgramSection()
    {
        name = "";
        resources = new ArrayList<Resource>();
        hidden = false;
    }

    public ProgramSection( String name )
    {
        this();
        setName( name );
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
        if( name != null ) this.name = name;
    }

    public List<Resource> getResources()
    {
        return resources;
    }

    public void setResources( List<Resource> resources )
    {
        this.resources = resources;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden( boolean hidden )
    {
        this.hidden = hidden;
    }

}
