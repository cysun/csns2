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

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.File;
import csns.model.core.Resource;
import csns.model.core.User;
import csns.model.core.dao.FileDao;
import csns.model.site.Item;
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
    private CourseDao courseDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FileIO fileIO;

    private static Logger logger = LoggerFactory.getLogger( SiteController.class );

    private Section getSection( String qtr, String cc, int sn )
    {
        Quarter quarter = new Quarter();
        quarter.setShortString( qtr );
        Course course = courseDao.getCourse( cc );
        return sectionDao.getSection( quarter, course, sn );
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}")
    public String view( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, ModelMap models )
    {
        Section section = getSection( qtr, cc, sn );
        models.put( "section", section );
        if( section != null && SecurityUtils.isAuthenticated() )
        {
            User user = SecurityUtils.getUser();
            models.put( "isInstructor", section.isInstructor( user ) );
            models.put( "isStudent", section.isEnrolled( user ) );
        }
        return section == null || section.getSite() == null ? "site/nosite"
            : "site/view";
    }

    private String createSite( Section section )
    {
        if( section.getSite() != null )
            return "redirect:" + section.getSiteUrl();

        Site site = new Site( section );
        site = siteDao.saveSite( site );
        logger.info( SecurityUtils.getUser().getUsername()
            + " create site for section " + section.getId() );
        return "redirect:" + section.getSiteUrl();
    }

    @RequestMapping("/site/create")
    public String create( @RequestParam Long sectionId,
        @RequestParam(required = false, value = "new") Boolean newSite,
        ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        if( newSite != null && newSite ) return createSite( section );

        List<Site> sites = siteDao.getSites( section.getCourse(),
            SecurityUtils.getUser(), 20 );
        if( sites.size() == 0 ) return createSite( section );

        models.put( "section", section );
        models.put( "sites", sites );
        return "site/create";
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/item/{itemId}")
    public String item( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, @PathVariable Long itemId, ModelMap models,
        HttpServletResponse response )
    {
        Item item = itemDao.getItem( itemId );
        Resource resource = item.getResource();
        switch( resource.getType() )
        {
            case TEXT:
                models.put( "item", item );
                return "site/item";

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
