/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012, Chengyu Sun (csun@calstatela.edu).
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Assignment;
import csns.model.academics.Section;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SectionDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class AssignmentController {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( AssignmentController.class );

    @RequestMapping("/assignment/view")
    public String view( @RequestParam Long id, ModelMap models,
        HttpServletResponse response )
    {
        Assignment assignment = assignmentDao.getAssignment( id );

        switch( assignment.getDescription().getType() )
        {
            case TEXT:
                models.put( "assignment", assignment );
                return "assignment/view";

            case FILE:
                fileIO.write( assignment.getDescription().getFile(), response );
                return null;

            case URL:
                return "redirect:" + assignment.getDescription().getUrl();

            default:
                logger.warn( "Invalid resource type: "
                    + assignment.getDescription().getType() );
                models.put( "message", "error.resource.type.invalid" );
                return "error";
        }
    }

    @RequestMapping("/assignment/clone")
    public String clone( @RequestParam Long sectionId,
        @RequestParam Long assignmentId )
    {
        Section section = sectionDao.getSection( sectionId );
        Assignment oldAssignment = assignmentDao.getAssignment( assignmentId );
        Assignment newAssignment = oldAssignment.clone();
        newAssignment.setSection( section );
        newAssignment = assignmentDao.saveAssignment( newAssignment );

        logger.info( SecurityUtils.getUser().getUsername()
            + " cloned assignment " + newAssignment.getId() + " from "
            + oldAssignment.getId() );

        return "redirect:/assignment/edit?id=" + newAssignment.getId();
    }

    @RequestMapping("/assignment/delete")
    public String delete( @RequestParam Long id )
    {
        Assignment assignment = assignmentDao.getAssignment( id );
        assignment.setDeleted( true );
        assignment = assignmentDao.saveAssignment( assignment );

        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted assignment " + assignment.getId() );

        return "redirect:/section/taught#section-"
            + assignment.getSection().getId();
    }

    @RequestMapping("/assignment/publish")
    public String publish( @RequestParam Long id, HttpServletResponse response )
        throws IOException
    {
        Assignment assignment = assignmentDao.getAssignment( id );
        if( !assignment.isPublished() )
        {
            assignment.setPublishDate( Calendar.getInstance() );
            assignment = assignmentDao.saveAssignment( assignment );

            logger.info( SecurityUtils.getUser().getUsername()
                + " published assignment " + assignment.getId() );
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm a" );
        response.setContentType( "text/plain" );
        response.getWriter().print(
            dateFormat.format( assignment.getPublishDate().getTime() ) );

        return null;
    }

    @RequestMapping("/assignment/search")
    public String search( @RequestParam Long sectionId,
        @RequestParam(required = false) String term, ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        models.put( "section", section );

        if( StringUtils.hasText( term ) )
            models.put(
                "results",
                assignmentDao.searchAssignments( term, "REGULAR",
                    SecurityUtils.getUser(), 20 ) );

        return "assignment/search";
    }

}
