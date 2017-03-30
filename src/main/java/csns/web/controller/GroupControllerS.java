/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016-2017, Chengyu Sun (csun@calstatela.edu).
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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;

import csns.importer.ImportedUser;
import csns.importer.UsersImporter;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.Group;
import csns.model.core.Member;
import csns.model.core.User;
import csns.model.core.dao.GroupDao;
import csns.model.core.dao.MemberDao;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.web.validator.GroupValidator;

@Controller
@SessionAttributes({ "group", "importer" })
public class GroupControllerS {

    private static final Logger logger = LoggerFactory
        .getLogger( GroupControllerS.class );

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private GroupValidator groupValidator;

    @Autowired
    private WebApplicationContext context;

    @RequestMapping(value = "/department/{dept}/group/create",
        method = RequestMethod.GET)
    public String create( @PathVariable String dept, ModelMap models )
    {
        models.put( "group", new Group( departmentDao.getDepartment( dept ) ) );
        return "group/create";
    }

    @RequestMapping(value = "/department/{dept}/group/create",
        method = RequestMethod.POST)
    public String create( @ModelAttribute Group group, BindingResult result,
        SessionStatus sessionStatus )
    {
        groupValidator.validate( group, result );
        if( result.hasErrors() ) return "group/create";

        group.setDate( new Date() );
        group = groupDao.saveGroup( group );
        logger.info( SecurityUtils.getUser().getUsername() + " created group "
            + group.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + group.getId();
    }

    @RequestMapping(value = "/department/{dept}/group/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "group", groupDao.getGroup( id ) );
        return "group/edit";
    }

    @RequestMapping(value = "/department/{dept}/group/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Group group, BindingResult result,
        SessionStatus sessionStatus )
    {
        groupValidator.validate( group, result );
        if( result.hasErrors() ) return "group/edit";

        group.setDate( new Date() );
        group = groupDao.saveGroup( group );
        logger.info( SecurityUtils.getUser().getUsername() + " edited group "
            + group.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + group.getId();
    }

    @RequestMapping(value = "/department/{dept}/group/import",
        method = RequestMethod.GET)
    public String importUsers( @RequestParam Long id, ModelMap models )
    {
        UsersImporter importer = (UsersImporter) context
            .getBean( "usersImporter" );
        models.put( "importer", importer );
        models.put( "group", groupDao.getGroup( id ) );
        return "group/import0";
    }

    @RequestMapping(value = "/department/{dept}/group/import",
        method = RequestMethod.POST)
    public String importUsers(
        @ModelAttribute("importer") UsersImporter importer,
        @RequestParam("_page") int currentPage,
        @RequestParam(value = "_target", required = false) Integer targetPage,
        @RequestParam Long id, HttpServletRequest request,
        SessionStatus sessionStatus )
    {
        Group group = groupDao.getGroup( id );

        if( request.getParameter( "_finish" ) == null )
        {
            if( targetPage == 1 && currentPage < targetPage )
            {
                importer.checkAccountStatus();
                importer.checkMemberStatus( group );
            }
            return "group/import" + targetPage;
        }

        // received _finish, so do the import.
        int count = 0;
        for( ImportedUser importedUser : importer.getImportedUsers() )
        {
            if( !importedUser.isNewMember() ) continue;

            boolean isNewUser = false;
            String cin = importedUser.getCin();
            User user = userDao.getUserByCin( cin );
            if( user == null )
            {
                isNewUser = true;
                user = SecurityUtils.createTemporaryAccount( cin,
                    importedUser.getFirstName(), importedUser.getLastName() );
                user = userDao.saveUser( user );
                logger.info( "New account created for user " + user.getName() );
            }

            if( isNewUser || memberDao.getMember( group, user ) == null )
            {
                memberDao.saveMember( new Member( group, user ) );
                ++count;
            }
        }

        if( count > 0 )
        {
            group.setDate( new Date() );
            group = groupDao.saveGroup( group );
        }

        logger.info( SecurityUtils.getUser().getUsername() + " imported "
            + count + " users to group " + id );

        sessionStatus.setComplete();
        return "redirect:view?id=" + id;
    }

}
