/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Mahdiye Jamali (mjamali@calstatela.edu).
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

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.WebUtils;

import csns.importer.ImportedUser;
import csns.importer.StudentsImporter;
import csns.model.academics.Group;
import csns.model.academics.Standing;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.GroupDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.web.editor.StandingPropertyEditor;

@Controller
@SessionAttributes({ "importer"})
public class GroupUserControllerS {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private GroupDao groupDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private WebApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger( GroupUserControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Standing.class,
            (StandingPropertyEditor) context.getBean( "standingPropertyEditor" ) );
    }

    @RequestMapping(value = "/department/{dept}/group/user/import",
        method = RequestMethod.GET)
    public String importStudents( @PathVariable String dept, @RequestParam Long id, ModelMap models )
    {
        StudentsImporter importer = (StudentsImporter) context.getBean( "studentsImporter" );
        importer.setDepartment( departmentDao.getDepartment( dept ) );
        models.put( "importer", importer );
        models.put("group", groupDao.getGroup(id));
        return "group/user/import0";
    }

    @RequestMapping(value = "/department/{dept}/group/user/import",
        method = RequestMethod.POST)
    public String importStudents(
        @ModelAttribute("importer") StudentsImporter importer,
        @PathVariable String dept, @RequestParam("_page") int currentPage,  @RequestParam Long id,
        HttpServletRequest request, SessionStatus sessionStatus, ModelMap models )
    {
    	Group group = groupDao.getGroup(id);
    	List<User> users = group.getUsers();
    	
        if( request.getParameter( "_finish" ) == null )
        {
            int targetPage = WebUtils.getTargetPage( request, "_target",
                currentPage );
            /*if( targetPage == 1 && currentPage < targetPage )
            {
                for( ImportedUser importedStudent : importer.getImportedStudents() )
                {
                    User user = userDao.getUserByCin( importedStudent.getCin() );
                    if( user == null ) importedStudent.setNewAccount( true );
                }
            }*/
            return "group/user/import" + targetPage;
        }

        // received _finish, so do the import.
        for( ImportedUser importedStudent : importer.getImportedStudents() )
        {
            String cin = importedStudent.getCin();
            User student = userDao.getUserByCin( cin );
            if( student != null ) {
            	users.add(student);
            }
        }
        group = groupDao.saveGroup(group);
        sessionStatus.setComplete();
        
        logger.info(SecurityUtils.getUser() + " imported users to group " + group.getName());

        models.put( "backUrl", "/department/" + dept + "/people#group" );
        return "status";
    }

}
