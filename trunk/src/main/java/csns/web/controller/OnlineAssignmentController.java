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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Assignment;
import csns.model.academics.OnlineAssignment;
import csns.model.academics.Section;
import csns.model.academics.dao.SectionDao;

@Controller
public class OnlineAssignmentController {

    @Autowired
    SectionDao sectionDao;

    @RequestMapping("/assignment/online/list")
    public String list( @RequestParam Long sectionId, ModelMap models )
    {
        Section section = sectionDao.getSection( sectionId );
        models.put( "section", section );

        List<OnlineAssignment> assignments = new ArrayList<OnlineAssignment>();
        for( Assignment a : section.getAssignments() )
            if( a.isOnline() ) assignments.add( (OnlineAssignment) a );
        models.put( "assignments", assignments );

        return "assignment/online/list";
    }

}
