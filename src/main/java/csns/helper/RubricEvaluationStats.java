/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014-2015, Chengyu Sun (csun@calstatela.edu).
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.RubricSubmission;

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

    private Double min, max, mean, median;

    public RubricEvaluationStats()
    {
        count = 0;
    }

    public RubricEvaluationStats( RubricEvaluation.Type type )
    {
        this();
        this.type = type.toString();
    }

    public static List<RubricEvaluationStats> calcStats(
        RubricSubmission submission, RubricEvaluation.Type type )
    {
        int numOfIndicators = submission.getAssignment()
            .getRubric()
            .getIndicators()
            .size();
        // Stats for each indicator plus overall
        List<RubricEvaluationStats> allStats = new ArrayList<RubricEvaluationStats>();
        for( int i = 0; i < numOfIndicators + 1; ++i )
            allStats.add( new RubricEvaluationStats( type ) );

        List<RubricEvaluation> evaluations = new ArrayList<RubricEvaluation>();
        for( RubricEvaluation evaluation : submission.getEvaluations() )
            if( evaluation.getType() == type && evaluation.isCompleted() )
                evaluations.add( evaluation );
        if( evaluations.size() == 0 ) return allStats;

        Double allRatings[][] = new Double[numOfIndicators + 1][evaluations
            .size()];
        for( int i = 0; i < evaluations.size(); ++i )
        {
            List<Integer> ratings = evaluations.get( i ).getRatings();
            for( int j = 1; j < ratings.size() + 1; ++j )
                allRatings[j][i] = ratings.get( j - 1 ).doubleValue();
            allRatings[0][i] = evaluations.get( i ).getOverallRating();
        }

        for( int i = 0; i < allRatings.length; ++i )
        {
            Arrays.sort( allRatings[i] );
            RubricEvaluationStats stats = allStats.get( i );
            int n = evaluations.size();
            stats.setCount( n );
            stats.setMin( allRatings[i][0] );
            stats.setMax( allRatings[i][n - 1] );
            if( n % 2 == 0 )
                stats.setMedian(
                    (allRatings[i][n / 2 - 1] + allRatings[i][n / 2]) / 2 );
            else
                stats.setMedian( allRatings[i][n / 2] );
            double sum = 0;
            for( double rating : allRatings[i] )
                sum += rating;
            stats.setMean( sum / n );
        }

        return allStats;
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

    public Double getMin()
    {
        return min;
    }

    public void setMin( Double min )
    {
        this.min = min;
    }

    public Double getMax()
    {
        return max;
    }

    public void setMax( Double max )
    {
        this.max = max;
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

}
