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
package csns.web.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Component
@Scope("prototype")
public class UserPropertyEditor extends PropertyEditorSupport {

    @Autowired
    UserDao userDao;

    @Override
    public void setAsText( String text ) throws IllegalArgumentException
    {
        if( StringUtils.hasText( text ) )
            setValue( userDao.getUser( Long.valueOf( text ) ) );
    }

    @Override
    public String getAsText()
    {
        User user = (User) getValue();
        return user != null ? user.getId().toString() : "";
    }

}
