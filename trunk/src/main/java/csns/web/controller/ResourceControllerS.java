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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Section;
import csns.model.academics.dao.SectionDao;
import csns.model.core.File;
import csns.model.core.Resource;
import csns.model.core.ResourceType;
import csns.model.core.User;
import csns.model.core.dao.ResourceDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.validator.ResourceValidator;

@Controller
@SessionAttributes({ "resource", "view" })
public class ResourceControllerS {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private ResourceValidator resourceValidator;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( ResourceControllerS.class );

    private Resource save( Resource resource, MultipartFile uploadedFile,
        SessionStatus sessionStatus )
    {
        User user = SecurityUtils.getUser();
        if( resource.getType() == ResourceType.FILE && uploadedFile != null
            && !uploadedFile.isEmpty() )
        {
            File file = fileIO.save( uploadedFile, user, true );
            resource.setFile( file );
        }
        resource = resourceDao.saveResource( resource );
        sessionStatus.setComplete();

        logger.info( user.getUsername() + " added/edited resource "
            + resource.getId() );

        return resource;
    }

    @RequestMapping(value = "/section/journal/addHandout",
        method = RequestMethod.GET)
    public String addHandout( @RequestParam Long sectionId, ModelMap models )
    {
        models.put( "view", "journal_handout" );
        models.put( "section", sectionDao.getSection( sectionId ) );
        models.put( "resource", new Resource( ResourceType.FILE ) );
        models.put( "resourceTypes", ResourceType.values() );
        return "resource/edit";
    }

    @RequestMapping(value = "/section/journal/addHandout",
        method = RequestMethod.POST)
    public String addHandout(
        @RequestParam Long sectionId,
        @ModelAttribute Resource resource,
        @RequestParam(value = "uploadedFile", required = false) MultipartFile uploadedFile,
        BindingResult bindingResult, SessionStatus sessionStatus,
        ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        resourceValidator.validate( resource, uploadedFile, bindingResult );
        if( bindingResult.hasErrors() )
        {
            models.put( "section", section );
            return "resource/edit";
        }

        resource = save( resource, uploadedFile, sessionStatus );
        section.getJournal().getHandouts().add( resource );
        section = sectionDao.saveSection( section );
        return "redirect:/section/journal/handouts?sectionId="
            + section.getId();
    }

    @RequestMapping(value = "/section/journal/editHandout",
        method = RequestMethod.GET)
    public String editHandout( @RequestParam Long sectionId,
        @RequestParam Long resourceId, ModelMap models )
    {
        models.put( "view", "journal_handout" );
        models.put( "section", sectionDao.getSection( sectionId ) );
        models.put( "resource", resourceDao.getResource( resourceId ) );
        models.put( "resourceTypes", ResourceType.values() );
        return "resource/edit";
    }

    @RequestMapping(value = "/section/journal/editHandout",
        method = RequestMethod.POST)
    public String editHandout(
        @RequestParam Long sectionId,
        @ModelAttribute Resource resource,
        @RequestParam(value = "uploadedFile", required = false) MultipartFile uploadedFile,
        BindingResult bindingResult, SessionStatus sessionStatus,
        ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        resourceValidator.validate( resource, uploadedFile, bindingResult );
        if( bindingResult.hasErrors() )
        {
            models.put( "section", section );
            return "resource/edit";
        }

        resource = save( resource, uploadedFile, sessionStatus );
        return "redirect:/section/journal/handouts?sectionId="
            + section.getId();
    }

}
