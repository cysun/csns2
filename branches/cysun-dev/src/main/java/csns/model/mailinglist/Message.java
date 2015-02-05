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
package csns.model.mailinglist;

import javax.persistence.AssociationOverride;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import csns.model.core.AbstractMessage;

@Entity
@Table(name = "mailinglist_messages")
@AssociationOverride(name = "attachments",
    joinTable = @JoinTable(name = "mailinglist_message_attachments",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "file_id")))
public class Message extends AbstractMessage {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "mailinglist_id")
    private Mailinglist mailinglist;

    public Message()
    {
        super();
    }

    public Message( Mailinglist mailinglist )
    {
        super();
        this.mailinglist = mailinglist;
    }

    public Mailinglist getMailinglist()
    {
        return mailinglist;
    }

    public void setMailinglist( Mailinglist mailinglist )
    {
        this.mailinglist = mailinglist;
    }

}
