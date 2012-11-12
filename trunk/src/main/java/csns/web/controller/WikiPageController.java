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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.HandlerMapping;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.wiki.Page;
import csns.model.wiki.Revision;
import csns.model.wiki.dao.PageDao;
import csns.model.wiki.dao.RevisionDao;
import csns.security.SecurityUtils;
import csns.util.NotificationService;
import csns.web.validator.MessageValidator;

@Controller
@SessionAttributes("revision")
public class WikiPageController {

    @Autowired
    PageDao pageDao;

    @Autowired
    RevisionDao revisionDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    SubscriptionDao subscriptionDao;

    @Autowired
    MessageValidator messageValidator;

    @Autowired
    NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger( WikiPageController.class );

    private Department getDepartment( String path )
    {
        String prefix = "/wiki/content/department/";

        if( !path.startsWith( prefix ) ) return null;

        int beginIndex = prefix.length();
        int endIndex = path.indexOf( '/', beginIndex );
        if( endIndex == -1 ) endIndex = path.length();
        String dept = path.substring( beginIndex, endIndex );

        return departmentDao.getDepartment( dept );
    }

    @RequestMapping("/wiki/content/**")
    public String view( @RequestParam(required = false) Long revisionId,
        HttpServletRequest request, ModelMap models )
    {
        String path = (String) request.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE );
        models.put( "path", path );

        Revision revision = revisionId == null ? revisionDao.getRevision( path )
            : revisionDao.getRevision( revisionId );
        if( revision == null ) return "wiki/nopage";

        if( StringUtils.hasText( revision.getPage().getPassword() )
            && request.getSession().getAttribute( path ) == null )
            return "wiki/password";

        revision.getPage().incrementViews();
        pageDao.savePage( revision.getPage() );
        models.put( "revision", revision );

        Department department = getDepartment( path );
        if( revision.isIncludeSidebar() )
        {
            String sidebar = department != null ? "/wiki/content/department/"
                + department.getAbbreviation() + "/sidebar"
                : "/wiki/content/sidebar";
            models.put( "sidebar", revisionDao.getRevision( sidebar ) );
        }

        if( SecurityUtils.isAuthenticated() )
        {
            User user = SecurityUtils.getUser();
            boolean isAdmin = department == null ? user.isSysadmin()
                : user.isAdmin( department.getAbbreviation() );
            models.put( "user", user );
            models.put( "isAdmin", isAdmin );

            Subscription subscription = subscriptionDao.getSubscription(
                revision.getPage(), user );
            if( subscription != null )
            {
                if( subscription.isNotificationSent() )
                {
                    subscription.setNotificationSent( false );
                    subscription = subscriptionDao.saveSubscription( subscription );
                }
                models.put( "subscription", subscription );
            }
        }

        return "wiki/page";
    }

    @RequestMapping(value = "/wiki/password", method = RequestMethod.POST)
    public String password( @RequestParam String path,
        @RequestParam String password, HttpSession session )
    {
        if( pageDao.getPage( path ).getPassword().equals( password ) )
            session.setAttribute( path, "password verifeid" );

        return "redirect:" + path;
    }

    @RequestMapping(value = "/wiki/edit", method = RequestMethod.GET)
    public String edit( @RequestParam String path, ModelMap models,
        HttpSession session )
    {
        Revision revision = revisionDao.getRevision( path );

        if( revision != null
            && StringUtils.hasText( revision.getPage().getPassword() )
            && session.getAttribute( path ) == null ) return "wiki/password";

        revision = revision == null ? new Revision( new Page( path ) )
            : revision.clone();
        models.put( "revision", revision );
        return "wiki/edit";
    }

    @RequestMapping(value = "/wiki/edit", method = RequestMethod.POST)
    public String edit( @ModelAttribute Revision revision, HttpSession session,
        BindingResult result, SessionStatus sessionStatus )
    {
        // NOTE It is possible to bypass both password and locked here, but it
        // will take too much code to prevent that. Considering this is wiki we
        // are taking about here, I'm choosing simplicity over security.

        messageValidator.validate( revision, result );
        if( result.hasErrors() ) return "wiki/edit";

        User user = SecurityUtils.getUser();
        revision.setAuthor( user );
        revision.setDate( new Date() );
        Page page = revision.getPage();
        if( page.getId() == null ) page.setOwner( user );
        page = revisionDao.saveRevision( revision ).getPage();
        sessionStatus.setComplete();

        logger.info( "Wiki page " + page.getPath() + " edited by "
            + user.getUsername() );

        subscriptionDao.subscribe( page, user );

        String subject = "New Revision of Wiki Page " + page.getPath();
        String vTemplate = "notification.new.wiki.revision.vm";
        Map<String, Object> vModels = new HashMap<String, Object>();
        vModels.put( "page", page );
        vModels.put( "author", user );
        notificationService.notifiy( page, subject, vTemplate, vModels, true );

        return "redirect:" + page.getPath();
    }

}
