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
package csns.model.qa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

@Entity
@DiscriminatorValue("CHOICE")
public class ChoiceAnswer extends Answer {

    private static final long serialVersionUID = 1L;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "answer_selections",
        joinColumns = @JoinColumn(name = "answer_id"))
    @Column(name = "selection")
    private Set<Integer> selections;

    public ChoiceAnswer()
    {
        selections = new HashSet<Integer>();
    }

    public ChoiceAnswer( ChoiceQuestion choiceQuestion )
    {
        super( choiceQuestion );
        selections = new HashSet<Integer>();
    }

    @Override
    public int check()
    {
        int points = question.getPointValue();

        Set<Integer> correctSelections = ((ChoiceQuestion) question).getCorrectSelections();

        if( correctSelections.size() == 0 ) return 0;
        if( correctSelections.size() != selections.size() ) return -points;
        for( Integer selection : selections )
            if( !correctSelections.contains( selection ) ) return -points;

        return points;
    }

    @Override
    public String toString()
    {
        List<Integer> list = new ArrayList<Integer>();
        list.addAll( selections );
        Collections.sort( list );

        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < list.size(); ++i )
        {
            sb.append( list.get( i ) );
            if( i < list.size() - 1 ) sb.append( "," );
        }

        return sb.toString();
    }

    public Set<Integer> getSelections()
    {
        return selections;
    }

    public void setSelections( Set<Integer> selections )
    {
        this.selections = selections;
    }

}
