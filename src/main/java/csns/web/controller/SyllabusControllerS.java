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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Course;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.File;
import csns.model.core.Resource;
import csns.model.core.ResourceType;
import csns.model.core.User;
import csns.model.site.dao.SiteDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.validator.ResourceValidator;

@Controller
@SessionAttributes({ "syllabus", "view" })
public class SyllabusControllerS {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ResourceValidator resourceValidator;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( SyllabusControllerS.class );

    private Section getSection( String qtr, String cc, int sn )
    {
        Quarter quarter = new Quarter();
        quarter.setShortString( qtr );
        Course course = courseDao.getCourse( cc );
        return sectionDao.getSection( quarter, course, sn );
    }

    private String edit( Section section, ModelMap models )
    {
        Resource syllabus = section.getSyllabus();
        if( syllabus == null )
        {
            syllabus = new Resource( section.getCourse().getCode() + "-"
                + section.getNumber() + " " + section.getQuarter()
                + " Syllabus" );
            syllabus.setType( ResourceType.TEXT );
        }

        models.put( "section", section );
        models.put( "syllabus", syllabus );
        models.put( "resourceTypes", ResourceType.values() );
        return "syllabus/edit";
    }

    @RequestMapping(value = "/site/{qtr}/{cc}-{sn}/syllabus/edit",
        method = RequestMethod.GET)
    public String edit( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, ModelMap models )
    {
        models.put( "view", "site" );
        Section section = getSection( qtr, cc, sn );
        return edit( section, models );
    }

    @RequestMapping(value = "/section/journal/editSyllabus",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long sectionId, ModelMap models )
    {
        models.put( "view", "journal1" );
        Section section = sectionDao.getSection( sectionId );
        return edit( section, models );
    }

    private String edit( Section section, Resource syllabus,
        MultipartFile uploadedFile, BindingResult bindingResult,
        SessionStatus sessionStatus, ModelMap models, String redirectUrl )
    {
        resourceValidator.validate( syllabus, uploadedFile, bindingResult );
        if( bindingResult.hasErrors() )
        {
            models.put( "section", section );
            return "syllabus/edit";
        }

        if( syllabus.getType() == ResourceType.NONE )
            section.setSyllabus( null );
        else
            section.setSyllabus( syllabus );

        // This is a workaround for a rather weird behavior in Hibernate: when
        // FileDao.saveFile() is called, the syllabus Resource object is
        // automatically flushed with a persist(), which will cause a
        // "persisting a detached object" exception. So here we re-attach
        // syllabus first, and then save the uploaded file if there is one.
        section = sectionDao.saveSection( section );
        syllabus = section.getSyllabus();
        User user = SecurityUtils.getUser();
        if( syllabus.getType() == ResourceType.FILE && uploadedFile != null
            && !uploadedFile.isEmpty() )
        {
            File file = fileIO.save( uploadedFile, user, true );
            syllabus.setFile( file );
            sectionDao.saveSection( section );
        }
        sessionStatus.setComplete();

        logger.info( user.getUsername() + " edited the syllabus of section "
            + section.getId() );

        return "redirect:" + redirectUrl;
    }

    @RequestMapping(value = "/site/{qtr}/{cc}-{sn}/syllabus/edit",
        method = RequestMethod.POST)
    public String edit(
        @PathVariable String qtr,
        @PathVariable String cc,
        @PathVariable int sn,
        @ModelAttribute("syllabus") Resource syllabus,
        @RequestParam(value = "uploadedFile", required = false) MultipartFile uploadedFile,
        BindingResult bindingResult, SessionStatus sessionStatus,
        ModelMap models )
    {
        Section section = getSection( qtr, cc, sn );
        return edit( section, syllabus, uploadedFile, bindingResult,
            sessionStatus, models, section.getSiteUrl() );
    }

    @RequestMapping(value = "/section/journal/editSyllabus",
        method = RequestMethod.POST)
    public String edit(
        @RequestParam Long sectionId,
        @ModelAttribute("syllabus") Resource syllabus,
        @RequestParam(value = "uploadedFile", required = false) MultipartFile uploadedFile,
        BindingResult bindingResult, SessionStatus sessionStatus,
        ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        return edit( section, syllabus, uploadedFile, bindingResult,
            sessionStatus, models, "view?sectionId=" + sectionId );
    }

}
