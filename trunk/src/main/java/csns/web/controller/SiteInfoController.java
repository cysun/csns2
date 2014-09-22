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

    @RequestMapping("/site/{qtr}/{cc}-{sn}/info/edit")
    public String edit( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, ModelMap models )
    {
        Section section = getSection( qtr, cc, sn );
        models.put( "section", section );
        return "site/info/edit";
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

        return "redirect:edit";
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

        return "redirect:edit";
    }

}
