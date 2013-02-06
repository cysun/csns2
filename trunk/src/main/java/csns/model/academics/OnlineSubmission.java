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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import csns.model.core.User;
import csns.model.qa.Answer;
import csns.model.qa.AnswerSheet;

@Entity
@DiscriminatorValue("ONLINE")
public class OnlineSubmission extends Submission {

    private static final long serialVersionUID = 1L;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST },
        fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_sheet_id", unique = true)
    private AnswerSheet answerSheet;

    @Column(nullable = false)
    private boolean saved;

    @Column(nullable = false)
    private boolean finished;

    public OnlineSubmission()
    {
        super();

        saved = false;
        finished = false;
    }

    public OnlineSubmission( User student, OnlineAssignment assignment )
    {
        super( student, assignment );

        if( assignment.isPublished() ) createAnswerSheet();
    }

    @Override
    public boolean isOnline()
    {
        return true;
    }

    public void createAnswerSheet()
    {
        answerSheet = new AnswerSheet(
            ((OnlineAssignment) assignment).getQuestionSheet() );
        answerSheet.setAuthor( student );
    }

    public void grade()
    {
        if( !assignment.isPastDue() ) return;

        if( answerSheet == null ) createAnswerSheet();

        int total = 0;
        StringBuffer sb = new StringBuffer();
        boolean showSection = answerSheet.getSections().size() > 1;

        for( int i = 0; i < answerSheet.getSections().size(); ++i )
        {
            if( showSection )
                sb.append( "Section " ).append( i + 1 ).append( "\n" );

            List<Answer> answers = answerSheet.getSections()
                .get( i )
                .getAnswers();
            for( int j = 0; j < answers.size(); ++j )
            {
                int points = answers.get( j ).check();
                if( points > 0 )
                    total += points;
                else if( points < 0 )
                    sb.append( j + 1 ).append( ". " + points + "\n" );
                else
                    sb.append( j + 1 ).append( ". ?\n" );
            }

            sb.append( "\n" );
        }

        comments = sb.toString();
        if( !("" + total).equals( grade ) )
        {
            grade = "" + total;
            gradeMailed = false;
        }
    }

    public AnswerSheet getAnswerSheet()
    {
        return answerSheet;
    }

    public void setAnswerSheet( AnswerSheet answerSheet )
    {
        this.answerSheet = answerSheet;
    }

    public boolean isSaved()
    {
        return saved;
    }

    public void setSaved( boolean saved )
    {
        this.saved = saved;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public void setFinished( boolean finished )
    {
        this.finished = finished;
    }

}
