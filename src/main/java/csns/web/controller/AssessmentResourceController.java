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

import csns.model.assessment.Program;
import csns.model.assessment.ProgramSection;
import csns.model.assessment.dao.ProgramDao;
import csns.model.core.Resource;
import csns.model.core.dao.ResourceDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class AssessmentResourceController {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory
        .getLogger( AssessmentResourceController.class );

    @RequestMapping("/department/{dept}/assessment/program/resource/view")
    public String view( @PathVariable String dept, @RequestParam Long programId,
        @RequestParam Long resourceId, ModelMap models,
        HttpServletResponse response )
    {
        Program program = programDao.getProgram( programId );
        Resource resource = resourceDao.getResource( resourceId );

        switch( resource.getType() )
        {
            case TEXT:
                models.put( "program", program );
                models.put( "resource", resource );
                return "assessment/program/resource/view";

            case FILE:
                fileIO.write( resource.getFile(), response );
                return null;

            case URL:
                return "redirect:" + resource.getUrl();

            default:
                logger.warn( "Invalid resource type: " + resource.getType() );
                return "redirect:../view?id=" + programId;
        }
    }

    @RequestMapping("/department/{dept}/assessment/program/resource/reorder")
    @ResponseStatus(HttpStatus.OK)
    public void reorder( @RequestParam Long programId,
        @RequestParam Long sectionId, @RequestParam Long resourceId,
        @RequestParam int newIndex )
    {
        Program program = programDao.getProgram( programId );
        ProgramSection section = program.getSection( sectionId );
        Resource resource = section.removeResource( resourceId );
        if( resource != null )
        {
            section.getResources().add( newIndex, resource );
            programDao.saveProgram( program );

            logger.info(
                SecurityUtils.getUser().getUsername() + " reordered resource "
                    + resourceId + " of assessment program " + programId
                    + " to index " + newIndex );
        }
    }

    @RequestMapping("/department/{dept}/assessment/program/resource/remove")
    public String remove( @RequestParam Long programId,
        @RequestParam Long sectionId, @RequestParam Long resourceId )
    {
        Program program = programDao.getProgram( programId );
        program.getSection( sectionId ).removeResource( resourceId );
        programDao.saveProgram( program );

        logger
            .info( SecurityUtils.getUser().getUsername() + " removed resource "
                + resourceId + " from assessment program " + programId );

        return "redirect:../edit?id=" + programId + "#s" + sectionId;
    }

}
