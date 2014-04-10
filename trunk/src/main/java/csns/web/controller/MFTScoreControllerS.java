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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import csns.importer.MFTScoreImporter;
import csns.importer.parser.MFTScoreParser;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.MFTScore;
import csns.model.assessment.dao.MFTScoreDao;
import csns.security.SecurityUtils;
import csns.web.validator.MFTScoreImporterValidator;

@Controller
@SessionAttributes("importer")
public class MFTScoreControllerS {

    @Autowired
    private MFTScoreDao mftScoreDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private MFTScoreParser mftScoreParser;

    @Autowired
    private MFTScoreImporterValidator importerValidator;

    private static final Logger logger = LoggerFactory.getLogger( MFTScoreController.class );

    @InitBinder
    public void initBinder( WebDataBinder binder, WebRequest request )
    {
        binder.registerCustomEditor( Date.class, new CustomDateEditor(
            new SimpleDateFormat( "MM/dd/yyyy" ), true ) );
    }

    @RequestMapping(value = "/department/{dept}/mft/import",
        method = RequestMethod.GET)
    public String importScoreReport( ModelMap models )
    {
        models.put( "importer", new MFTScoreImporter() );
        return "mft/import0";
    }

    @RequestMapping(value = "/department/{dept}/mft/import",
        method = RequestMethod.POST)
    public String importScoreReport(
        @ModelAttribute("importer") MFTScoreImporter importer,
        @PathVariable String dept, @RequestParam("_page") int currentPage,
        HttpServletRequest request, BindingResult result,
        SessionStatus sessionStatus, ModelMap models )
    {
        int targetPage = WebUtils.getTargetPage( request, "_target",
            currentPage );

        if( targetPage == 0 )
        {
            importer.clear();
            return "mft/import0";
        }

        if( targetPage == 1 )
        {
            importerValidator.validate( importer, result );
            if( result.hasErrors() ) return "mft/import0";

            importer.setDepartment( departmentDao.getDepartment( dept ) );
            mftScoreParser.parse( importer );
            return "mft/import1";
        }

        for( MFTScore score : importer.getScores() )
            mftScoreDao.saveScore( score );

        String date = (new SimpleDateFormat( "yyyy-MM-dd" )).format( importer.getDate() );
        logger.info( SecurityUtils.getUser().getUsername() + " imported "
            + importer.getScores().size() + " mft scores for [" + dept + ","
            + date + "]." );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/mft/score?date=" + date;
    }

}
