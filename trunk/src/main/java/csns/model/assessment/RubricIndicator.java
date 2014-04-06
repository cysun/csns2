/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "rubric_indicators",
    uniqueConstraints = @UniqueConstraint(columnNames = { "rubric_id",
        "indicator_order" }))
public class RubricIndicator {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rubric_id")
    private Rubric rubric;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "rubric_indicator_criteria",
        joinColumns = @JoinColumn(name = "indicator_id"))
    @Column(name = "criterion")
    @OrderColumn(name = "criterion_order")
    private List<String> criteria;

    public RubricIndicator()
    {
    }

    public RubricIndicator( Rubric rubric )
    {
        this.rubric = rubric;
        this.criteria = new ArrayList<String>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Rubric getRubric()
    {
        return rubric;
    }

    public void setRubric( Rubric rubric )
    {
        this.rubric = rubric;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public List<String> getCriteria()
    {
        return criteria;
    }

    public void setCriteria( List<String> criteria )
    {
        this.criteria = criteria;
    }

}
