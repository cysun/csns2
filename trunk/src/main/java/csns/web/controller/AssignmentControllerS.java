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

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Assignment;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.Resource;
import csns.model.core.ResourceType;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.editor.CalendarPropertyEditor;
import csns.web.validator.AssignmentValidator;

@Controller
@SessionAttributes("assignment")
public class AssignmentControllerS {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private AssignmentValidator assignmentValidator;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( AssignmentControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Calendar.class,
            new CalendarPropertyEditor() );
    }

    @RequestMapping(value = "/assignment/create", method = RequestMethod.GET)
    public String create( @RequestParam Long sectionId, ModelMap models )
    {
        Assignment assignment = new Assignment();
        assignment.setSection( sectionDao.getSection( sectionId ) );
        assignment.setDescription( new Resource( "Assignment Description" ) );
        models.put( "assignment", assignment );
        models.put( "resourceTypes", ResourceType.values() );
        return "assignment/create";
    }

    @RequestMapping(value = "/assignment/create", method = RequestMethod.POST)
    public String create(
        @ModelAttribute Assignment assignment,
        @RequestParam(value = "file", required = false) MultipartFile uploadedFile,
        BindingResult result, SessionStatus sessionStatus )
    {
        assignmentValidator.validate( assignment, uploadedFile, result );
        if( result.hasErrors() ) return "assignment/create";

        Resource description = assignment.getDescription();
        if( description.getType() == ResourceType.NONE )
            assignment.setDescription( null );
        else if( description.getType() == ResourceType.FILE )
            description.setFile( fileIO.save( uploadedFile,
                SecurityUtils.getUser(), false ) );

        assignment = assignmentDao.saveAssignment( assignment );
        sessionStatus.setComplete();

        logger.info( SecurityUtils.getUser().getUsername()
            + " created assignment " + assignment.getId() );

        return "redirect:/section/taught#section-"
            + assignment.getSection().getId();
    }

    @RequestMapping(value = "/assignment/edit", method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        Assignment assignment = assignmentDao.getAssignment( id );
        if( assignment.getDescription() == null )
            assignment.setDescription( new Resource( "Assignment Description" ) );
        models.put( "assignment", assignment );
        return assignment.isOnline() ? "assignment/online/edit"
            : "assignment/edit";
    }

    @RequestMapping(value = "/assignment/edit", method = RequestMethod.POST)
    public String edit(
        @ModelAttribute Assignment assignment,
        @RequestParam(value = "file", required = false) MultipartFile uploadedFile,
        HttpServletRequest request, BindingResult result,
        SessionStatus sessionStatus )
    {
        assignmentValidator.validate( assignment, uploadedFile, result );
        if( result.hasErrors() )
            return assignment.isOnline() ? "assignment/online/edit"
                : "assignment/edit";

        if( !assignment.isOnline() )
        {
            Resource description = assignment.getDescription();
            if( description.getType() == ResourceType.NONE )
                assignment.setDescription( null );
            else if( description.getType() == ResourceType.FILE
                && uploadedFile != null && !uploadedFile.isEmpty() )
                description.setFile( fileIO.save( uploadedFile,
                    SecurityUtils.getUser(), false ) );
        }

        assignment = assignmentDao.saveAssignment( assignment );
        sessionStatus.setComplete();

        logger.info( SecurityUtils.getUser().getUsername()
            + " edited assignment " + assignment.getId() );

        return assignment.isOnline() && request.getParameter( "next" ) != null
            ? "redirect:/assignment/online/editQuestionSheet?assignmentId="
                + assignment.getId() : "redirect:/section/taught#section-"
                + assignment.getSection().getId();
    }

}
