/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Chengyu Sun (csun@calstatela.edu).
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
package csns.importer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import csns.importer.parser.UserListParser;
import csns.model.core.Group;
import csns.model.core.Member;
import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Component
@Scope("prototype")
public class UsersImporter {

    @Autowired
    UserDao userDao;

    @Autowired
    @Qualifier("usersParser")
    UserListParser usersParser;

    String text;

    List<ImportedUser> importedUsers;

    public UsersImporter()
    {
        importedUsers = new ArrayList<ImportedUser>();
    }

    public void checkAccountStatus()
    {
        for( ImportedUser importedUser : importedUsers )
        {
            User user = userDao.getUserByCin( importedUser.getCin() );
            importedUser.setNewAccount( user == null );
        }
    }

    public void checkMemberStatus( Group group )
    {
        Set<String> cins = new HashSet<String>();
        for( Member member : group.getMembers() )
            cins.add( member.getUser().getCin() );

        for( ImportedUser importedUser : importedUsers )
            importedUser
                .setNewMember( !cins.contains( importedUser.getCin() ) );
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        if( StringUtils.hasText( text ) )
        {
            this.text = text;
            importedUsers = usersParser.parse( text );
        }
    }

    public List<ImportedUser> getImportedUsers()
    {
        return importedUsers;
    }

    public void setImportedUsers( List<ImportedUser> importedUsers )
    {
        this.importedUsers = importedUsers;
    }

}
