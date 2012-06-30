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
package csns.model.academics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import csns.model.core.User;

@Entity
@Table(name = "departments")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    /**
     * <code>name</code> is the full name of a department, e.g. Computer
     * Science.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * <code>abbreviation</code> is the abbreviated department name, e.g. cs.
     * For programming convenience, abbreviation is always in lower case.
     */
    @Column(nullable = false, unique = true)
    private String abbreviation;

    @Column(name = "welcome_message")
    private String welcomeMessage;

    @ManyToMany
    @JoinTable(name = "department_administrators",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("lastName asc")
    private List<User> administrators;

    @ManyToMany
    @JoinTable(name = "department_faculty",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("lastName asc")
    private List<User> faculty;

    @ManyToMany
    @JoinTable(name = "department_instructors",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("lastName asc")
    private List<User> instructors;

    @ManyToMany
    @JoinTable(name = "department_reviewers",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("lastName asc")
    private List<User> reviewers;

    /**
     * Department courses are courses offered by the department. For example,
     * CS101 is a department course for the CS department.
     */
    @ManyToMany
    @JoinTable(name = "department_undergraduate_courses",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    @OrderBy("code asc")
    private List<Course> undergraduateCourses;

    /**
     * Additional courses are the courses offered by other departments which the
     * students have to take to meet major requirements, particularly the ones
     * that are related to program assessment, e.g. EE444 and TECH250 for
     * Computer Science.
     */
    @ManyToMany
    @JoinTable(name = "department_additional_undergraduate_courses",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    @OrderBy("code asc")
    private List<Course> additionalUndergraduateCourses;

    @ManyToMany
    @JoinTable(name = "department_graduate_courses",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    @OrderBy("code asc")
    private List<Course> graduateCourses;

    /**
     * Additional courses are the courses offered by other departments which the
     * students have to take to meet major requirements, particularly the ones
     * that are related to program assessment, e.g. EE444 and TECH250 for
     * Computer Science.
     */
    @ManyToMany
    @JoinTable(name = "department_additional_graduate_courses",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    @OrderBy("code asc")
    private List<Course> additionalGraduateCourses;

    public Department()
    {
        administrators = new ArrayList<User>();
        faculty = new ArrayList<User>();
        instructors = new ArrayList<User>();
        reviewers = new ArrayList<User>();

        undergraduateCourses = new ArrayList<Course>();
        additionalUndergraduateCourses = new ArrayList<Course>();
        graduateCourses = new ArrayList<Course>();
        additionalGraduateCourses = new ArrayList<Course>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public void setAbbreviation( String abbreviation )
    {
        this.abbreviation = abbreviation;
    }

    public String getWelcomeMessage()
    {
        return welcomeMessage;
    }

    public void setWelcomeMessage( String welcomeMessage )
    {
        this.welcomeMessage = welcomeMessage;
    }

    public List<User> getAdministrators()
    {
        return administrators;
    }

    public void setAdministrators( List<User> administrators )
    {
        this.administrators = administrators;
    }

    public List<User> getFaculty()
    {
        return faculty;
    }

    public void setFaculty( List<User> faculty )
    {
        this.faculty = faculty;
    }

    public List<User> getInstructors()
    {
        return instructors;
    }

    public void setInstructors( List<User> instructors )
    {
        this.instructors = instructors;
    }

    public List<User> getReviewers()
    {
        return reviewers;
    }

    public void setReviewers( List<User> reviewers )
    {
        this.reviewers = reviewers;
    }

    public List<Course> getUndergraduateCourses()
    {
        return undergraduateCourses;
    }

    public void setUndergraduateCourses( List<Course> undergraduateCourses )
    {
        this.undergraduateCourses = undergraduateCourses;
    }

    public List<Course> getAdditionalUndergraduateCourses()
    {
        return additionalUndergraduateCourses;
    }

    public void setAdditionalUndergraduateCourses(
        List<Course> additionalUndergraduateCourses )
    {
        this.additionalUndergraduateCourses = additionalUndergraduateCourses;
    }

    public List<Course> getGraduateCourses()
    {
        return graduateCourses;
    }

    public void setGraduateCourses( List<Course> graduateCourses )
    {
        this.graduateCourses = graduateCourses;
    }

    public List<Course> getAdditionalGraduateCourses()
    {
        return additionalGraduateCourses;
    }

    public void setAdditionalGraduateCourses(
        List<Course> additionalGraduateCourses )
    {
        this.additionalGraduateCourses = additionalGraduateCourses;
    }

}
