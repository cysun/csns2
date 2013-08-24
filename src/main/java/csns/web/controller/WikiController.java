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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.XslFilter;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.wiki.Page;
import csns.model.wiki.Revision;
import csns.model.wiki.dao.PageDao;
import csns.model.wiki.dao.RevisionDao;
import csns.security.SecurityUtils;
import csns.util.NotificationService;

@Controller
public class WikiController {

    @Autowired
    private PageDao pageDao;

    @Autowired
    private RevisionDao revisionDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger( WikiController.class );

    public static String getDept( String path )
    {
        String prefix = "/wiki/content/department/";

        if( !path.startsWith( prefix ) ) return null;

        int beginIndex = prefix.length();
        int endIndex = path.indexOf( '/', beginIndex );
        return endIndex > 0 ? path.substring( beginIndex, endIndex ) : null;
    }

    // @RequestMapping("/department/{dept}/wiki/content/**")
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

        String dept = getDept( path );
        if( revision.isIncludeSidebar() )
        {
            String sidebar = dept == null ? "/wiki/content/sidebar"
                : "/wiki/content/department/" + dept + "/sidebar";
            models.put( "sidebar", revisionDao.getRevision( sidebar ) );
        }

        if( SecurityUtils.isAuthenticated() )
        {
            User user = SecurityUtils.getUser();
            models.put( "user", user );
            models.put( "isAdmin",
                dept == null ? user.isSysadmin() : user.isAdmin( dept ) );

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

    @RequestMapping(value = "/wiki/move", method = RequestMethod.GET)
    public String move( @RequestParam String from, ModelMap models )
    {
        Page page = pageDao.getPage( from );
        User user = SecurityUtils.getUser();
        String dept = getDept( from );
        if( !page.getOwner().getId().equals( user.getId() )
            && (dept == null ? !user.isSysadmin() : user.isAdmin( dept )) )
        {
            models.put( "message", "error.wiki.not.owner" );
            return "error";
        }

        return "wiki/move";
    }

    @RequestMapping(value = "/wiki/move", method = RequestMethod.POST)
    public String move( @RequestParam String from, @RequestParam String to,
        ModelMap models )
    {
        if( pageDao.getPage( to ) != null )
        {
            models.put( "message", "error.wiki.page.exists" );
            return "error";
        }

        Page page = pageDao.getPage( from );
        page.setPath( to );
        page = pageDao.savePage( page );

        User user = SecurityUtils.getUser();
        logger.info( "Wiki page " + from + " moved to " + to + " by "
            + user.getUsername() );

        subscriptionDao.subscribe( page, user );

        String subject = "Wiki Page " + from + " Moved";
        String vTemplate = "notification.wiki.page.moved.vm";
        Map<String, Object> vModels = new HashMap<String, Object>();
        vModels.put( "page", page );
        vModels.put( "author", user );
        vModels.put( "from", from );
        notificationService.notifiy( page, subject, vTemplate, vModels, false );

        return "redirect:" + to;
    }

    @RequestMapping("/wiki/revisions")
    public String revisions( @RequestParam Long id, ModelMap models )
    {
        Page page = pageDao.getPage( id );
        models.put( "page", page );
        models.put( "revisions", revisionDao.getRevisions( page ) );
        return "wiki/revisions";
    }

    @RequestMapping("/wiki/revert")
    public String revert( @RequestParam Long revisionId, HttpSession session,
        ModelMap models )
    {
        Revision revision = revisionDao.getRevision( revisionId );
        Page page = revision.getPage();
        User user = SecurityUtils.getUser();
        String dept = getDept( page.getPath() );
        if( page.isLocked() && !page.getOwner().getId().equals( user.getId() )
            && (dept == null ? !user.isSysadmin() : !user.isAdmin( dept )) )
        {
            models.put( "message", "error.wiki.not.owner" );
            return "error";
        }

        revision = revision.clone();
        revision.setAuthor( user );
        revision.setDate( new Date() );
        revision = revisionDao.saveRevision( revision );

        subscriptionDao.subscribe( page, user );

        String subject = "New Revision of Wiki Page " + page.getPath();
        String vTemplate = "notification.new.wiki.revision.vm";
        Map<String, Object> vModels = new HashMap<String, Object>();
        vModels.put( "page", page );
        vModels.put( "author", user );
        notificationService.notifiy( page, subject, vTemplate, vModels, true );

        return "redirect:" + page.getPath();
    }

    @RequestMapping("/wiki/compare")
    public String compare( @RequestParam("revisionId") Long[] ids,
        ModelMap models ) throws Exception
    {
        Long oldId = ids[0] < ids[1] ? ids[0] : ids[1];
        Long newId = ids[0] > ids[1] ? ids[0] : ids[1];
        Revision oldRevision = revisionDao.getRevision( oldId );
        Revision newRevision = revisionDao.getRevision( newId );

        models.put( "path", newRevision.getPage().getPath() );
        models.put( "diffResult", diff( oldRevision, newRevision ) );
        return "wiki/compare";
    }

    @RequestMapping("/wiki/discussions")
    public String discussions( @RequestParam Long id, ModelMap models )
    {
        models.put( "page", pageDao.getPage( id ) );
        return "wiki/discussions";
    }

    @RequestMapping("/wiki/search")
    public String search( @RequestParam String term, ModelMap models )
    {
        models.put( "results", pageDao.searchPages( term, 40 ) );
        return "wiki/search";
    }

    private String diff( Revision oldRevision, Revision newRevision )
        throws Exception
    {
        StringWriter result = new StringWriter();
        TransformerHandler transformerHandler = ((SAXTransformerFactory) TransformerFactory.newInstance()).newTransformerHandler();
        transformerHandler.setResult( new StreamResult( result ) );
        XslFilter xslFilter = new XslFilter();
        ContentHandler contentHandler = xslFilter.xsl( transformerHandler,
            "xsl/diff2html.xsl" );

        contentHandler.startDocument();
        HtmlSaxDiffOutput diffOutput = new HtmlSaxDiffOutput( contentHandler,
            "diff" );
        HTMLDiffer differ = new HTMLDiffer( diffOutput );
        TextNodeComparator leftComparator = createComparator( oldRevision );
        TextNodeComparator rightComparator = createComparator( newRevision );
        differ.diff( leftComparator, rightComparator );
        contentHandler.endDocument();

        return result.toString();
    }

    private TextNodeComparator createComparator( Revision revision )
        throws IOException, SAXException
    {
        Locale locale = Locale.getDefault();
        HtmlCleaner cleaner = new HtmlCleaner();
        DomTreeBuilder domTreeBuilder = new DomTreeBuilder();

        InputSource inputSource = new InputSource( new ByteArrayInputStream(
            revision.getContent().getBytes( "UTF-8" ) ) );
        cleaner.cleanAndParse( inputSource, domTreeBuilder );

        return new TextNodeComparator( domTreeBuilder, locale );
    }

}
