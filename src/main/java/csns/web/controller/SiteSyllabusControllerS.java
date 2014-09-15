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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Section;
import csns.model.academics.dao.SectionDao;
import csns.model.core.Resource;
import csns.model.core.ResourceType;
import csns.model.core.User;
import csns.model.site.Site;
import csns.model.site.dao.SiteDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
@SessionAttributes("syllabus")
public class SiteSyllabusControllerS {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( SiteSyllabusControllerS.class );

    private String get( Section section, ModelMap models )
    {
        Resource syllabus = section.getSyllabus();
        if( syllabus == null ) syllabus = new Resource();

        models.put( "syllabus", syllabus );
        return "section/syllabus/edit";
    }

    private String post( Section section, Resource syllabus,
        MultipartFile uploadedFile, String url, SessionStatus sessionStatus )
    {
        User user = SecurityUtils.getUser();
        if( syllabus.getType() == ResourceType.NONE )
            section.setSyllabus( null );
        else if( syllabus.getType() == ResourceType.FILE )
        {
            syllabus.setFile( fileIO.save( uploadedFile, user, true ) );
            section.setSyllabus( syllabus );
        }
        sectionDao.saveSection( section );
        sessionStatus.setComplete();

        logger.info( user.getUsername() + " edited the syllabus of section "
            + section.getId() );

        return "redirect:" + url;
    }

    @RequestMapping(value = "/site/edit/syllabus", method = RequestMethod.GET)
    public String edit( @RequestParam Long siteId, ModelMap models )
    {
        Site site = siteDao.getSite( siteId );
        models.put( "site", site );
        return get( site.getSection(), models );
    }

    @RequestMapping(value = "/site/edit/syllabus", method = RequestMethod.POST)
    public String edit( @ModelAttribute("syllabus") Resource syllabus,
        @RequestParam Long siteId, @RequestParam(value = "file",
            required = false) MultipartFile uploadedFile,
        SessionStatus sessionStatus )
    {
        Site site = siteDao.getSite( siteId );
        return post( site.getSection(), syllabus, uploadedFile, site.getUrl(),
            sessionStatus );
    }

}
