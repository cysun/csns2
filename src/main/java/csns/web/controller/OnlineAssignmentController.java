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
import csns.model.academics.OnlineAssignment;
import csns.model.academics.Section;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SectionDao;
import csns.model.qa.Question;
import csns.model.qa.QuestionSection;
import csns.security.SecurityUtils;

@Controller
public class OnlineAssignmentController {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private AssignmentDao assignmentDao;

    private static final Logger logger = LoggerFactory.getLogger( OnlineAssignmentController.class );

    @RequestMapping("/assignment/online/view")
    public String view( @RequestParam Long id,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "assignment", assignmentDao.getAssignment( id ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "assignment/online/view";
    }

    @RequestMapping("/assignment/online/clone")
    public String clone( @RequestParam Long sectionId,
        @RequestParam Long assignmentId )
    {
        Section section = sectionDao.getSection( sectionId );
        Assignment oldAssignment = assignmentDao.getAssignment( assignmentId );
        Assignment newAssignment = oldAssignment.clone();
        newAssignment.setSection( section );
        // some old assignments do not have the correct total points
        ((OnlineAssignment) newAssignment).calcTotalPoints();
        newAssignment = assignmentDao.saveAssignment( newAssignment );

        logger.info( SecurityUtils.getUser().getUsername()
            + " cloned online assignment " + newAssignment.getId() + " from "
            + oldAssignment.getId() );

        return "redirect:/assignment/edit?id=" + newAssignment.getId();
    }

    @RequestMapping("/assignment/online/editQuestionSheet")
    public String editQuestionSheet( @RequestParam Long assignmentId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "assignment", assignmentDao.getAssignment( assignmentId ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "assignment/online/editQuestionSheet";
    }

    @RequestMapping("/assignment/online/deleteSection")
    public String deleteSection( @RequestParam Long assignmentId,
        @RequestParam int sectionIndex )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            assignment.getQuestionSheet().getSections().remove( sectionIndex );
            assignment.calcTotalPoints();
            assignmentDao.saveAssignment( assignment );

            logger.info( SecurityUtils.getUser().getUsername()
                + " deleted section " + sectionIndex
                + " from online assignment " + assignmentId );
        }

        return "redirect:/assignment/online/editQuestionSheet?assignmentId="
            + assignmentId;
    }

    @RequestMapping("/assignment/online/deleteQuestion")
    public String deleteQuestion( @RequestParam Long assignmentId,
        @RequestParam int sectionIndex, @RequestParam Long questionId )
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            assignment.getQuestionSheet()
                .getSections()
                .get( sectionIndex )
                .removeQuestion( questionId );
            assignment.calcTotalPoints();
            assignmentDao.saveAssignment( assignment );

            logger.info( SecurityUtils.getUser().getUsername()
                + " deleted question " + questionId
                + " from online assignment " + assignmentId );
        }

        return "redirect:/assignment/online/editQuestionSheet?assignmentId="
            + assignmentId + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping("/assignment/online/reorderQuestion")
    public String reorderQuestion( @RequestParam Long assignmentId,
        @RequestParam int sectionIndex, @RequestParam Long questionId,
        @RequestParam int newIndex, HttpServletResponse response )
        throws IOException
    {
        OnlineAssignment assignment = (OnlineAssignment) assignmentDao.getAssignment( assignmentId );
        if( !assignment.isPublished() )
        {
            QuestionSection questionSection = assignment.getQuestionSheet()
                .getSections()
                .get( sectionIndex );
            Question question = questionSection.removeQuestion( questionId );
            if( question != null )
            {
                questionSection.getQuestions().add( newIndex, question );
                assignmentDao.saveAssignment( assignment );
            }
        }

        logger.info( SecurityUtils.getUser().getUsername() + " moved question "
            + questionId + " to index " + newIndex + " in online assignment "
            + assignmentId );

        response.setContentType( "text/plain" );
        response.getWriter().print( "" );

        return null;
    }

    @RequestMapping("/assignment/online/search")
    public String search( @RequestParam Long sectionId,
        @RequestParam(required = false) String term, ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        models.put( "section", section );

        if( StringUtils.hasText( term ) )
            models.put(
                "results",
                assignmentDao.searchAssignments( term, "ONLINE",
                    SecurityUtils.getUser(), 20 ) );

        return "assignment/online/search";
    }

}
