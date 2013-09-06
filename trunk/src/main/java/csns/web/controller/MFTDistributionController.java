/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.MFTDistribution;
import csns.model.assessment.dao.MFTDistributionDao;

@Controller
public class MFTDistributionController {

    @Autowired
    private MFTDistributionDao mftDistributionDao;

    @Autowired
    private DepartmentDao departmentDao;

    @RequestMapping("/department/{dept}/mft/distribution")
    public String distribution( @PathVariable String dept,
        @RequestParam(required = false) Integer year, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        List<Integer> years = mftDistributionDao.getYears( department );
        if( years.size() == 0 ) return "mft/distribution";

        if( year == null ) year = years.get( 0 );
        List<MFTDistribution> distributions = mftDistributionDao.getDistributions(
            department, year );

        models.put( "years", years );
        models.put( "selectedYear", year );
        models.put( "distributions", distributions );
        return "mft/distribution";
    }

}
