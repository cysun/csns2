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
package csns.model.forum;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import csns.model.core.AbstractMessage;
import csns.model.core.User;

@Entity
@Table(name = "forum_posts")
@AssociationOverride(name = "attachments",
    joinTable = @JoinTable(name = "forum_post_attachments",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "file_id")))
public class Post extends AbstractMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "edited_by")
    private User editedBy;

    @Column(name = "edit_date")
    private Date editDate;

    public Post()
    {
        super();
    }

    public Post( Topic topic )
    {
        super();
        this.topic = topic;
    }

    public Topic getTopic()
    {
        return topic;
    }

    public void setTopic( Topic topic )
    {
        this.topic = topic;
    }

    public User getEditedBy()
    {
        return editedBy;
    }

    public void setEditedBy( User editedBy )
    {
        this.editedBy = editedBy;
    }

    public Date getEditDate()
    {
        return editDate;
    }

    public void setEditDate( Date editDate )
    {
        this.editDate = editDate;
    }

}
