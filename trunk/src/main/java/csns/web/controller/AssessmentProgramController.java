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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.Program;
import csns.model.assessment.dao.ProgramDao;
import csns.security.SecurityUtils;

@Controller
public class AssessmentProgramController {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private DepartmentDao departmentDao;

    private static final Logger logger = LoggerFactory
        .getLogger( AssessmentProgramController.class );

    @RequestMapping("/department/{dept}/assessment/program/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "programs", programDao.getPrograms( department ) );
        return "assessment/program/list";
    }

    @RequestMapping("/department/{dept}/assessment/program/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "program", programDao.getProgram( id ) );
        return "assessment/program/view";
    }

    @RequestMapping("/department/{dept}/assessment/program/remove")
    public String remove( @RequestParam Long id, ModelMap models )
    {
        Program program = programDao.getProgram( id );
        program.setDeleted( true );
        program = programDao.saveProgram( program );

        logger.info( SecurityUtils.getUser().getUsername()
            + " removed assessment program " + program.getId() );

        return "redirect:list";
    }

    @RequestMapping("/department/{dept}/assessment/program/objectives")
    public String objectives( @RequestParam Long programId, ModelMap models )
    {
        models.put( "program", programDao.getProgram( programId ) );
        return "assessment/program/objectives";
    }

}
