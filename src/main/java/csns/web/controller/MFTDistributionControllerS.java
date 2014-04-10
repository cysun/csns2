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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

import csns.model.assessment.MFTDistribution;
import csns.model.assessment.MFTDistributionEntry;
import csns.model.assessment.dao.MFTDistributionDao;
import csns.security.SecurityUtils;

@Controller
@SessionAttributes("distribution")
public class MFTDistributionControllerS {

    @Autowired
    private MFTDistributionDao mftDistributionDao;

    private static final Logger logger = LoggerFactory.getLogger( MFTDistributionControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder, WebRequest request )
    {
        binder.registerCustomEditor( Date.class, new CustomDateEditor(
            new SimpleDateFormat( "MM/yyyy" ), true ) );
    }

    @RequestMapping(value = "/department/{dept}/mft/distribution/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "distribution", mftDistributionDao.getDistribution( id ) );
        return "mft/distribution/edit";
    }

    @RequestMapping(value = "/department/{dept}/mft/distribution/edit",
        method = RequestMethod.POST)
    public String edit(
        @ModelAttribute("distribution") MFTDistribution distribution,
        @PathVariable String dept, SessionStatus sessionStatus )
    {
        distribution = mftDistributionDao.saveDistribution( distribution );
        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/mft/distribution/view?id="
            + distribution.getId();
    }

    @RequestMapping("/department/{dept}/mft/distribution/addEntry")
    public @ResponseBody
    String addEntry(
        @ModelAttribute("distribution") MFTDistribution distribution,
        @RequestParam int value, @RequestParam int percentile )
    {
        distribution.getEntries().add(
            new MFTDistributionEntry( value, percentile ) );
        logger.info( SecurityUtils.getUser().getUsername() + " added entry ("
            + value + "," + percentile + ") to mft distribution "
            + distribution.getId() );

        return "";
    }

    @RequestMapping("/department/{dept}/mft/distribution/deleteEntry")
    public @ResponseBody
    String deleteEntry(
        @ModelAttribute("distribution") MFTDistribution distribution,
        @RequestParam int value )
    {
        for( MFTDistributionEntry entry : distribution.getEntries() )
            if( entry.getValue() == value )
            {
                distribution.getEntries().remove( entry );
                logger.info( SecurityUtils.getUser().getUsername()
                    + " deleted entry with value " + value
                    + " from mft distribution " + distribution.getId() );
                break;
            }

        return "";
    }

}
