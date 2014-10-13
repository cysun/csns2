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
package csns.model.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import csns.model.core.Resource;
import csns.model.core.ResourceType;

@Entity
@Table(name = "site_items")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "site_item_additional_resources",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @OrderBy("name asc")
    private List<Resource> additionalResources;

    @Column(nullable = false)
    private boolean hidden;

    public Item()
    {
        hidden = false;
        resource = new Resource( ResourceType.FILE );
        additionalResources = new ArrayList<Resource>();
    }

    public Item clone()
    {
        Item newItem = new Item();
        newItem.hidden = true;

        newItem.resource = resource.clone();
        for( Resource additionalResource : additionalResources )
            newItem.additionalResources.add( additionalResource.clone() );

        return newItem;
    }

    public String getName()
    {
        return resource.getName();
    }

    public boolean toggle()
    {
        hidden = !hidden;
        return hidden;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Resource getResource()
    {
        return resource;
    }

    public void setResource( Resource resource )
    {
        this.resource = resource;
    }

    public List<Resource> getAdditionalResources()
    {
        return additionalResources;
    }

    public void setAdditionalResources( List<Resource> additionalResources )
    {
        this.additionalResources = additionalResources;
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
