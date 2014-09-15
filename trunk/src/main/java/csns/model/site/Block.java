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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "site_blocks")
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        REGULAR, ANNOUNCEMENTS, ASSIGNMENTS, EXAMS, QUIZZES, EXERCISES
    };

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // Used to identify special blocks like Announcements and Assignments.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @OneToMany
    @JoinColumn(name = "block_id")
    @OrderColumn(name = "item_index")
    private List<Item> items;

    @Column(nullable = false)
    private boolean hidden;

    public Block()
    {
        type = Type.REGULAR;
        hidden = false;
    }

    public Block( String name )
    {
        this( name, Type.REGULAR );
    }

    public Block( String name, Type type )
    {
        this();
        this.name = name;
        this.type = type;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Type getType()
    {
        return type;
    }

    public void setType( Type type )
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public void setItems( List<Item> items )
    {
        this.items = items;
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
