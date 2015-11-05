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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.Objective;
import csns.model.assessment.Program;
import csns.model.assessment.dao.ProgramDao;
import csns.security.SecurityUtils;
import csns.web.validator.AssessmentProgramValidator;

@Controller
@SessionAttributes({ "program", "objective" })
public class AssessmentProgramControllerS {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private AssessmentProgramValidator programValidator;

    private static final Logger logger = LoggerFactory
        .getLogger( AssessmentProgramControllerS.class );

    @RequestMapping(value = "/department/{dept}/assessment/program/add",
        method = RequestMethod.GET)
    public String add( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "program", new Program( department ) );
        return "assessment/program/add";
    }

    @RequestMapping(value = "/department/{dept}/assessment/program/add",
        method = RequestMethod.POST)
    public String add( @ModelAttribute("program" ) Program program,
        BindingResult result, SessionStatus sessionStatus)
    {
        programValidator.validate( program, result );
        if( result.hasErrors() ) return "assessment/program/add";

        program = programDao.saveProgram( program );
        logger.info( SecurityUtils.getUser().getUsername()
            + " added assessment program " + program.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + program.getId();
    }

    @RequestMapping(value = "/department/{dept}/assessment/program/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "program", programDao.getProgram( id ) );
        return "assessment/program/edit";
    }

    @RequestMapping(value = "/department/{dept}/assessment/program/edit",
        method = RequestMethod.POST)
    public String edit( @ModelAttribute("program" ) Program program,
        BindingResult result, SessionStatus sessionStatus)
    {
        programValidator.validate( program, result );
        if( result.hasErrors() ) return "assessment/program/edit";

        program = programDao.saveProgram( program );
        logger.info( SecurityUtils.getUser().getUsername()
            + " edited assessment program " + program.getId() );

        sessionStatus.setComplete();
        return "redirect:view?id=" + program.getId();
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/addObjective",
        method = RequestMethod.GET)
    public String addObjective( @RequestParam Long programId, ModelMap models )
    {
        models.put( "program", programDao.getProgram( programId ) );
        models.put( "objective", new Objective() );
        return "assessment/program/addObjective";
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/addObjective",
        method = RequestMethod.POST)
    public String addObjective( @ModelAttribute Objective objective,
        @RequestParam Long programId )
    {
        if( StringUtils.hasText( objective.getText() ) )
        {
            Program program = programDao.getProgram( programId );
            objective.setProgram( program );
            objective.setIndex( program.getObjectives().size() );
            program.getObjectives().add( objective );
            program = programDao.saveProgram( program );

            logger.info( SecurityUtils.getUser().getUsername()
                + " added an objective to assessment program " + programId );
        }

        return "redirect:objectives?programId=" + programId;
    }

}
