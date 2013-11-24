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
package csns.model.news;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import csns.model.academics.Department;
import csns.model.core.File;
import csns.model.forum.Post;
import csns.model.forum.Topic;

@Entity
@Table(name = "news")
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @JsonIgnore
    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "expire_date")
    private Calendar expireDate;

    public News()
    {
        super();

        expireDate = Calendar.getInstance();
        expireDate.add( Calendar.DATE, 7 );
        expireDate.set( Calendar.HOUR_OF_DAY, 23 );
        expireDate.set( Calendar.MINUTE, 59 );
        expireDate.set( Calendar.SECOND, 59 );
        expireDate.set( Calendar.MILLISECOND, 59 );

        topic = new Topic();
        Post post = new Post();
        post.setTopic( topic );
        topic.addPost( post );
    }

    // for web service
    public String getTitle()
    {
        return topic.getFirstPost().getSubject();
    }

    // for web service
    public String getContent()
    {
        return topic.getFirstPost().getContent();
    }

    // for web service
    public String getAuthor()
    {
        return topic.getAuthor().getName();
    }

    // for web service
    public Date getPublishDate()
    {
        return topic.getFirstPost().getDate();
    }

    // for web service
    public List<File> getAttachments()
    {
        return topic.getFirstPost().getAttachments();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public Topic getTopic()
    {
        return topic;
    }

    public void setTopic( Topic topic )
    {
        this.topic = topic;
    }

    public Calendar getExpireDate()
    {
        return expireDate;
    }

    public void setExpireDate( Calendar expireDate )
    {
        this.expireDate = expireDate;
    }

}
