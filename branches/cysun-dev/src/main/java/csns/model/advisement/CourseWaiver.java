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
package csns.model.advisement;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import csns.model.academics.Course;

@Entity
@Table(name = "course_waivers")
public class CourseWaiver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST },
        fetch = FetchType.LAZY)
    @JoinColumn(name = "advisement_record_id")
    private AdvisementRecord advisementRecord;

    public CourseWaiver()
    {
    }

    public CourseWaiver( Course course, AdvisementRecord advisementRecord )
    {
        this.course = course;
        this.advisementRecord = advisementRecord;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse( Course course )
    {
        this.course = course;
    }

    public AdvisementRecord getAdvisementRecord()
    {
        return advisementRecord;
    }

    public void setAdvisementRecord( AdvisementRecord advisementRecord )
    {
        this.advisementRecord = advisementRecord;
    }

}
