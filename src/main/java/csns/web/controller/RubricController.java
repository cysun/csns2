/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.Rubric;
import csns.model.assessment.dao.RubricDao;
import csns.model.core.User;
import csns.security.SecurityUtils;

@Controller
public class RubricController {

    @Autowired
    private RubricDao rubricDao;

    @Autowired
    private DepartmentDao departmentDao;

    private static final Logger logger = LoggerFactory.getLogger( RubricController.class );

    @RequestMapping("/department/{dept}/rubric/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        User creator = SecurityUtils.getUser();
        models.put( "personalRubrics", rubricDao.getPersonalRubrics( creator ) );

        Department department = departmentDao.getDepartment( dept );
        models.put( "departmentRubrics",
            rubricDao.getDepartmentRubrics( department ) );

        return "rubric/list";
    }

    @RequestMapping("/department/{dept}/rubric/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "rubric", rubricDao.getRubric( id ) );
        return "rubric/view";
    }

    @RequestMapping("/department/{dept}/rubric/toggle")
    public String toggle( @RequestParam Long id, ModelMap models )
    {
        Rubric rubric = rubricDao.getRubric( id );
        rubric.setPublic( !rubric.isPublic() );
        rubric = rubricDao.saveRubric( rubric );

        logger.info( SecurityUtils.getUser().getUsername() + " set rubric "
            + rubric.getId() + " to "
            + (rubric.isPublic() ? "public" : "private") );

        models.put( "rubric", rubric );
        return "rubric/toggle";
    }

    @RequestMapping("/department/{dept}/rubric/clone")
    public String clone( @RequestParam Long id )
    {
        Rubric oldRubric = rubricDao.getRubric( id );
        Rubric newRubric = oldRubric.clone();
        newRubric.setCreator( SecurityUtils.getUser() );
        newRubric = rubricDao.saveRubric( newRubric );

        logger.info( SecurityUtils.getUser().getUsername() + " cloned rubric "
            + newRubric.getId() + " from " + oldRubric.getId() );

        return "redirect:edit?id=" + newRubric.getId();
    }

    @RequestMapping("/department/{dept}/rubric/publish")
    public @ResponseBody
    String publish( @RequestParam Long id )
    {
        Rubric rubric = rubricDao.getRubric( id );
        if( !rubric.isPublished() )
        {
            rubric.setPublishDate( Calendar.getInstance() );
            rubric = rubricDao.saveRubric( rubric );
            logger.info( SecurityUtils.getUser().getUsername()
                + " published rubric " + rubric.getId() );
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        return dateFormat.format( rubric.getPublishDate().getTime() );
    }

    @RequestMapping("/department/{dept}/rubric/delete")
    public String delete( @RequestParam Long id )
    {
        Rubric rubric = rubricDao.getRubric( id );
        if( !rubric.isDeleted() )
        {
            rubric.setDeleted( true );
            rubric = rubricDao.saveRubric( rubric );
            logger.info( SecurityUtils.getUser().getUsername()
                + " deleted rubric " + rubric.getId() );
        }

        return "redirect:list";
    }

    @RequestMapping("/department/{dept}/rubric/promote")
    public String promote( @PathVariable String dept, @RequestParam Long id )
    {
        Rubric rubric = rubricDao.getRubric( id );
        if( rubric.getDepartment() == null )
        {
            rubric.setDepartment( departmentDao.getDepartment( dept ) );
            // Department rubrics are always public.
            rubric.setPublic( true );
            rubric = rubricDao.saveRubric( rubric );
            logger.info( SecurityUtils.getUser().getUsername()
                + " promoted rubric " + rubric.getId() );
        }

        return "redirect:view?id=" + rubric.getId();
    }

    public String search( @RequestParam(required = false) String term,
        ModelMap models )
    {
        List<Rubric> rubrics = null;
        if( StringUtils.hasText( term ) )
            rubrics = rubricDao.searchRubrics( term, -1 );

        models.addAttribute( "searchResults", rubrics );
        return "rubric/search";
    }

    @RequestMapping("/department/{dept}/rubric/search")
    public String search( @RequestParam String term, HttpSession session )
    {
        List<Rubric> rubrics = rubricDao.searchRubrics( term, -1 );
        session.setAttribute( "rubricSearchTerm", term );
        session.setAttribute( "rubricSearchResults", rubrics );
        return "redirect:list#search";
    }

}
