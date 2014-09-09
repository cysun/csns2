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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import csns.model.academics.Section;

@Entity
@Table(name = "sites")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(mappedBy = "site")
    @OrderBy("date desc")
    private List<Announcement> announcements;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id")
    @OrderColumn(name = "block_index")
    private List<Block> blocks;

    @Column(nullable = false)
    private boolean shared;

    public Site()
    {
        blocks = new ArrayList<Block>();
        blocks.add( new Block( "Announcements", true ) );
        blocks.add( new Block( "Lecture Notes", false ) );
        blocks.add( new Block( "Homework", true ) );

        shared = false;
    }

    public Site( Section section )
    {
        this();
        this.section = section;
    }

    public String getUrl()
    {
        return section == null ? "" : "/site/"
            + section.getQuarter().getShortString().toLowerCase() + "/"
            + section.getCourse().getCode().toLowerCase() + "-"
            + section.getNumber();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Section getSection()
    {
        return section;
    }

    public void setSection( Section section )
    {
        this.section = section;
    }

    public List<Announcement> getAnnouncements()
    {
        return announcements;
    }

    public void setAnnouncements( List<Announcement> announcements )
    {
        this.announcements = announcements;
    }

    public List<Block> getBlocks()
    {
        return blocks;
    }

    public void setBlocks( List<Block> blocks )
    {
        this.blocks = blocks;
    }

    public boolean isShared()
    {
        return shared;
    }

    public void setShared( boolean shared )
    {
        this.shared = shared;
    }

}
