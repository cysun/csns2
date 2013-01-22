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

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.ProjectDao;

@Controller
public class ProjectController {

    @Autowired
    ProjectDao projectDao;

    @Autowired
    DepartmentDao departmentDao;

    @RequestMapping("/department/{dept}/projects")
    public String projects( @PathVariable String dept,
        @RequestParam(required = false) Integer year, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );

        Integer currentYear = Calendar.getInstance().get( Calendar.YEAR );
        if( year == null ) year = currentYear;
        List<Integer> years = projectDao.getProjectYears( department );
        if( !years.contains( currentYear ) ) years.add( 0, currentYear );
        if( !years.contains( currentYear + 1 ) )
            years.add( 0, currentYear + 1 );

        models.put( "department", department );
        models.put( "year", year );
        models.put( "years", years );
        models.put( "projects", projectDao.getProjects( department, year ) );
        return "department/projects";
    }

}
