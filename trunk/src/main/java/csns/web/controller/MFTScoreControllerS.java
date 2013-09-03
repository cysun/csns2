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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.WebUtils;

import csns.importer.MFTReportImporter;
import csns.importer.parser.MFTReportParser;
import csns.model.academics.dao.DepartmentDao;
import csns.model.assessment.dao.MFTScoreDao;

@Controller
@SessionAttributes("importer")
public class MFTScoreControllerS {

    @Autowired
    private MFTScoreDao mftScoreDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private MFTReportParser mftReportParser;

    @RequestMapping(value = "/department/{dept}/mft/import",
        method = RequestMethod.GET)
    public String importScoreReport( @PathVariable String dept, ModelMap models )
    {
        MFTReportImporter importer = new MFTReportImporter();
        importer.setDepartment( departmentDao.getDepartment( dept ) );
        models.put( "importer", importer );
        return "mft/import0";
    }

    @RequestMapping(value = "/department/{dept}/mft/import",
        method = RequestMethod.POST)
    public String importScoreReport(
        @ModelAttribute("importer") MFTReportImporter importer,
        @PathVariable String dept, @RequestParam("_page") int currentPage,
        HttpServletRequest request, SessionStatus sessionStatus, ModelMap models )
    {
        Map<Integer, String> views = new HashMap<Integer, String>();
        views.put( 0, "mft/import0" );
        views.put( 1, "mft/import1" );
        
        int targetPage = WebUtils.getTargetPage( request, "_target",
            currentPage );
        if( targetPage == 1 && currentPage < targetPage ) {}
        
        if( request.getParameter( "_finish" ) == null )
        {
            return views.get( targetPage );
        }

        
        return null;
    }

}
