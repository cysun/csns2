/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Mahdiye Jamali (mjamali@calstatela.edu).
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.WebRequest;

import csns.model.academics.Term;
import csns.model.academics.dao.DepartmentDao;
import csns.model.prereg.Schedule;
import csns.model.prereg.dao.ScheduleDao;
import csns.security.SecurityUtils;
import csns.web.editor.TermPropertyEditor;

@Controller
@SessionAttributes({ "schedule", "terms" })
public class PreregScheduleControllerS {

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private WebApplicationContext context;

    private static final Logger logger = LoggerFactory
        .getLogger( PreregScheduleControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder, WebRequest request )
    {
        binder.registerCustomEditor( Term.class,
            (TermPropertyEditor) context.getBean( "termPropertyEditor" ) );
        binder.registerCustomEditor( Date.class, new CustomDateEditor(
            new SimpleDateFormat( "MM/dd/yyyy" ), true ) );
    }

    @RequestMapping(value = "/department/{dept}/prereg/schedule/create",
        method = RequestMethod.GET)
    public String create( @PathVariable String dept, ModelMap models )
    {
        List<Term> terms = new ArrayList<Term>();
        terms.add( (new Term()).next() );
        for( int i = 0; i < 2; ++i )
            terms.add( terms.get( i ).next() );

        models.put( "terms", terms );
        models.put( "schedule", new Schedule(
            departmentDao.getDepartment( dept ), terms.get( 0 ) ) );
        return "prereg/schedule/create";
    }

    @RequestMapping(value = "/department/{dept}/prereg/schedule/create",
        method = RequestMethod.POST)
    public String create( @ModelAttribute Schedule schedule,
        SessionStatus sessionStatus )
    {
        schedule = scheduleDao.saveSchedule( schedule );
        logger.info( SecurityUtils.getUser().getUsername()
            + " created schedule " + schedule.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + schedule.getId();
    }

    @RequestMapping(value = "/department/{dept}/prereg/schedule/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        List<Term> terms = new ArrayList<Term>();
        terms.add( new Term() );
        for( int i = 0; i < 3; ++i )
            terms.add( terms.get( i ).next() );

        models.put( "terms", terms );
        models.put( "schedule", scheduleDao.getSchedule( id ) );
        return "prereg/schedule/edit";
    }

    @RequestMapping(value = "/department/{dept}/prereg/schedule/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute Schedule schedule,
        SessionStatus sessionStatus )
    {
        schedule = scheduleDao.saveSchedule( schedule );
        logger.info( SecurityUtils.getUser().getUsername() + " edited schedule "
            + schedule.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + schedule.getId();
    }

}
