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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.MFTIndicator;
import csns.model.assessment.dao.MFTIndicatorDao;
import csns.security.SecurityUtils;

@Controller
public class MFTIndicatorController {

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    private MFTIndicatorDao mftIndicatorDao;

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
        models.put( "indicator", new MFTIndicator() );
        models.put( "indicators", mftIndicatorDao.getIndicators( department ) );
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

}
