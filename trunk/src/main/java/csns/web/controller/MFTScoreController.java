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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.MFTDistribution;
import csns.model.assessment.MFTDistributionType;
import csns.model.assessment.MFTScore;
import csns.model.assessment.dao.MFTDistributionDao;
import csns.model.assessment.dao.MFTDistributionTypeDao;
import csns.model.assessment.dao.MFTScoreDao;

@Controller
public class MFTScoreController {

    @Autowired
    private MFTScoreDao mftScoreDao;

    @Autowired
    private MFTDistributionDao mftDistributionDao;

    @Autowired
    private MFTDistributionTypeDao mftDistributionTypeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @InitBinder
    public void initBinder( WebDataBinder binder, WebRequest request )
    {
        binder.registerCustomEditor( Date.class, new CustomDateEditor(
            new SimpleDateFormat( "yyyy-MM-dd" ), true ) );
    }

    @RequestMapping("/department/{dept}/mft/overview")
    public String overview()
    {
        return "mft/overview";
    }

    @RequestMapping("/department/{dept}/mft/score")
    public String score( @PathVariable String dept,
        @RequestParam(required = false) Date date, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        List<Date> dates = mftScoreDao.getDates( department );
        if( dates.size() == 0 ) return "mft/score";

        if( date == null ) date = dates.get( 0 );
        List<MFTScore> scores = mftScoreDao.getScores( department, date );
        getPercentile( department, date, scores );

        models.put( "dates", dates );
        models.put( "selectedDate", date );
        models.put( "scores", scores );
        return "mft/score";
    }

    private void getPercentile( Department department, Date date,
        List<MFTScore> scores )
    {
        MFTDistributionType distType = mftDistributionTypeDao.getDistributionType(
            department, "Student" );
        if( distType == null ) return;

        MFTDistribution distribution = mftDistributionDao.getDistribution(
            date, distType );
        if( distribution == null ) return;

        for( MFTScore score : scores )
            score.setPercentile( distribution.getPercentile( (double) score.getValue() ) );
    }

}
