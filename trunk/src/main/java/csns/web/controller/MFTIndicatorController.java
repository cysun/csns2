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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import csns.helper.highcharts.Chart;
import csns.helper.highcharts.Series;
import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.MFTDistribution;
import csns.model.assessment.MFTDistributionType;
import csns.model.assessment.MFTIndicator;
import csns.model.assessment.dao.MFTDistributionDao;
import csns.model.assessment.dao.MFTDistributionTypeDao;
import csns.model.assessment.dao.MFTIndicatorDao;
import csns.security.SecurityUtils;
import csns.util.DateUtils;

@Controller
public class MFTIndicatorController {

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    private MFTIndicatorDao mftIndicatorDao;

    @Autowired
    private MFTDistributionDao mftDistributionDao;

    @Autowired
    private MFTDistributionTypeDao mftDistributionTypeDao;

    private static final Logger logger = LoggerFactory.getLogger( MFTIndicatorController.class );

    @InitBinder
    public void initBinder( WebDataBinder binder, WebRequest request )
    {
        binder.registerCustomEditor( Date.class, new CustomDateEditor(
            new SimpleDateFormat( "MM/dd/yyyy" ), true ) );
    }

    @RequestMapping("/department/{dept}/mft/ai")
    public String ai( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );

        List<MFTIndicator> indicators = mftIndicatorDao.getIndicators( department );
        for( MFTIndicator indicator : indicators )
            getPercentile( department, indicator );

        models.put( "indicator", new MFTIndicator() );
        models.put( "indicators", indicators );

        List<Integer> years = mftIndicatorDao.getYears( department );
        if( years.size() > 0 )
        {
            Integer beginYear = years.size() < 6 ? years.get( 0 )
                : years.get( years.size() - 6 );
            Integer endYear = years.get( years.size() - 1 );
            models.put( "beginYear", beginYear );
            models.put( "endYear", endYear );
            models.put( "years", years );
        }

        return "mft/ai";
    }

    @RequestMapping("/department/{dept}/mft/ai/update")
    public String update( @ModelAttribute("indicator") MFTIndicator indicator,
        @PathVariable String dept )
    {
        Department department = departmentDao.getDepartment( dept );
        MFTIndicator oldIndicator = mftIndicatorDao.getIndicator( department,
            indicator.getDate() );
        if( oldIndicator != null )
        {
            oldIndicator.setAi1( indicator.getAi1() );
            oldIndicator.setAi2( indicator.getAi2() );
            oldIndicator.setAi3( indicator.getAi3() );
            oldIndicator.setDeleted( false );
            mftIndicatorDao.saveIndicator( oldIndicator );
            logger.info( SecurityUtils.getUser().getUsername()
                + " updated mft indicator " + oldIndicator.getId() );
        }
        else
        {
            indicator.setDepartment( department );
            indicator = mftIndicatorDao.saveIndicator( indicator );
            logger.info( SecurityUtils.getUser().getUsername()
                + " added mft indicator " + indicator.getId() );
        }

        return "redirect:/department/" + dept + "/mft/ai";
    }

    @RequestMapping("/department/{dept}/mft/ai/delete")
    public String delete( @PathVariable String dept, @RequestParam Long id )
    {
        MFTIndicator indicator = mftIndicatorDao.getIndicator( id );
        indicator.setDeleted( true );
        indicator = mftIndicatorDao.saveIndicator( indicator );

        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted mft indicator " + indicator.getId() );

        return "redirect:/department/" + dept + "/mft/ai";
    }

    @RequestMapping("/department/{dept}/mft/ai/chart")
    public String chart( @PathVariable String dept,
        @RequestParam String chartType, @RequestParam Integer beginYear,
        @RequestParam Integer endYear, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        List<MFTIndicator> indicators = mftIndicatorDao.getIndicators(
            department, beginYear, endYear );

        Iterator<MFTIndicator> iterator = indicators.iterator();
        MFTIndicator currentInd = iterator.next();
        while( iterator.hasNext() )
        {
            MFTIndicator nextInd = iterator.next();
            if( DateUtils.getYear( currentInd.getDate() ) == DateUtils.getYear( nextInd.getDate() ) )
            {
                currentInd.merge( nextInd );
                iterator.remove();
            }
            else
                currentInd = nextInd;
        }

        Chart chart = new Chart( "MFT Assessment Indicators (" + beginYear
            + "-" + endYear + ")", "Year", chartType.equalsIgnoreCase( "score" )
            ? "Mean Score" : "National Percentile" );
        chart.getxAxis().setCategories( beginYear, endYear );
        chart.getyAxis().setMax( 100 );

        if( chartType.equalsIgnoreCase( "percentile" ) )
            for( MFTIndicator indicator : indicators )
                getPercentile( department, indicator );

        chart.getSeries().add( getSeries( chartType, "AI-1", indicators ) );
        chart.getSeries().add( getSeries( chartType, "AI-2", indicators ) );
        chart.getSeries().add( getSeries( chartType, "AI-3", indicators ) );

        models.put( "chart", chart );
        return "jsonView";
    }

    private void getPercentile( Department department, MFTIndicator indicator )
    {
        String indicatorAliases[] = { "AI1", "AI2", "AI3" };

        for( String indicatorAlias : indicatorAliases )
        {
            MFTDistributionType distType = mftDistributionTypeDao.getDistributionType(
                department, indicatorAlias );
            if( distType == null ) continue;

            MFTDistribution distribution = mftDistributionDao.getDistribution(
                indicator.getDate(), distType );
            if( distribution == null || distribution.isDeleted() ) return;

            switch( indicatorAlias )
            {
                case "AI1":
                    indicator.setAi1Percentile( distribution.getPercentile( indicator.getAi1()
                        .doubleValue() ) );
                    break;

                case "AI2":
                    indicator.setAi2Percentile( distribution.getPercentile( indicator.getAi2()
                        .doubleValue() ) );
                    break;

                case "AI3":
                    indicator.setAi3Percentile( distribution.getPercentile( indicator.getAi3()
                        .doubleValue() ) );
                    break;
            }
        }
    }

    private Series getSeries( String chartType, String indicatorAlias,
        List<MFTIndicator> indicators )
    {
        List<Integer> data = new ArrayList<Integer>();
        for( MFTIndicator indicator : indicators )
        {
            switch( indicatorAlias )
            {
                case "AI-1":
                    data.add( chartType.equalsIgnoreCase( "score" )
                        ? indicator.getAi1() : indicator.getAi1Percentile() );
                    break;

                case "AI-2":
                    data.add( chartType.equalsIgnoreCase( "score" )
                        ? indicator.getAi2() : indicator.getAi2Percentile() );
                    break;

                case "AI-3":
                    data.add( chartType.equalsIgnoreCase( "score" )
                        ? indicator.getAi3() : indicator.getAi3Percentile() );
                    break;
            }
        }

        return new Series( indicatorAlias, data );
    }

}
