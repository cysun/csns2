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
package csns.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.mailinglist.Mailinglist;
import csns.model.mailinglist.dao.MailinglistDao;
import csns.model.mailinglist.dao.MessageDao;

@Controller
public class MailinglistMessageController {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MailinglistDao mailinglistDao;

    @RequestMapping("/department/{dept}/mailinglist/message/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "message", messageDao.getMessage( id ) );
        return "mailinglist/message/view";
    }

    @RequestMapping("/department/{dept}/mailinglist/message/search")
    public String search( @RequestParam Long listId, @RequestParam String term,
        ModelMap models )
    {
        Mailinglist mailinglist = mailinglistDao.getMailinglist( listId );
        models.put( "mailinglist", mailinglist );
        models.put( "messages",
            messageDao.searchMessages( mailinglist, term, 40 ) );
        return "mailinglist/message/search";
    }

}
