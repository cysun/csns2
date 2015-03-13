/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2015, Chengyu Sun (csun@calstatela.edu).
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
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import csns.model.core.User;
import csns.model.forum.Forum;
import csns.model.mailinglist.Mailinglist;

@Entity
@Table(name = "departments")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    /**
     * <code>name</code> is the name of a department, e.g. Computer Science.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * <code>fullName</code> is the full name of a department, e.g. Computer
     * Science Department or Department of Computer Science.
     */
    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    /**
     * <code>abbreviation</code> is the abbreviated department name, e.g. cs.
     * For programming convenience, abbreviation is always in lower case.
     */
    @Column(nullable = false, unique = true)
    private String abbreviation;

    @JsonIgnore
    @Column(name = "welcome_message")
    private String welcomeMessage;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "department_administrators",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("firstName asc")
    private List<User> administrators;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "department_faculty",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("firstName asc")
    private List<User> faculty;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "department_instructors",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("firstName asc")
    private List<User> instructors;

    /**
     * Rubric evaluators, e.g. the IAB members.
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "department_evaluators",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("firstName asc")
    private List<User> evaluators;

    /**
     * Program reviewers, e.g. abet.
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "department_reviewers",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("firstName asc")
    private List<User> reviewers;

    /**
     * Department courses are courses offered by the department. For example,
     * CS101 is a department course for the CS department.
     */
    @JsonIgnore
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
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "department_additional_undergraduate_courses",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    @OrderBy("code asc")
    private List<Course> additionalUndergraduateCourses;

    @JsonIgnore
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
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "department_additional_graduate_courses",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    @OrderBy("code asc")
    private List<Course> additionalGraduateCourses;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    @OrderBy("name asc")
    private List<Program> programs;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = { CascadeType.PERSIST,
        CascadeType.MERGE })
    @OrderBy("id asc")
    private List<Forum> forums;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = { CascadeType.PERSIST,
        CascadeType.MERGE })
    @OrderBy("id asc")
    private List<Mailinglist> mailinglists;

    @JsonIgnore
    @ElementCollection
    @CollectionTable(name = "department_options",
        joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "option", nullable = false)
    private Set<String> options;

    public Department()
    {
        administrators = new ArrayList<User>();
        faculty = new ArrayList<User>();
        instructors = new ArrayList<User>();
        evaluators = new ArrayList<User>();
        reviewers = new ArrayList<User>();

        undergraduateCourses = new ArrayList<Course>();
        additionalUndergraduateCourses = new ArrayList<Course>();
        graduateCourses = new ArrayList<Course>();
        additionalGraduateCourses = new ArrayList<Course>();

        programs = new ArrayList<Program>();
        forums = new ArrayList<Forum>();
        mailinglists = new ArrayList<Mailinglist>();
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

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName( String fullName )
    {
        this.fullName = fullName;
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

    public List<User> getEvaluators()
    {
        return evaluators;
    }

    public void setEvaluators( List<User> evaluators )
    {
        this.evaluators = evaluators;
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

    public List<Program> getPrograms()
    {
        return programs;
    }

    public void setPrograms( List<Program> programs )
    {
        this.programs = programs;
    }

    public List<Forum> getForums()
    {
        return forums;
    }

    public void setForums( List<Forum> forums )
    {
        this.forums = forums;
    }

    public List<Mailinglist> getMailinglists()
    {
        return mailinglists;
    }

    public void setMailinglists( List<Mailinglist> mailinglists )
    {
        this.mailinglists = mailinglists;
    }

    public Set<String> getOptions()
    {
        return options;
    }

    public void setOptions( Set<String> options )
    {
        this.options = options;
    }

}
