/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Mahdiye Jamali (mjamali@calstatela.edu).
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
package csns.model.academics.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Department;
import csns.model.academics.Group;
import csns.model.academics.dao.GroupDao;

@Repository
public class GroupDaoImpl implements GroupDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Group getGroup(Long id) {
		return entityManager.find(Group.class, id);
	}

	@Override
	public Group getGroup(String name) {
		String query = "from Group where name = :name";
		List<Group> groups = entityManager.createQuery(query, Group.class).setParameter("name", name).getResultList();
		return groups.size() == 0 ? null : groups.get(0);
	}
	
	@Override
	public List<Group> getGroups(Department department) {
		String query = "from Group where department = :department "
				+ "and deleted = :deleted order by id desc";
		return entityManager.createQuery(query, Group.class)
				.setParameter("department", department)
				.setParameter("deleted", false)
				.getResultList();
	}


	@Override
	@Transactional
	public Group saveGroup(Group group) {
		return entityManager.merge(group);
	}
}
