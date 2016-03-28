/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Chengyu Sun (csun@calstatela.edu).
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

import csns.model.assessment.Program;
import csns.model.assessment.ProgramSection;
import csns.model.assessment.dao.ProgramDao;
import csns.model.core.Resource;
import csns.model.core.ResourceType;
import csns.model.core.dao.ResourceDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.validator.ResourceValidator;

@Controller
@SessionAttributes({ "program", "resource" })
public class AssessmentResourceControllerS {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private ResourceValidator resourceValidator;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory
        .getLogger( AssessmentResourceControllerS.class );

    @RequestMapping(
        value = "/department/{dept}/assessment/program/resource/add",
        method = RequestMethod.GET)
    public String add( @RequestParam Long programId, ModelMap models )
    {
        models.put( "program", programDao.getProgram( programId ) );
        models.put( "resource", new Resource() );
        return "assessment/program/resource/add";
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/resource/add",
        method = RequestMethod.POST)
    public String add( @ModelAttribute Resource resource,
        @RequestParam Long programId, @RequestParam Long sectionId,
        @RequestParam(required = false ) MultipartFile uploadedFile,
        BindingResult result, SessionStatus sessionStatus)
    {
        resourceValidator.validate( resource, uploadedFile, result );
        if( result.hasErrors() ) return "assessment/program/resource/add";

        if( resource.getType() == ResourceType.FILE ) resource.setFile(
            fileIO.save( uploadedFile, SecurityUtils.getUser(), false ) );

        Program program = programDao.getProgram( programId );
        ProgramSection section = program.getSection( sectionId );
        if( resource.getType() != ResourceType.NONE )
        {
            section.getResources().add( resource );
            program = programDao.saveProgram( program );

            logger.info( SecurityUtils.getUser().getUsername()
                + " added a resource to assessment program "
                + program.getId() );
        }

        sessionStatus.setComplete();
        return "redirect:../edit?id=" + programId + "#s" + sectionId;
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/resource/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long programId,
        @RequestParam Long resourceId, ModelMap models )
    {
        models.put( "program", programDao.getProgram( programId ) );
        models.put( "resource", resourceDao.getResource( resourceId ) );
        return "assessment/program/resource/edit";
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/resource/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Resource resource,
        @RequestParam Long programId, @RequestParam Long sectionId,
        @RequestParam(required = false ) MultipartFile uploadedFile,
        BindingResult result, SessionStatus sessionStatus)
    {
        resourceValidator.validate( resource, uploadedFile, result );
        if( result.hasErrors() ) return "assessment/program/resource/edit";

        if( resource.getType() == ResourceType.FILE && uploadedFile != null
            && !uploadedFile.isEmpty() )
            resource.setFile(
                fileIO.save( uploadedFile, SecurityUtils.getUser(), false ) );

        resource = resourceDao.saveResource( resource );

        logger.info( SecurityUtils.getUser().getUsername() + " edited resource "
            + resource.getId() + " of assessment program " + programId );

        sessionStatus.setComplete();
        return "redirect:../edit?id=" + programId + "#s" + sectionId;
    }

}
