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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;

import csns.model.academics.Course;
import csns.model.prereg.Schedule;
import csns.model.prereg.Section;
import csns.model.prereg.dao.ScheduleDao;
import csns.model.prereg.dao.SectionDao;
import csns.security.SecurityUtils;
import csns.web.editor.CoursePropertyEditor;
import csns.web.editor.PreregSectionPropertyEditor;

@Controller
@SessionAttributes("section")
public class PreregSectionControllerS {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private WebApplicationContext context;

    private static final Logger logger = LoggerFactory
        .getLogger( PreregSectionControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Course.class,
            (CoursePropertyEditor) context.getBean( "coursePropertyEditor" ) );
        binder.registerCustomEditor( Section.class,
            (PreregSectionPropertyEditor) context
                .getBean( "preregSectionPropertyEditor" ) );
    }

    @RequestMapping(value = "/department/{dept}/prereg/section/add",
        method = RequestMethod.GET)
    public String add( @RequestParam Long scheduleId, ModelMap models )
    {
        Schedule schedule = scheduleDao.getSchedule( scheduleId );
        models.put( "section", new Section( schedule ) );
        return "prereg/section/add";
    }

    @RequestMapping(value = "/department/{dept}/prereg/section/add",
        method = RequestMethod.POST)
    public String add( @ModelAttribute Section section,
        SessionStatus sessionStatus )
    {
        section = sectionDao.saveSection( section );
        logger.info( SecurityUtils.getUser().getUsername() + " added section "
            + section.getId() );

        sessionStatus.setComplete();
        return "redirect:../schedule/view?id=" + section.getSchedule().getId();
    }

    @RequestMapping(value = "/department/{dept}/prereg/section/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        models.put( "section", section );

        List<Section> otherCourseSections = new ArrayList<Section>();
        for( Section s : section.getSchedule().getSections() )
            if( s != section && s.getCourse() == section.getCourse() )
                otherCourseSections.add( s );
        models.put( "otherCourseSections", otherCourseSections );

        return "prereg/section/edit";
    }

    @RequestMapping(value = "/department/{dept}/prereg/section/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Section section,
        SessionStatus sessionStatus )
    {
        section = sectionDao.saveSection( section );
        logger.info( SecurityUtils.getUser().getUsername() + " edited section "
            + section.getId() );

        sessionStatus.setComplete();
        return "redirect:../schedule/view?id=" + section.getSchedule().getId()
            + "#" + section.getId();
    }

}
