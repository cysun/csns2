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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.prereg.Schedule;
import csns.model.prereg.Section;
import csns.model.prereg.dao.SectionDao;
import csns.security.SecurityUtils;

@Controller
public class PreregSectionController {

    @Autowired
    private SectionDao sectionDao;

    private static final Logger logger = LoggerFactory
        .getLogger( PreregSectionController.class );

    @RequestMapping("/department/{dept}/prereg/section/remove")
    public String remove( @PathVariable String dept, @RequestParam Long id,
        ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        if( section.getRegistrations().size() > 0 )
        {
            models.put( "message", "error.prereg.section.nonempty" );
            models.put( "backUrl", "/department/" + dept
                + "/prereg/registration/list?sectionId=" + id );
            return "error";
        }

        Schedule schedule = section.getSchedule();
        section.setSchedule( null );
        sectionDao.saveSection( section );
        logger.info( SecurityUtils.getUser().getUsername() + " removed section "
            + section.getId() );

        return "redirect:/department/" + dept + "/prereg/schedule/view?id="
            + schedule.getId();
    }

}
