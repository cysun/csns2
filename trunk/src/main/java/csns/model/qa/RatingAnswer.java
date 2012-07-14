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
package csns.model.qa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("RATING")
public class RatingAnswer extends Answer {

    private static final long serialVersionUID = 1L;

    private int rating;

    public RatingAnswer()
    {
        rating = -1;
    }

    public RatingAnswer( RatingQuestion ratingQuestion )
    {
        super( ratingQuestion );
        rating = ratingQuestion.getMaxRating() + 1;
    }

    @Override
    public int check()
    {
        return 0;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating( int rating )
    {
        this.rating = rating;
    }

}
