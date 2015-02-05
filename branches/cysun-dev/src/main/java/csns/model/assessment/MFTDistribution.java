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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "mft_distributions",
    uniqueConstraints = @UniqueConstraint(columnNames = { "year", "type_id" }))
public class MFTDistribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private int year;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private MFTDistributionType type;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "num_of_samples")
    private Integer numOfSamples;

    private Double mean;

    private Double median;

    private Double stdev;

    @ElementCollection
    @CollectionTable(name = "mft_distribution_entries",
        joinColumns = @JoinColumn(name = "distribution_id"))
    @OrderBy("value desc")
    private List<MFTDistributionEntry> entries;

    private boolean deleted;

    public MFTDistribution()
    {
        year = Calendar.getInstance().get( Calendar.YEAR );
        entries = new ArrayList<MFTDistributionEntry>();
        deleted = false;
    }

    public Integer getPercentile( double value )
    {
        for( MFTDistributionEntry entry : entries )
            if( value >= entry.getValue() ) return entry.getPercentile();

        return null;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear( int year )
    {
        this.year = year;
    }

    public MFTDistributionType getType()
    {
        return type;
    }

    public void setType( MFTDistributionType type )
    {
        this.type = type;
    }

    public Date getFromDate()
    {
        return fromDate;
    }

    public void setFromDate( Date fromDate )
    {
        this.fromDate = fromDate;
    }

    public Date getToDate()
    {
        return toDate;
    }

    public void setToDate( Date toDate )
    {
        this.toDate = toDate;
    }

    public Integer getNumOfSamples()
    {
        return numOfSamples;
    }

    public void setNumOfSamples( Integer numOfSamples )
    {
        this.numOfSamples = numOfSamples;
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

    public Double getStdev()
    {
        return stdev;
    }

    public void setStdev( Double stdev )
    {
        this.stdev = stdev;
    }

    public List<MFTDistributionEntry> getEntries()
    {
        return entries;
    }

    public void setEntries( List<MFTDistributionEntry> entries )
    {
        this.entries = entries;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted( boolean deleted )
    {
        this.deleted = deleted;
    }

}
