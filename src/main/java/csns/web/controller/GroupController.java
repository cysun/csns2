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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Group;
import csns.model.academics.dao.GroupDao;
import csns.security.SecurityUtils;

@Controller
public class GroupController {
	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
	
	@Autowired
	private GroupDao groupDao;
	
	@RequestMapping(value = "/department/{dept}/group/view")
	public String view(@PathVariable String dept, @RequestParam Long id, ModelMap models) {	
		models.put("group", groupDao.getGroup(id));
		models.put("dept", dept);
		return "group/view";
	}
	
	@RequestMapping(value = "/department/{dept}/group/delete")
	public String delete(@PathVariable String dept, @RequestParam Long id) {
		
		Group group = groupDao.getGroup(id);
		group.setDeleted(true);
		group = groupDao.saveGroup(group);
		
		logger.info(SecurityUtils.getUser().getUsername() + " deleted group " + group.getId());
		
		return "redirect:/department/" + dept + "/people#group";
	}
	
	@RequestMapping(value = "/department/{dept}/group/check", method = RequestMethod.GET)
	protected void doGet(@PathVariable String dept, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");
		Group group = groupDao.getGroup(name);
		
		PrintWriter out = response.getWriter();
		if(group == null){
			out.write(toJson(0));
		}else{
			out.write(toJson(1));
		}
		out.flush();
		out.close();

	}

	private String toJson(int count) {
		StringBuilder sb = new StringBuilder();

		sb.append("{ \"count\" : \"" + count + "\"");
		sb.append("}");

		return sb.toString();
	}
}
