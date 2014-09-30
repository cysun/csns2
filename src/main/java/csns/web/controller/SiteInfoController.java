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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import csns.model.academics.Course;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.SectionDao;
import csns.model.site.InfoEntry;
import csns.model.site.Site;
import csns.model.site.dao.SiteDao;
import csns.security.SecurityUtils;

@Controller
public class SiteInfoController {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private SiteDao siteDao;

    private static final Logger logger = LoggerFactory.getLogger( SiteInfoController.class );

    private Section getSection( String qtr, String cc, int sn )
    {
        Quarter quarter = new Quarter();
        quarter.setShortString( qtr );
        Course course = courseDao.getCourse( cc );
        return sectionDao.getSection( quarter, course, sn );
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/info/list")
    public String list( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, ModelMap models )
    {
        Section section = getSection( qtr, cc, sn );
        models.put( "section", section );
        return "site/info/list";
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/info/add")
    public String add( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, @RequestParam String name,
        @RequestParam String value )
    {
        Site site = getSection( qtr, cc, sn ).getSite();
        site.getInfoEntries().add( new InfoEntry( name, value ) );
        siteDao.saveSite( site );

        logger.info( SecurityUtils.getUser().getUsername()
            + " added an info entry to site " + site.getId() );

        return "redirect:list";
    }

    @RequestMapping(value = "/site/{qtr}/{cc}-{sn}/info/edit",
        method = RequestMethod.GET)
    public String edit( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, @RequestParam int index, ModelMap models )
    {
        Section section = getSection( qtr, cc, sn );
        models.put( "section", section );
        models.put( "infoEntry", section.getSite().getInfoEntries().get( index ) );
        return "site/info/edit";
    }

    @RequestMapping(value = "/site/{qtr}/{cc}-{sn}/info/edit",
        method = RequestMethod.POST)
    public String edit( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, @ModelAttribute InfoEntry infoEntry,
        @RequestParam int index )
    {
        Site site = getSection( qtr, cc, sn ).getSite();
        site.getInfoEntries().set( index, infoEntry );
        site = siteDao.saveSite( site );

        logger.info( SecurityUtils.getUser().getUsername()
            + " edited info entry " + index + " of site " + site.getId() );

        return "redirect:list";
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/info/delete")
    public String delete( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, @RequestParam int index )
    {
        Site site = getSection( qtr, cc, sn ).getSite();
        site.getInfoEntries().remove( index );
        siteDao.saveSite( site );

        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted an info entry from site " + site.getId() );

        return "redirect:list";
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/info/reorder")
    @ResponseStatus(HttpStatus.OK)
    public void reorder( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, @RequestParam int oldIndex,
        @RequestParam int newIndex )
    {
        Site site = getSection( qtr, cc, sn ).getSite();
        InfoEntry entry = site.getInfoEntries().remove( oldIndex );
        if( entry != null )
        {
            site.getInfoEntries().add( newIndex, entry );
            siteDao.saveSite( site );
        }

        logger.info( SecurityUtils.getUser().getUsername()
            + " moved an info entry from position " + oldIndex + " to "
            + newIndex );
    }

}
