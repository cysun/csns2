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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.Assignment;
import csns.model.academics.OnlineAssignment;
import csns.model.academics.Section;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SectionDao;
import csns.web.editor.CalendarPropertyEditor;
import csns.web.validator.AssignmentValidator;

@Controller
@SessionAttributes("assignment")
public class OnlineAssignmentController {

    @Autowired
    SectionDao sectionDao;

    @Autowired
    AssignmentDao assignmentDao;

    @Autowired
    AssignmentValidator assignmentValidator;

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Calendar.class,
            new CalendarPropertyEditor() );
    }

    @RequestMapping("/assignment/online/list")
    public String list( @RequestParam Long sectionId, ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        models.put( "section", section );

        List<OnlineAssignment> assignments = new ArrayList<OnlineAssignment>();
        for( Assignment assignment : section.getAssignments() )
            if( assignment.isOnline() )
                assignments.add( (OnlineAssignment) assignment );
        models.put( "assignments", assignments );

        return "assignment/online/list";
    }

    @RequestMapping(value = "/assignment/online/create",
        method = RequestMethod.GET)
    public String create( @RequestParam Long sectionId, ModelMap models )
    {
        OnlineAssignment assignment = new OnlineAssignment();
        assignment.setSection( sectionDao.getSection( sectionId ) );
        models.put( "assignment", assignment );
        return "assignment/online/create";
    }

    // Remember that the default @ModelAttribute name is inferred from
    // the parameter type, not the parameter name.
    @RequestMapping(value = "/assignment/online/create",
        method = RequestMethod.POST)
    public String create(
        @ModelAttribute("assignment") OnlineAssignment assignment,
        BindingResult result, SessionStatus sessionStatus )
    {
        assignmentValidator.validate( assignment, result );
        if( result.hasErrors() ) return "assignment/online/create";

        if( !StringUtils.hasText( assignment.getAlias() ) )
            assignment.setAlias( assignment.getName() );
        assignment = (OnlineAssignment) assignmentDao.saveAssignment( assignment );

        sessionStatus.setComplete();
        return "redirect:/assignment/online/list?sectionId="
            + assignment.getSection().getId();
    }

}
