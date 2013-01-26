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
package csns.helper;

import java.util.ArrayList;
import java.util.List;

import csns.model.core.AbstractMessage;
import csns.model.core.User;

public class Email extends AbstractMessage {

    private static final long serialVersionUID = 1L;

    private List<User> recipients;

    private boolean useSecondaryEmail;

    public Email()
    {
        recipients = new ArrayList<User>();
        useSecondaryEmail = false;
    }

    public void addRecipient( User user )
    {
        recipients.add( user );
    }

    public List<User> getRecipients()
    {
        return recipients;
    }

    public void setRecipients( List<User> recipients )
    {
        this.recipients = recipients;
    }

    public boolean isUseSecondaryEmail()
    {
        return useSecondaryEmail;
    }

    public void setUseSecondaryEmail( boolean useSecondaryEmail )
    {
        this.useSecondaryEmail = useSecondaryEmail;
    }

}
