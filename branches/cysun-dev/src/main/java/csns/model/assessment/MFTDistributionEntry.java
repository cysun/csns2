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

import javax.persistence.Embeddable;

@Embeddable
public class MFTDistributionEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private int value;

    private int percentile;

    public MFTDistributionEntry()
    {
    }

    public MFTDistributionEntry( int value, int percentile )
    {
        this.value = value;
        this.percentile = percentile;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }

    public int getPercentile()
    {
        return percentile;
    }

    public void setPercentile( int percentile )
    {
        this.percentile = percentile;
    }

}
