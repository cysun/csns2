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

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.Resource;
import csns.model.core.ResourceType;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class SyllabusController {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( SyllabusController.class );

    private Section getSection( String qtr, String cc, int sn )
    {
        Quarter quarter = new Quarter();
        quarter.setShortString( qtr );
        Course course = courseDao.getCourse( cc );
        return sectionDao.getSection( quarter, course, sn );
    }

    private String view( Section section, ModelMap models,
        HttpServletResponse response )
    {
        Resource syllabus = section.getSyllabus();

        if( syllabus == null || syllabus.getType() == ResourceType.NONE )
        {
            models.put( "message", "error.section.nosyllabus" );
            return "error";
        }

        switch( syllabus.getType() )
        {
            case TEXT:
                models.put( "section", section );
                models.put( "syllabus", syllabus );
                return "syllabus/view";

            case FILE:
                fileIO.write( syllabus.getFile(), response );
                return null;

            case URL:
                return "redirect:" + syllabus.getUrl();

            default:
                logger.warn( "Invalid resource type: " + syllabus.getType() );
                models.put( "message", "error.resource.type.invalid" );
                return "error";
        }
    }

    @RequestMapping("/site/{qtr}/{cc}-{sn}/syllabus")
    public String view( @PathVariable String qtr, @PathVariable String cc,
        @PathVariable int sn, ModelMap models, HttpServletResponse response )
    {
        Section section = getSection( qtr, cc, sn );
        models.put( "view", "site" );
        models.put( "isInstructor",
            section.isInstructor( SecurityUtils.getUser() ) );
        return view( section, models, response );
    }

    @RequestMapping("/section/journal/viewSyllabus")
    public String view1( @RequestParam Long sectionId, ModelMap models,
        HttpServletResponse response )
    {
        Section section = sectionDao.getSection( sectionId );
        models.put( "view", "journal1" );
        return view( section, models, response );
    }

    @RequestMapping("/department/{dept}/journal/viewSyllabus")
    public String view2( @RequestParam Long sectionId, ModelMap models,
        HttpServletResponse response )
    {
        Section section = sectionDao.getSection( sectionId );
        models.put( "view", "journal2" );
        return view( section, models, response );
    }

}
