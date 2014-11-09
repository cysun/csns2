/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.forum;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.core.Subscribable;
import csns.model.core.User;

/**
 * There are three types of forums: department forums, course forums, and
 * general forums.
 */
@Entity
@Table(name = "forums")
public class Forum implements Subscribable, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private Date date;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "forum_moderators",
        joinColumns = @JoinColumn(name = "forum_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> moderators;

    @ManyToMany
    @JoinTable(name = "forum_members",
        joinColumns = @JoinColumn(name = "forum_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members;

    @Column(name = "num_of_topics", nullable = false)
    private int numOfTopics;

    @Column(name = "num_of_posts", nullable = false)
    private int numOfPosts;

    @OneToOne
    @JoinColumn(name = "last_post_id", unique = true)
    private Post lastPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", unique = true)
    private Course course;

    @OneToMany(mappedBy = "forum")
    @OrderBy("pinned desc, lastPostDate desc")
    @Where(clause = "deleted='f'")
    private List<Topic> topics;

    @Column(name = "members_only", nullable = false)
    private boolean membersOnly;

    /**
     * Hidden forums are not shown in forum listings, e.g. Wiki Discussion.
     */
    @Column(nullable = false)
    private boolean hidden;

    public Forum()
    {
        numOfTopics = 0;
        numOfPosts = 0;
        moderators = new HashSet<User>();
        members = new HashSet<User>();
        hidden = false;
        membersOnly = false;
    }

    public Forum( String name )
    {
        this();
        this.name = name;
    }

    public String getShortName()
    {
        return course != null ? course.getCode() : name;
    }

    @Override
    public String getType()
    {
        return "Forum";
    }

    public boolean isAdmin( User user )
    {
        return user != null && department != null
            && user.isAdmin( department.getAbbreviation() );
    }

    public boolean isCoordinator( User user )
    {
        return user != null && course != null
            && course.getCoordinator() != null
            && course.getCoordinator().getId().equals( user.getId() );
    }

    public boolean isModerator( User user )
    {
        if( user == null ) return false;

        boolean result = false;
        for( User moderator : moderators )
            if( moderator.getId().equals( user.getId() ) )
            {
                result = true;
                break;
            }

        return result || isAdmin( user ) || isCoordinator( user );
    }

    public boolean isMember( User user )
    {
        if( user == null ) return false;

        boolean result = false;
        for( User member : members )
            if( member.getId().equals( user.getId() ) )
            {
                result = true;
                break;
            }

        return result || isModerator( user );
    }

    public void removeMember( User user )
    {
        if( user != null )
        {
            for( User member : members )
                if( member.getId().equals( user.getId() ) )
                {
                    members.remove( member );
                    return;
                }
        }
    }

    public void incrementNumOfTopics()
    {
        ++numOfTopics;
    }

    public void incrementNumOfPosts()
    {
        ++numOfPosts;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public Set<User> getModerators()
    {
        return moderators;
    }

    public void setModerators( Set<User> moderators )
    {
        this.moderators = moderators;
    }

    public Set<User> getMembers()
    {
        return members;
    }

    public void setMembers( Set<User> members )
    {
        this.members = members;
    }

    public int getNumOfTopics()
    {
        return numOfTopics;
    }

    public void setNumOfTopics( int numOfTopics )
    {
        this.numOfTopics = numOfTopics;
    }

    public int getNumOfPosts()
    {
        return numOfPosts;
    }

    public void setNumOfPosts( int numOfPosts )
    {
        this.numOfPosts = numOfPosts;
    }

    public Post getLastPost()
    {
        return lastPost;
    }

    public void setLastPost( Post lastPost )
    {
        this.lastPost = lastPost;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse( Course course )
    {
        this.course = course;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden( boolean hidden )
    {
        this.hidden = hidden;
    }

    public List<Topic> getTopics()
    {
        return topics;
    }

    public void setTopics( List<Topic> topics )
    {
        this.topics = topics;
    }

    public boolean isMembersOnly()
    {
        return membersOnly;
    }

    public void setMembersOnly( boolean membersOnly )
    {
        this.membersOnly = membersOnly;
    }

}
