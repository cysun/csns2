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

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import csns.model.academics.Assignment;
import csns.model.academics.Enrollment;
import csns.model.academics.Section;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.SectionDao;
import csns.model.assessment.CourseJournal;
import csns.model.assessment.dao.CourseJournalDao;
import csns.model.core.User;
import csns.model.site.Block;
import csns.model.site.Item;
import csns.model.site.Site;
import csns.security.SecurityUtils;

@Controller
public class SectionJournalController {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private CourseJournalDao courseJournalDao;

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    private static final Logger logger = LoggerFactory.getLogger( SectionJournalController.class );

    @RequestMapping("/section/journal/create")
    public String create( @RequestParam Long sectionId )
    {
        Section section = sectionDao.getSection( sectionId );
        if( section.getJournal() != null )
            return "redirect:view?sectionId=" + sectionId;

        CourseJournal journal = new CourseJournal( section );
        section.setJournal( journal );

        // Populate handouts if the section has a class website
        Site site = section.getSite();
        if( site != null )
            for( Block block : site.getBlocks() )
                if( block.getType().equals( Block.Type.REGULAR ) )
                    for( Item item : block.getItems() )
                        journal.getHandouts().add( item.getResource().clone() );

        // Populate assignments
        journal.getAssignments().addAll( section.getAssignments() );

        section = sectionDao.saveSection( section );
        logger.info( SecurityUtils.getUser().getUsername()
            + " created course journal for section " + sectionId );

        return "redirect:view?sectionId=" + sectionId;
    }

    @RequestMapping("/section/journal/view")
    public String view( @RequestParam Long sectionId, ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        if( section.getJournal() == null )
        {
            models.put( "message", "error.section.nosyllabus" );
            return "error";
        }

        models.put( "section", section );
        User coordinator = section.getCourse().getCoordinator();
        if( coordinator != null )
            models.put( "isCoordinator",
                coordinator.getId().equals( SecurityUtils.getUser().getId() ) );

        return "section/journal/view";
    }

    @RequestMapping("/section/journal/submit")
    public String submit( @RequestParam Long sectionId )
    {
        Section section = sectionDao.getSection( sectionId );
        if( section.getJournal().getSubmitDate() == null )
        {
            section.getJournal().setSubmitDate( new Date() );
            section = sectionDao.saveSection( section );
            logger.info( SecurityUtils.getUser().getUsername()
                + " submitted course journal for section " + sectionId );
        }

        return "redirect:view?sectionId=" + sectionId;
    }

    @RequestMapping("/section/journal/handouts")
    public String handouts( @RequestParam Long sectionId, ModelMap models )
    {
        models.put( "section", sectionDao.getSection( sectionId ) );
        return "section/journal/handouts";
    }

    @RequestMapping("/section/journal/assignments")
    public String assignments( @RequestParam Long sectionId, ModelMap models )
    {
        models.put( "section", sectionDao.getSection( sectionId ) );
        return "section/journal/assignments";
    }

    @RequestMapping("/section/journal/toggleAssignment")
    @ResponseStatus(HttpStatus.OK)
    public void toggleAssignment( @RequestParam Long journalId,
        @RequestParam Long assignmentId )
    {
        User user = SecurityUtils.getUser();
        CourseJournal courseJournal = courseJournalDao.getCourseJournal( journalId );
        Assignment assignment = assignmentDao.getAssignment( assignmentId );

        List<Assignment> assignments = courseJournal.getAssignments();
        if( assignments.contains( assignment ) )
        {
            assignments.remove( assignment );
            logger.info( user.getUsername() + " removed assignment "
                + assignmentId + " from course jorunal " + journalId );
        }
        else
        {
            assignments.add( assignment );
            logger.info( user.getUsername() + " added assignment "
                + assignmentId + " to course jorunal " + journalId );
        }
        courseJournalDao.saveCourseJournal( courseJournal );
    }

    @RequestMapping("/section/journal/samples")
    public String samples( @RequestParam Long sectionId, ModelMap models )
    {
        models.put( "section", sectionDao.getSection( sectionId ) );
        return "section/journal/samples";
    }

    @RequestMapping("/section/journal/toggleEnrollment")
    @ResponseStatus(HttpStatus.OK)
    public void toggleEnrollment( @RequestParam Long journalId,
        @RequestParam Long enrollmentId )
    {
        User user = SecurityUtils.getUser();
        CourseJournal courseJournal = courseJournalDao.getCourseJournal( journalId );
        Enrollment enrollment = enrollmentDao.getEnrollment( enrollmentId );

        List<Enrollment> samples = courseJournal.getStudentSamples();
        if( samples.contains( enrollment ) )
        {
            samples.remove( enrollment );
            logger.info( user.getUsername() + " removed enrollment "
                + enrollmentId + " from course jorunal " + journalId );
        }
        else
        {
            samples.add( enrollment );
            logger.info( user.getUsername() + " added enrollment "
                + enrollmentId + " to course jorunal " + journalId );
        }
        courseJournalDao.saveCourseJournal( courseJournal );
    }

}
