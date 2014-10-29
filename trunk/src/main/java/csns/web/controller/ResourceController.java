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

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Section;
import csns.model.academics.dao.SectionDao;
import csns.model.core.Resource;
import csns.model.core.dao.ResourceDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class ResourceController {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( ResourceController.class );

    @RequestMapping("/resource/view")
    public String view( @RequestParam Long id, ModelMap models,
        HttpServletResponse response )
    {
        Resource resource = resourceDao.getResource( id );

        switch( resource.getType() )
        {
            case TEXT:
                models.put( "resource", resource );
                return "resource/view";

            case FILE:
                fileIO.write( resource.getFile(), response );
                return null;

            case URL:
                return "redirect:" + resource.getUrl();

            default:
                logger.warn( "Invalid resource type: " + resource.getType() );
                models.put( "message", "error.resource.type.invalid" );
                return "error";
        }
    }

    @RequestMapping("/section/journal/removeHandout")
    public String remove( @RequestParam Long sectionId,
        @RequestParam Long resourceId )
    {
        String username = SecurityUtils.getUser().getUsername();
        Section section = sectionDao.getSection( sectionId );
        boolean removed = section.getCourseJournal().removeHandout( resourceId );
        if( removed )
        {
            section = sectionDao.saveSection( section );
            logger.info( username + " removed handout " + resourceId
                + " from section " + sectionId );
        }
        else
            logger.info( username + " failed to remove handout " + resourceId
                + " from section " + sectionId );

        return "redirect:/section/journal/handouts?sectionId="
            + section.getId();
    }

}
