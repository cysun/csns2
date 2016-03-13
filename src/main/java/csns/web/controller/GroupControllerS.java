/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Mahdiye Jamali (mjamali@calstatela.edu).
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

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import csns.model.academics.Group;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.GroupDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.web.validator.GroupValidator;

@Controller
@SessionAttributes("group")
public class GroupControllerS {
	private static final Logger logger = LoggerFactory.getLogger(GroupControllerS.class);

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private GroupValidator groupValidator;
    @Resource(name = "contentTypes")
    private Properties contentTypes;

	@RequestMapping(value = "/department/{dept}/group/add", method = RequestMethod.GET)
	public String add(@PathVariable String dept, ModelMap models) {
		Group group = new Group();
		group.setDepartment(departmentDao.getDepartment(dept));
		models.put("group", group);
		models.put("dept", dept);
		return "group/add";
	}

	@RequestMapping(value = "/department/{dept}/group/add", method = RequestMethod.POST)
	public String add(@PathVariable String dept, @ModelAttribute Group group,
			@RequestParam(required = false) Long addUserId, @RequestParam(required = false) Long removeUserId,
			BindingResult result, SessionStatus sessionStatus, ModelMap models) {

		// ------------add individual users------------//
		if (addUserId != null) {
			List<User> users = group.getUsers();
			boolean add = true;
			for (User u : users) {
				if (u.getId().equals(addUserId))
					add = false;
			}
			if (add)
				users.add(userDao.getUser(addUserId));

			models.put("group", group);
			models.put("dept", dept);
			return "group/add";
		}
		// ---------------------------------------------//

		// ------------remove individual users------------//
		if (removeUserId != null) {
			List<User> users = group.getUsers();
			for (User u : users) {
				if (u.getId().equals(removeUserId)) {
					users.remove(u);
					break;
				}
			}

			models.put("group", group);
			models.put("dept", dept);
			return "group/add";
		}
		// ---------------------------------------------//
		groupValidator.validate(group, result);
		if (result.hasErrors()) {
			models.put("group", group);
			models.put("dept", dept);
			return "group/add";
		}

		// -------------import users from file provided--------//
		// -------------------------------------------------//

		group = groupDao.saveGroup(group);

		logger.info(SecurityUtils.getUser().getUsername() + " posted popup " + group.getId());

		return "redirect:/department/" + dept + "/people#group";
	}

	@RequestMapping(value = "/department/{dept}/group/edit", method = RequestMethod.GET)
	public String edit(@PathVariable String dept, @RequestParam Long id, ModelMap models) {
		models.put("group", groupDao.getGroup(id));
		models.put("dept", dept);
		return "group/edit";
	}

	@RequestMapping(value = "/department/{dept}/group/edit", method = RequestMethod.POST)
	public String edit(@PathVariable String dept, @ModelAttribute Group group,
			@RequestParam(required = false) Long addUserId, @RequestParam(required = false) Long removeUserId,
			BindingResult result, SessionStatus sessionStatus, ModelMap models) {

		// ------------add individual users------------//
		if (addUserId != null) {
			List<User> users = group.getUsers();
			boolean add = true;
			for (User u : users) {
				if (u.getId().equals(addUserId))
					add = false;
			}
			if (add)
				users.add(userDao.getUser(addUserId));

			models.put("group", group);
			models.put("dept", dept);
			return "group/edit";
		}
		// ---------------------------------------------//
		// ------------remove individual users------------//
		if (removeUserId != null) {
			List<User> users = group.getUsers();
			for (User u : users) {
				if (u.getId().equals(removeUserId)) {
					users.remove(u);
					break;
				}
			}

			models.put("group", group);
			models.put("dept", dept);
			return "group/edit";
		}
		// ---------------------------------------------//
		// ---------------------------------------------//
		groupValidator.validate(group, result);
		if (result.hasErrors()) {
			models.put("group", group);
			models.put("dept", dept);
			return "group/edit";
		}

		// -------------import users from file provided--------//
		// ----------------------------------------------------//

		group = groupDao.saveGroup(group);

		logger.info(SecurityUtils.getUser().getUsername() + " edited popup " + group.getId());

		return "redirect:/department/" + dept + "/people#group";
	}
	
	@RequestMapping("/department/{dept}/group/export")
    public String export( @RequestParam Long id, HttpServletResponse response )
        throws IOException
    {
        Group group = groupDao.getGroup(id);
        List<User> users = group.getUsers();

        response.setContentType( contentTypes.getProperty( "xlsx" ) );
        response.setHeader( "Content-Disposition", "attachment; filename="
            + group.getName() + "-"
            + "users" + ".xlsx" );

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet( "Users" );

        int n = users.size();
        Row row = sheet.createRow( 0 );
        row.createCell( 0 ).setCellValue( "CIN" );
        row.createCell( 1 ).setCellValue( "Name" );
        row.createCell( 2 ).setCellValue( "Primary Email" );
        
        for(int i = 0; i < n; i++) {
        	row = sheet.createRow( i+1 );
        	row.createCell( 0 ).setCellValue( users.get(i).getCin() );
            row.createCell( 1 ).setCellValue( users.get(i).getName() );
            row.createCell( 2 ).setCellValue( users.get(i).getPrimaryEmail() );
        }

        wb.write( response.getOutputStream() );
        wb.close();

        logger.info( SecurityUtils.getUser().getUsername()
            + " exported the info of group " + id );

        return null;
    }

}
