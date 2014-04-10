/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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

import csns.model.academics.Project;
import csns.model.academics.dao.ProjectDao;
import csns.model.core.Resource;
import csns.model.core.ResourceType;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.validator.ResourceValidator;

@Controller
@SessionAttributes("resource")
public class ProjectResourceControllerS {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ResourceValidator resourceValidator;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( ProjectResourceControllerS.class );

    @RequestMapping(value = "/department/{dept}/project/resource/add",
        method = RequestMethod.GET)
    public String add( @RequestParam Long projectId, ModelMap models )
    {
        models.put( "project", projectDao.getProject( projectId ) );
        models.put( "resource", new Resource() );
        return "project/resource/add";
    }

    @RequestMapping(value = "/department/{dept}/project/resource/add",
        method = RequestMethod.POST)
    public String add( @ModelAttribute Resource resource,
        @PathVariable String dept, @RequestParam Long projectId,
        @RequestParam(required = false) MultipartFile uploadedFile,
        BindingResult result, SessionStatus sessionStatus )
    {
        resourceValidator.validate( resource, uploadedFile, result );
        if( result.hasErrors() ) return "project/resource/add";

        if( resource.getType() == ResourceType.FILE )
            resource.setFile( fileIO.save( uploadedFile,
                SecurityUtils.getUser(), false ) );

        Project project = projectDao.getProject( projectId );
        if( resource.getType() != ResourceType.NONE )
        {
            project.getResources().add( resource );
            project = projectDao.saveProject( project );
        }

        logger.info( SecurityUtils.getUser().getUsername()
            + " added a resource to project " + project.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/project/view?id="
            + project.getId();
    }

    @RequestMapping(value = "/department/{dept}/project/resource/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long projectId,
        @RequestParam Long resourceId, ModelMap models )
    {
        Project project = projectDao.getProject( projectId );
        models.put( "project", project );
        models.put( "resource", project.getResource( resourceId ) );
        models.put( "user", SecurityUtils.getUser() );
        return "project/resource/edit";
    }

    @RequestMapping(value = "/department/{dept}/project/resource/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Resource resource,
        @PathVariable String dept, @RequestParam Long projectId,
        @RequestParam(required = false) MultipartFile uploadedFile,
        BindingResult result, SessionStatus sessionStatus )
    {
        resourceValidator.validate( resource, uploadedFile, result );
        if( result.hasErrors() ) return "project/resource/edit";

        if( resource.getType() == ResourceType.FILE && uploadedFile != null
            && !uploadedFile.isEmpty() )
            resource.setFile( fileIO.save( uploadedFile,
                SecurityUtils.getUser(), false ) );

        Project project = projectDao.getProject( projectId );
        project.replaceResource( resource );
        projectDao.saveProject( project );

        logger.info( SecurityUtils.getUser().getUsername()
            + " edited resource " + resource.getId() + " of project "
            + project.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/project/view?id=" + projectId;
    }

}
