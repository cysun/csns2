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
package csns.web.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.forum.dao.ForumDao;
import csns.security.SecurityUtils;
import csns.web.validator.ForumValidator;

@Controller
@SessionAttributes("forum")
public class ForumControllerS {

    @Autowired
    private ForumDao forumDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @Autowired
    private ForumValidator forumValidator;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger( ForumControllerS.class );

    @RequestMapping("/department/{dept}/forum/addMember")
    public String addMember( @ModelAttribute Forum forum,
        @RequestParam Long userId, HttpServletResponse response )
        throws JsonGenerationException, JsonMappingException, IOException
    {
        User user = userDao.getUser( userId );
        forum.getMembers().add( user );
        if( forum.getId() != null ) subscriptionDao.subscribe( forum, user );

        logger.info( SecurityUtils.getUser().getUsername() + " added member "
            + userId + " to forum " + forum.getId() );

        // We have to use ObjectMapper here instead of Spring's JsonView so the
        // session attribute "forum" doesn't get serialized.
        response.setContentType( "application/json" );
        objectMapper.writeValue( response.getWriter(), user );
        return null;
    }

    @RequestMapping("/department/{dept}/forum/removeMember")
    @ResponseStatus(HttpStatus.OK)
    public void removeMember( @ModelAttribute Forum forum,
        @RequestParam Long userId )
    {
        User user = userDao.getUser( userId );
        forum.removeMember( user );
        if( forum.getId() != null ) subscriptionDao.unsubscribe( forum, user );

        logger.info( SecurityUtils.getUser().getUsername() + " removed member "
            + userId + " from forum " + forum.getId() );
    }

    @RequestMapping(value = "/department/{dept}/forum/create",
        method = RequestMethod.GET)
    public String create( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        Forum forum = new Forum();
        forum.setDepartment( department );
        models.put( "forum", forum );
        return "forum/create";
    }

    @RequestMapping(value = "/department/{dept}/forum/create",
        method = RequestMethod.POST)
    public String create( @ModelAttribute Forum forum,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        forumValidator.validate( forum, bindingResult );
        if( bindingResult.hasErrors() ) return "forum/create";

        forum.setDate( new Date() );
        forum = forumDao.saveForum( forum );
        sessionStatus.setComplete();

        if( forum.isMembersOnly() )
        {
            for( User member : forum.getMembers() )
                subscriptionDao.subscribe( forum, member );
            for( User admin : forum.getDepartment().getAdministrators() )
                subscriptionDao.subscribe( forum, admin );
        }

        logger.info( SecurityUtils.getUser().getUsername() + " created forum "
            + forum.getId() );

        return "redirect:view?id=" + forum.getId();
    }

    @RequestMapping(value = "/department/{dept}/forum/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "forum", forumDao.getForum( id ) );
        return "forum/edit";
    }

    @RequestMapping(value = "/department/{dept}/forum/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Forum forum,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        forumValidator.validate( forum, bindingResult );
        if( bindingResult.hasErrors() ) return "forum/edit";

        forum.setDate( new Date() );
        forum = forumDao.saveForum( forum );
        sessionStatus.setComplete();

        logger.info( SecurityUtils.getUser().getUsername() + " edited forum "
            + forum.getId() );

        return "redirect:view?id=" + forum.getId();
    }

}
