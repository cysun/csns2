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
package csns.model.wiki;

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

import csns.model.core.Subscribable;
import csns.model.core.User;
import csns.model.forum.Topic;

@Entity
@Table(name = "wiki_pages")
public class Page implements Subscribable, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private int views;

    private String password;

    @ManyToMany
    @JoinTable(name = "wiki_discussions",
        joinColumns = @JoinColumn(name = "page_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id"))
    @OrderBy("id desc")
    private List<Topic> discussions;

    /** A locked page can only be edited by the owner. */
    @Column(nullable = false)
    private boolean locked;

    public Page()
    {
        views = 0;
        discussions = new ArrayList<Topic>();
        locked = false;
    }

    public Page( String path )
    {
        this();
        this.path = path;
    }

    @Override
    public String getType()
    {
        return "Wiki Page";
    }

    @Override
    public String getName()
    {
        return path;
    }

    public int incrementViews()
    {
        return ++views;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getPath()
    {
        return path;
    }

    public User getOwner()
    {
        return owner;
    }

    public void setOwner( User owner )
    {
        this.owner = owner;
    }

    public void setPath( String path )
    {
        this.path = path;
    }

    public int getViews()
    {
        return views;
    }

    public void setViews( int views )
    {
        this.views = views;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public List<Topic> getDiscussions()
    {
        return discussions;
    }

    public void setDiscussions( List<Topic> discussions )
    {
        this.discussions = discussions;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked( boolean locked )
    {
        this.locked = locked;
    }

}
