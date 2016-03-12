/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2015, Mahdiye Jamali (mjamali@calstatela.edu).
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
package csns.model.academics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import csns.model.core.User;

@Entity
@Table(name="groups", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class Group implements Serializable, Comparable<Group>{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	private String name;
	
	@JsonIgnore
	private String description;
	
	@JsonIgnore
	private boolean deleted;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="department_id")
	private Department department;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "group_users",
			joinColumns = @JoinColumn(name="group_id", nullable=false),
			inverseJoinColumns = @JoinColumn(name="user_id", nullable=false))
	private List<User> users;    
	
	public Group() {
		users = new ArrayList<User>();
		deleted = false;
	}
	
	@Override
	public int compareTo(Group group) {
		if(group == null)
			throw new IllegalArgumentException("Cannot compare to Null group");
		return getName().compareTo(group.getName());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
