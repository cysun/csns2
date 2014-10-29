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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Department;
import csns.model.academics.Section;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.SectionDao;
import csns.model.assessment.CourseJournal;
import csns.model.assessment.dao.CourseJournalDao;
import csns.model.site.Block;
import csns.model.site.Item;
import csns.model.site.Site;
import csns.security.SecurityUtils;

@Controller
public class CourseJournalController {

    // XXX INCOMPLETE

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CourseJournalDao courseJournalDao;

    private static final Logger logger = LoggerFactory.getLogger( CourseJournalController.class );

    @RequestMapping("/department/{dept}/journal/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "department", department );
        models.put( "submittedJournals",
            courseJournalDao.getSubmittedCourseJournals( department ) );
        return "journal/list";
    }

    @RequestMapping("/department/{dept}/journal/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "courseJournal", courseJournalDao.getCourseJournal( id ) );
        return "journal/view";
    }

    @RequestMapping("/department/{dept}/journal/edit")
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "courseJournal", courseJournalDao.getCourseJournal( id ) );
        return "journal/edit";
    }

    @RequestMapping("/department/{dept}/journal/create")
    public String create( @RequestParam Long sectionId )
    {
        Section section = sectionDao.getSection( sectionId );
        if( section.getCourseJournal() != null )
            return "redirect:view?id=" + section.getCourseJournal().getId();

        CourseJournal courseJournal = new CourseJournal( section );

        // Populate handouts if the section has a class website
        Site site = section.getSite();
        if( site != null )
            for( Block block : site.getBlocks() )
                if( block.getType().equals( Block.Type.REGULAR ) )
                    for( Item item : block.getItems() )
                        courseJournal.getHandouts().add( item.getResource() );

        // Populate assignments
        courseJournal.getAssignments().addAll( section.getAssignments() );

        courseJournal = courseJournalDao.saveCourseJournal( courseJournal );
        logger.info( SecurityUtils.getUser().getUsername()
            + " created course journal " + courseJournal.getId() );

        return "redirect?view?id=" + courseJournal.getId();
    }

}
