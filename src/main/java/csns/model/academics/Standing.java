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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "standings")
public class Standing implements Serializable, Comparable<Standing> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String symbol;

    private String name;

    private String description;

    /** The mailing lists a student with this standing should subscribe. */
    @ElementCollection
    @CollectionTable(name = "standing_mailinglists",
        joinColumns = @JoinColumn(name = "standing_id"))
    @Column(name = "mailinglist")
    private Set<String> mailinglists;

    public Standing()
    {
        mailinglists = new HashSet<String>();
    }

    @Override
    public int compareTo( Standing standing )
    {
        if( standing == null )
            throw new IllegalArgumentException( "Cannot compare to NULL." );

        return id.compareTo( standing.getId() );
    }

    @Override
    public String toString()
    {
        return symbol;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol( String symbol )
    {
        this.symbol = symbol;
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

    public Set<String> getMailinglists()
    {
        return mailinglists;
    }

    public void setMailinglists( Set<String> mailinglists )
    {
        this.mailinglists = mailinglists;
    }

}
