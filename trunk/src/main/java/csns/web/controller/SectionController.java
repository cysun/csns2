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

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.QuarterDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.security.SecurityUtils;
import csns.web.editor.QuarterPropertyEditor;

@Controller
public class SectionController {

    @Autowired
    QuarterDao quarterDao;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    WebApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger( SectionController.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Quarter.class,
            (QuarterPropertyEditor) context.getBean( "quarterPropertyEditor" ) );
    }

    @RequestMapping("/section/list/{type}")
    public String list( @PathVariable String type,
        @RequestParam(required = false) Quarter quarter, ModelMap models,
        HttpSession session )
    {
        if( quarter != null )
            session.setAttribute( "quarter", quarter );
        else if( session.getAttribute( "quarter" ) != null )
            quarter = (Quarter) session.getAttribute( "quarter" );
        else
            quarter = new Quarter();

        User user = SecurityUtils.getUser();
        List<Quarter> quarters = null;
        List<Section> sections = null;
        String view = "error";
        switch( type )
        {
            case "taught":
                quarters = quarterDao.getQuartersByInstructor( user );
                sections = sectionDao.getSectionsByInstructor( user, quarter );
                view = "section/taught";
                break;
            case "taken":
                quarters = quarterDao.getQuartersByStudent( user );
                sections = sectionDao.getSectionsByStudent( user, quarter );
                view = "section/taken";
                break;
            default:
                logger.warn( "Invalid section type: " + type );
        }

        Quarter currentQuarter = new Quarter();
        if( !quarters.contains( currentQuarter ) )
            quarters.add( 0, currentQuarter );

        models.put( "quarter", quarter );
        models.put( "quarters", quarters );
        models.put( "sections", sections );

        return view;
    }

}
