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
package csns.model.mailinglist.dao;

import java.util.List;

import csns.model.mailinglist.Mailinglist;
import csns.model.mailinglist.Message;

public interface MessageDao {

    Message getMessage( Long id );

    List<Message> getMessagess( Mailinglist mailinglist, int maxResults );

    List<Message> searchMessages( Mailinglist mailinglist, String term,
        int maxResults );

    Message saveMessage( Message message );

}
