/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015, Chengyu Sun (csun@calstatela.edu).
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Program;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.ProgramDao;
import csns.security.SecurityUtils;

@Controller
public class ProgramController {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private DepartmentDao departmentDao;

    private static final Logger logger = LoggerFactory.getLogger( ProgramController.class );

    @RequestMapping("/program/search")
    public String search( @RequestParam(required = false) String term,
        ModelMap models )
    {
        if( StringUtils.hasText( term ) )
            models.addAttribute( "programs",
                programDao.searchPrograms( term, -1 ) );

        return "program/search";
    }

    @RequestMapping("/department/{dept}/program/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "program", programDao.getProgram( id ) );
        return "program/view";
    }

    @RequestMapping("/department/{dept}/program/clone")
    public String clone( @RequestParam Long id, ModelMap models )
    {
        Program program = programDao.getProgram( id );
        Program newProgram = programDao.saveProgram( program.clone() );

        logger.info( SecurityUtils.getUser().getUsername()
            + " created program " + newProgram.getId() + " from "
            + program.getId() );

        return "redirect:edit?id=" + newProgram.getId();
    }

    @RequestMapping("/department/{dept}/program/remove")
    public String remove( @RequestParam Long id )
    {
        Program program = programDao.getProgram( id );
        program.setDepartment( null );
        program = programDao.saveProgram( program );

        logger.info( SecurityUtils.getUser().getUsername()
            + " removed program " + program.getId() );

        return "redirect:../programs";
    }

}
