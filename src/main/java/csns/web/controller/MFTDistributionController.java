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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.MFTDistribution;
import csns.model.assessment.MFTDistributionEntry;
import csns.model.assessment.MFTDistributionType;
import csns.model.assessment.dao.MFTDistributionDao;
import csns.model.assessment.dao.MFTDistributionTypeDao;
import csns.security.SecurityUtils;
import csns.web.editor.MFTDistributionTypePropertyEditor;

@Controller
public class MFTDistributionController {

    @Autowired
    private MFTDistributionDao mftDistributionDao;

    @Autowired
    private MFTDistributionTypeDao mftDistributionTypeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private WebApplicationContext context;

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor(
            MFTDistributionType.class,
            (MFTDistributionTypePropertyEditor) context.getBean( "mftDistributionTypePropertyEditor" ) );
    }

    private static final Logger logger = LoggerFactory.getLogger( MFTDistributionController.class );

    @RequestMapping("/department/{dept}/mft/distribution")
    public String distribution( @PathVariable String dept,
        @RequestParam(required = false) Integer year, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        List<Integer> years = mftDistributionDao.getYears( department );
        if( years.size() == 0 ) return "mft/distribution";

        if( year == null ) year = years.get( 0 );
        List<MFTDistribution> distributions = mftDistributionDao.getDistributions(
            year, department );

        models.put( "years", years );
        models.put( "selectedYear", year );
        models.put( "distributions", distributions );
        return "mft/distribution";
    }

    @RequestMapping("/department/{dept}/mft/distribution/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "distribution", mftDistributionDao.getDistribution( id ) );
        return "mft/distribution/view";
    }

    @RequestMapping("/department/{dept}/mft/distribution/delete")
    public String delete( @PathVariable String dept, @RequestParam Long id )
    {
        MFTDistribution distribution = mftDistributionDao.getDistribution( id );
        distribution.setDeleted( true );
        distribution = mftDistributionDao.saveDistribution( distribution );

        logger.info( SecurityUtils.getUser().getUsername()
            + " deleted mft distribution " + distribution.getId() );

        return "redirect:/department/" + dept + "/mft/distribution?year="
            + distribution.getYear();
    }

    @RequestMapping(value = "/department/{dept}/mft/distribution/add",
        method = RequestMethod.GET)
    public String add( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        List<MFTDistributionType> distributionTypes = mftDistributionTypeDao.getDistributionTypes( department );
        MFTDistribution distribution = new MFTDistribution();
        distribution.setType( distributionTypes.get( 0 ) );

        models.put( "distributionTypes", distributionTypes );
        models.put( "distribution", distribution );
        return "mft/distribution/add";
    }

    @RequestMapping(value = "/department/{dept}/mft/distribution/add",
        method = RequestMethod.POST)
    public String add(
        @ModelAttribute("distribution") MFTDistribution distribution,
        @PathVariable String dept )
    {
        MFTDistribution oldDistribution = mftDistributionDao.getDistribution(
            distribution.getYear(), distribution.getType() );
        if( oldDistribution != null )
        {
            oldDistribution.setDeleted( false );
            distribution = mftDistributionDao.saveDistribution( oldDistribution );
            logger.info( SecurityUtils.getUser().getUsername()
                + " resurrected mft distribution " + distribution.getId() );
        }
        else
        {
            distribution.getEntries().add(
                new MFTDistributionEntry( distribution.getType().getMin(), 1 ) );
            distribution = mftDistributionDao.saveDistribution( distribution );
            logger.info( SecurityUtils.getUser().getUsername()
                + " added mft distribution " + distribution.getId() );
        }

        return "redirect:/department/" + dept + "/mft/distribution/edit?id="
            + distribution.getId();
    }

}
