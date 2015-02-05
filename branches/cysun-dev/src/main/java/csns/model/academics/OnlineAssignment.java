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
package csns.model.academics;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import csns.model.qa.QuestionSheet;

@Entity
@DiscriminatorValue("ONLINE")
public class OnlineAssignment extends Assignment {

    private static final long serialVersionUID = 1L;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST },
        fetch = FetchType.LAZY)
    @JoinColumn(name = "question_sheet_id", unique = true)
    private QuestionSheet questionSheet;

    public OnlineAssignment()
    {
        super();

        publishDate = null;
        availableAfterDueDate = false;
        questionSheet = new QuestionSheet();
    }

    @Override
    public OnlineAssignment clone()
    {
        OnlineAssignment assignment = new OnlineAssignment();

        assignment.name = name;
        assignment.alias = alias;
        assignment.totalPoints = totalPoints;
        assignment.dueDate = null;
        assignment.availableAfterDueDate = availableAfterDueDate;
        assignment.questionSheet = questionSheet.clone();

        return assignment;
    }

    @Override
    public boolean isOnline()
    {
        return true;
    }

    public void calcTotalPoints()
    {
        totalPoints = "" + questionSheet.getTotalPoints();
    }

    public QuestionSheet getQuestionSheet()
    {
        return questionSheet;
    }

    public void setQuestionSheet( QuestionSheet questionSheet )
    {
        this.questionSheet = questionSheet;
    }

}
