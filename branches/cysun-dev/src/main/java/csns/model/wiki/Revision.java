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

import javax.persistence.AssociationOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import csns.model.core.AbstractMessage;

@Entity
@Table(name = "wiki_revisions")
@AssociationOverride(name = "attachments",
    joinTable = @JoinTable(name = "wiki_revision_attachments",
        joinColumns = @JoinColumn(name = "revision_id"),
        inverseJoinColumns = @JoinColumn(name = "file_id")))
public class Revision extends AbstractMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;

    @Column(name = "include_sidebar", nullable = false)
    private boolean includeSidebar;

    public Revision()
    {
        super();
        includeSidebar = false;
    }

    public Revision( Page page )
    {
        this();
        this.page = page;
    }

    public Revision clone()
    {
        Revision newRevision = new Revision();
        newRevision.subject = subject;
        newRevision.content = content;
        newRevision.includeSidebar = includeSidebar;
        newRevision.page = page;

        return newRevision;
    }

    public Page getPage()
    {
        return page;
    }

    public void setPage( Page page )
    {
        this.page = page;
    }

    public boolean isIncludeSidebar()
    {
        return includeSidebar;
    }

    public void setIncludeSidebar( boolean includeSidebar )
    {
        this.includeSidebar = includeSidebar;
    }

}
