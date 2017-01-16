/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014-2017, Chengyu Sun (csun@calstatela.edu).
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
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import csns.model.academics.Course;
import csns.model.academics.Term;
import csns.model.academics.Section;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.File;
import csns.model.core.Resource;
import csns.model.core.User;
import csns.model.core.dao.FileDao;
import csns.model.core.dao.ResourceDao;
import csns.model.site.Site;
import csns.model.site.dao.ItemDao;
import csns.model.site.dao.SiteDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class SiteController {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FileIO fileIO;

    private static Logger logger = LoggerFactory
        .getLogger( SiteController.class );

    private Section getSection( String qtr, String cc, int sn )
    {
        Term term = new Term();
        term.setShortString( qtr );
        Course course = courseDao.getCourse( cc );
        return sectionDao.getSection( term, course, sn );
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}")
    public String view( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, ModelMap models )
    {
        Section section = getSection( qtr, cc, sn );
        User user = SecurityUtils.getUser();
        boolean isInstructor = section.isInstructor( user );
        boolean isStudent = section.isEnrolled( user );
        boolean isAdmin = user != null && user.isAdmin();
        models.put( "section", section );
        models.put( "isInstructor", isInstructor );
        models.put( "isStudent", isStudent );

        if( section == null || section.getSite() == null )
        {
            if( isInstructor ) models.put( "sites",
                siteDao.getSites( section.getCourse(), user, 10 ) );
            return "site/nosite";
        }

        Site site = section.getSite();

        if( site.isRestricted() && !isStudent && !isInstructor && !isAdmin )
        {
            models.put( "message", "error.site.restricted" );
            return "error";
        }

        if( site.isLimited() && section.getTerm().before( new Date() )
            && !isInstructor && !isAdmin )
        {
            models.put( "message", "error.site.limited" );
            return "error";
        }

        return "site/view";
    }

    private String create( Long sectionId, Site oldSite )
    {
        Section section = sectionDao.getSection( sectionId );
        if( section.getSite() != null )
            return "redirect:" + section.getSiteUrl();

        Site site = new Site( section );
        if( oldSite != null )
        {
            site = oldSite.clone();
            site.setSection( section );

            if( oldSite.getSection().getSyllabus() != null
                && section.getSyllabus() == null )
            {
                section
                    .setSyllabus( oldSite.getSection().getSyllabus().clone() );
                section = sectionDao.saveSection( section );
            }
        }
        site = siteDao.saveSite( site );

        logger.info( SecurityUtils.getUser().getUsername()
            + " created site for section " + section.getId() );

        return "redirect:" + section.getSiteUrl() + "/block/list";
    }

    @RequestMapping(value = "/site/create", params = "siteId")
    public String create( @RequestParam Long sectionId, Long siteId )
    {
        return create( sectionId, siteDao.getSite( siteId ) );
    }

    @RequestMapping(value = "/site/create", params = "siteUrl")
    public String create( @RequestParam Long sectionId, String siteUrl,
        ModelMap models )
    {
        String tokens[] = siteUrl.split( "/|-" );
        int index = -1;
        for( int i = 0; i < tokens.length; ++i )
            if( tokens[i].equalsIgnoreCase( "site" ) )
            {
                index = i;
                break;
            }
        if( index < 0 || tokens.length <= index + 3 )
        {
            models.put( "message", "error.site.invalid.url" );
            return "error";
        }

        String qtr = tokens[index + 1];
        String cc = tokens[index + 2];
        int sn = Integer.parseInt( tokens[index + 3] );
        return create( sectionId, getSection( qtr, cc, sn ).getSite() );
    }

    @RequestMapping(value = "/site/create")
    public String create( @RequestParam Long sectionId )
    {
        return create( sectionId, (Site) null );
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/settings")
    public String settings( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, ModelMap models )
    {
        Section section = getSection( qtr, cc, sn );
        models.put( "section", section );
        models.put( "site", section.getSite() );
        return "site/settings";
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/settings/toggle")
    @ResponseStatus(HttpStatus.OK)
    public void toggleSetting( @PathVariable String qtr,
        @PathVariable String cc, @PathVariable int sn,
        @RequestParam String setting, ModelMap models )
    {
        Site site = getSection( qtr, cc, sn ).getSite();
        Boolean result = site.toggleSetting( setting );
        site = siteDao.saveSite( site );

        logger.info( SecurityUtils.getUser().getUsername() + " set " + setting
            + " to " + result + " for site " + site.getId() );
    }

    private String resource( String qtr, String cc, int sn, Resource resource,
        ModelMap models, HttpServletResponse response )
    {
        if( resource.isDeleted() )
        {
            models.put( "message", "error.site.item.deleted" );
            models.put( "backUrl", "/site/" + qtr + "/" + cc + "-" + sn );
            return "error";
        }

        switch( resource.getType() )
        {
            case TEXT:
                models.put( "resource", resource );
                return "site/resource";

            case FILE:
                fileIO.write( resource.getFile(), response );
                return null;

            case URL:
                return "redirect:" + resource.getUrl();

            default:
                logger.warn( "Invalid resource type: " + resource.getType() );
                return "redirect:" + getSection( qtr, cc, sn ).getSiteUrl();
        }
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/resource/{id}")
    public String resource( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, @PathVariable Long id, ModelMap models,
        HttpServletResponse response )
    {
        return resource( qtr, cc, sn, resourceDao.getResource( id ), models,
            response );
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/item/{id}")
    public String item( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, @PathVariable Long id, ModelMap models,
        HttpServletResponse response )
    {
        return resource( qtr, cc, sn, itemDao.getItem( id ).getResource(),
            models, response );
    }

    private File getFolder( File parent, String name )
    {
        User user = SecurityUtils.getUser();
        List<File> results = fileDao.getFiles( user, parent, name, true );
        if( results.size() > 0 ) return results.get( 0 );

        File folder = new File();
        folder.setName( name );
        folder.setFolder( true );
        folder.setRegular( true );
        folder.setParent( parent );
        folder.setOwner( user );
        folder = fileDao.saveFile( folder );

        String parentName = parent != null ? parent.getName() : "root";
        logger.info( user.getUsername() + " created folder " + name + " under "
            + parentName );

        return folder;
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/files/")
    public String folder( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn )
    {
        Site site = getSection( qtr, cc, sn ).getSite();
        if( site.getFolder() != null )
            return "redirect:/file/view?id=" + site.getFolder().getId();

        File folder = getFolder( null, "Courses" );
        folder = getFolder( folder, cc.toUpperCase() );
        folder = getFolder( folder, qtr.toUpperCase() );
        site.setFolder( folder );
        site = siteDao.saveSite( site );

        return "redirect:/file/view?id=" + folder.getId();
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/files/remove")
    public String removeFolder( @PathVariable String qtr,
        @PathVariable String cc, @PathVariable int sn )
    {
        Site site = getSection( qtr, cc, sn ).getSite();
        site.setFolder( null );
        site = siteDao.saveSite( site );

        return "redirect:/site/" + qtr + "/" + cc + "-" + sn;
    }

}
