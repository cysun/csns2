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
import csns.model.site.Site;
import csns.model.site.dao.SiteDao;
import csns.security.SecurityUtils;

@Controller
public class SiteController {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private SectionDao sectionDao;

    private static Logger logger = LoggerFactory.getLogger( SiteController.class );

    @RequestMapping("/site/{qtr}/{cc}-{sn}")
    public String view( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, ModelMap models )
    {
        Quarter quarter = new Quarter();
        quarter.setShortString( qtr );
        Course course = courseDao.getCourse( cc );
        Section section = sectionDao.getSection( quarter, course, sn );

        models.put( "section", section );
        if( section != null && SecurityUtils.isAuthenticated() )
            models.put( "isInstructor",
                section.isInstructor( SecurityUtils.getUser() ) );

        if( section == null || section.getSite() == null )
            return "site/nosite";
        else
        {
            models.put( "site", section.getSite() );
            return "site/view";
        }
    }

    private String createSite( Section section )
    {
        if( section.getSite() != null )
            return "redirect:" + section.getSite().getUrl();

        Site site = new Site( section );
        site = siteDao.saveSite( site );
        logger.info( SecurityUtils.getUser().getUsername()
            + " create site for section " + section.getId() );
        return "redirect:" + site.getUrl();
    }

    @RequestMapping(value = "/site/create")
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

}
