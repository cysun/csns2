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
package csns.helper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import csns.model.assessment.RubricEvaluation;

/**
 * RubricEvaluationStats holds the result fields of aggregation queries of
 * rubric evaluations. It is an entity class because JPA native SQL query cannot
 * return non-entity classes. Note that not all fields of RubricEvaluationStats
 * are used in all queries.
 */
@Entity
public class RubricEvaluationStats {

    @Id
    private Long id;

    private Integer year;

    private String type;

    @Column(name = "indicator")
    private int indicatorIndex;

    private Integer count;

    private Double mean, median;

    private Integer min, max;

    public RubricEvaluationStats()
    {
    }

    public RubricEvaluation.Type getEvalType()
    {
        return RubricEvaluation.Type.valueOf( type );
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Integer getYear()
    {
        return year;
    }

    public void setYear( Integer year )
    {
        this.year = year;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public int getIndicatorIndex()
    {
        return indicatorIndex;
    }

    public void setIndicatorIndex( int indicatorIndex )
    {
        this.indicatorIndex = indicatorIndex;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount( Integer count )
    {
        this.count = count;
    }

    public Double getMean()
    {
        return mean;
    }

    public void setMean( Double mean )
    {
        this.mean = mean;
    }

    public Double getMedian()
    {
        return median;
    }

    public void setMedian( Double median )
    {
        this.median = median;
    }

    public Integer getMin()
    {
        return min;
    }

    public void setMin( Integer min )
    {
        this.min = min;
    }

    public Integer getMax()
    {
        return max;
    }

    public void setMax( Integer max )
    {
        this.max = max;
    }

}
