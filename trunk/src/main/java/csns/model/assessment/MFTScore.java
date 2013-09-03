/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.assessment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import csns.model.academics.Department;
import csns.model.core.User;

@Entity
@Table(name = "mft_scores",
    uniqueConstraints = @UniqueConstraint(columnNames = { "department_id",
        "user_id", "date" }))
public class MFTScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private Date date;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int value;

    @Transient
    private Integer percentile;

    public MFTScore()
    {
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

    public void setDate( Date date )
    {
        this.date = date;
    }

    public Integer getPercentile()
    {
        return percentile;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }

    public Date getDate()
    {
        return date;
    }

    public void setPercentile( Integer percentile )
    {
        this.percentile = percentile;
    }

}
