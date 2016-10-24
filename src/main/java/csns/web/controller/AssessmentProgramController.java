/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2015-2016, Chengyu Sun (csun@calstatela.edu).
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.Program;
import csns.model.assessment.ProgramObjective;
import csns.model.assessment.ProgramOutcome;
import csns.model.assessment.ProgramSection;
import csns.model.assessment.dao.ProgramDao;
import csns.model.assessment.dao.ProgramObjectiveDao;
import csns.model.assessment.dao.ProgramOutcomeDao;
import csns.security.SecurityUtils;

@Controller
public class AssessmentProgramController {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private ProgramObjectiveDao programObjectiveDao;

    @Autowired
    private ProgramOutcomeDao programOutcomeDao;

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

    @RequestMapping(value = "/department/{dept}/assessment/program/add",
        method = RequestMethod.GET)
    public String add()
    {
        return "assessment/program/add";
    }

    @RequestMapping(value = "/department/{dept}/assessment/program/add",
        method = RequestMethod.POST)
    public String add( @PathVariable String dept, @RequestParam String name )
    {
        Program program = new Program( departmentDao.getDepartment( dept ) );
        program.setName( name );
        program = programDao.saveProgram( program );

        logger.info( SecurityUtils.getUser().getUsername()
            + " added assessment program " + program.getId() );

        return "redirect:edit?id=" + program.getId();
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

    @RequestMapping("/department/{dept}/assessment/program/edit")
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "program", programDao.getProgram( id ) );
        return "assessment/program/edit";
    }

    @RequestMapping(value = "/department/{dept}/assessment/program/{field}/add",
        method = RequestMethod.GET)
    public String add( @PathVariable String field, @RequestParam Long programId,
        ModelMap models )
    {
        models.put( "program", programDao.getProgram( programId ) );
        return "assessment/program/add/" + field;
    }

    @RequestMapping(value = "/department/{dept}/assessment/program/{field}/add",
        method = RequestMethod.POST)
    public String add( @PathVariable String field, @RequestParam Long programId,
        @RequestParam String value )
    {
        Program program = programDao.getProgram( programId );
        switch( field )
        {
            case "objective":
                program.getObjectives()
                    .add( new ProgramObjective( program, value ) );
                break;

            case "outcome":
                program.getOutcomes()
                    .add( new ProgramOutcome( program, value ) );
                break;

            case "section":
                program.getSections().add( new ProgramSection( value ) );
                break;

            default:
                logger.warn( "Unsupported program field: " + field );
        }
        program = programDao.saveProgram( program );

        logger.info( SecurityUtils.getUser().getUsername() + " added " + field
            + " to assessment program" + programId );

        return "redirect:../edit?id=" + programId;
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/{field}/edit",
        method = RequestMethod.GET)
    public String edit( @PathVariable String field,
        @RequestParam Long programId, ModelMap models )
    {
        models.put( "program", programDao.getProgram( programId ) );
        return "assessment/program/edit/" + field;
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/{field}/edit",
        method = RequestMethod.POST)
    public String edit( @PathVariable String field,
        @RequestParam Long programId,
        @RequestParam(required = false) Long fieldId,
        @RequestParam(required = false) Integer index,
        @RequestParam String value )
    {
        Program program = programDao.getProgram( programId );
        switch( field )
        {
            case "name":
                program.setName( value );
                break;

            case "vision":
                program.setVision( value );
                break;

            case "mission":
                program.setMission( value );
                break;

            case "objective":
                program.getObjectives().get( index ).setText( value );
                break;

            case "outcome":
                program.getOutcomes().get( index ).setText( value );
                break;

            case "section":
                program.getSection( fieldId ).setName( value );
                break;

            default:
                logger.warn( "Unsupported program field: " + field );
        }
        program = programDao.saveProgram( program );

        logger.info( SecurityUtils.getUser().getUsername() + " edited " + field
            + " of assessment program" + programId );

        return "redirect:../edit?id=" + programId;
    }

    @RequestMapping("/department/{dept}/assessment/program/{field}/remove")
    public String remove( @PathVariable String field,
        @RequestParam Long programId, @RequestParam int index )
    {
        Program program = programDao.getProgram( programId );
        switch( field )
        {
            case "objective":
                program.getObjectives().get( index ).setProgram( null );
                program.getObjectives().remove( index );
                program.reIndexObjectives();
                break;

            case "outcome":
                program.getOutcomes().get( index ).setProgram( null );
                program.getOutcomes().remove( index );
                program.reIndexOutcomes();
                break;

            default:
                logger.warn( "Unsupported program field: " + field );
        }
        program = programDao.saveProgram( program );

        logger.info( SecurityUtils.getUser().getUsername() + " removed " + field
            + " " + index + " of assessment program" + programId );

        return "redirect:../edit?id=" + programId;
    }

    @RequestMapping("/department/{dept}/assessment/program/{field}/reorder")
    @ResponseStatus(HttpStatus.OK)
    public void reorder( @PathVariable String field,
        @RequestParam Long programId, @RequestParam Long fieldId,
        @RequestParam int newIndex )
    {
        Program program = programDao.getProgram( programId );
        switch( field )
        {
            case "section":
                ProgramSection section = program.removeSection( fieldId );
                if( section != null )
                    program.getSections().add( newIndex, section );
                break;

            default:
                logger.warn( "Unsupported program field: " + field );
        }
        program = programDao.saveProgram( program );

        logger.info( SecurityUtils.getUser().getUsername() + " reordered "
            + field + " " + fieldId + " of assessment program " + programId
            + " to index " + newIndex );
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/{field}/description",
        method = RequestMethod.GET)
    public String description( @PathVariable String field,
        @RequestParam Long fieldId, ModelMap models )
    {
        switch( field )
        {
            case "objective":
                models.put( "objective",
                    programObjectiveDao.getProgramObjective( fieldId ) );
                return "assessment/program/objective/description";

            default:
                models.put( "outcome",
                    programOutcomeDao.getProgramOutcome( fieldId ) );
                return "assessment/program/outcome/description";
        }
    }

    @RequestMapping(
        value = "/department/{dept}/assessment/program/{field}/description",
        method = RequestMethod.POST)
    public String description( @PathVariable String field,
        @RequestParam Long fieldId, @RequestParam String description )
    {
        switch( field )
        {
            case "objective":
                ProgramObjective objective = programObjectiveDao
                    .getProgramObjective( fieldId );
                objective.setDescription( description );
                objective = programObjectiveDao
                    .saveProgramObjective( objective );
                break;

            default:
                ProgramOutcome outcome = programOutcomeDao
                    .getProgramOutcome( fieldId );
                outcome.setDescription( description );
                outcome = programOutcomeDao.saveProgramOutcome( outcome );
        }

        logger.info( SecurityUtils.getUser().getUsername()
            + " edited the description of program " + field + " " + fieldId );

        return "redirect:description?fieldId=" + fieldId;
    }

}
