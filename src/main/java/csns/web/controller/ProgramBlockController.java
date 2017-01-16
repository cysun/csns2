/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2017, Chengyu Sun (csun@calstatela.edu).
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import csns.model.academics.Course;
import csns.model.academics.Program;
import csns.model.academics.ProgramBlock;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.ProgramBlockDao;
import csns.model.academics.dao.ProgramDao;
import csns.security.SecurityUtils;

@Controller
public class ProgramBlockController {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private ProgramBlockDao programBlockDao;

    private static final Logger logger = LoggerFactory
        .getLogger( ProgramBlockController.class );

    @RequestMapping("/department/{dept}/program/block/list")
    public String list( @RequestParam Long programId, ModelMap models )
    {
        models.put( "program", programDao.getProgram( programId ) );
        return "program/block/list";
    }

    @RequestMapping("/department/{dept}/program/block/reorder")
    @ResponseBody
    public void reorder( @RequestParam Long programId,
        @RequestParam Long blockId, @RequestParam int newIndex )
    {
        Program program = programDao.getProgram( programId );
        ProgramBlock block = program.removeBlock( blockId );
        if( block != null )
        {
            program.getBlocks().add( newIndex, block );
            program = programDao.saveProgram( program );

            logger.info( SecurityUtils.getUser().getUsername()
                + " reordered block " + blockId + " to index " + newIndex
                + " in program " + programId );
        }
    }

    @RequestMapping("/department/{dept}/program/block/remove")
    public String remove( @RequestParam Long programId,
        @RequestParam Long blockId )
    {
        Program program = programDao.getProgram( programId );
        ProgramBlock block = program.removeBlock( blockId );
        if( block != null )
        {
            program = programDao.saveProgram( program );
            logger.info( SecurityUtils.getUser().getUsername()
                + " removed block " + blockId + " from program " + programId );
        }

        return "redirect:list?programId=" + programId;
    }

    @RequestMapping("/department/{dept}/program/block/addCourse")
    public String addCourse( @RequestParam Long programId,
        @RequestParam Long blockId, @RequestParam Long courseId )
    {
        Course course = courseDao.getCourse( courseId );
        ProgramBlock block = programBlockDao.getProgramBlock( blockId );
        if( !block.getCourses().contains( course ) )
        {
            block.getCourses().add( course );
            block = programBlockDao.saveProgramBlock( block );
        }

        logger.info( SecurityUtils.getUser().getUsername() + " added course "
            + courseId + " to program block " + blockId );

        return "redirect:list?programId=" + programId + "#block-" + blockId;
    }

    @RequestMapping("/department/{dept}/program/block/removeCourse")
    public String removeCourse( @RequestParam Long programId,
        @RequestParam Long blockId, @RequestParam Long courseId )
    {
        ProgramBlock block = programBlockDao.getProgramBlock( blockId );
        Course course = block.removeCourse( courseId );
        if( course != null )
        {
            block = programBlockDao.saveProgramBlock( block );
            logger.info(
                SecurityUtils.getUser().getUsername() + " removed course "
                    + courseId + " from program block " + blockId );
        }

        return "redirect:list?programId=" + programId + "#block-" + blockId;
    }

}
