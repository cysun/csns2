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
package csns.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import csns.model.qa.Answer;
import csns.model.qa.AnswerSection;
import csns.model.qa.AnswerSheet;
import csns.model.qa.ChoiceAnswer;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.RatingAnswer;
import csns.model.qa.RatingQuestion;
import csns.model.qa.TextAnswer;
import csns.util.MyAntiSamy;

@Component
public class AnswerSheetValidator implements Validator {

    @Autowired
    private MyAntiSamy antiSamy;

    @Override
    public boolean supports( Class<?> clazz )
    {
        return AnswerSheet.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        AnswerSheet answerSheet = (AnswerSheet) target;
        for( int i = 0; i < answerSheet.getSections().size(); ++i )
            validateSection( answerSheet, i, errors );
    }

    public void validateSection( AnswerSheet answerSheet, int sectionIndex,
        Errors errors )
    {
        AnswerSection section = answerSheet.getSections().get( sectionIndex );

        for( int i = 0; i < section.getAnswers().size(); ++i )
        {
            Answer answer = section.getAnswers().get( i );
            String path = "answerSheet.sections[" + sectionIndex + "].answers["
                + i + "]";
            if( answer.getQuestion().getType().equals( "CHOICE" ) )
                validateChoiceAnswer( (ChoiceAnswer) answer, path, errors );

            if( answer.getQuestion().getType().equals( "RATING" ) )
                validateRatingAnswer( (RatingAnswer) answer, path, errors );

            if( answer.getQuestion().getType().equals( "TEXT" ) )
                validateTextAnswer( (TextAnswer) answer, path, errors );
        }
    }

    public void validateChoiceAnswer( ChoiceAnswer answer, String path,
        Errors errors )
    {
        ChoiceQuestion question = (ChoiceQuestion) answer.getQuestion();

        int minSelections = question.getMinSelections();
        int maxSelections = question.getMaxSelections();
        int numOfSelections = answer.getSelections() == null ? 0
            : answer.getSelections().size();

        Integer errorArgs[] = { minSelections, maxSelections };
        if( numOfSelections < minSelections )
            errors.rejectValue( path, "error.qa.choice.underselect", errorArgs,
                "Please select at least " + minSelections + " choice(s)." );
        if( numOfSelections > maxSelections )
            errors.rejectValue( path, "error.qa.choice.overselect", errorArgs,
                "Please select at most " + maxSelections + " choice(s)." );
    }

    public void validateRatingAnswer( RatingAnswer answer, String path,
        Errors errors )
    {
        Integer rating = answer.getRating();

        if( rating != null )
        {
            RatingQuestion question = (RatingQuestion) answer.getQuestion();
            if( rating < question.getMinRating()
                || rating > question.getMaxRating() )
                errors.rejectValue( path, "error.qa.rating.invalid" );
        }
        else
            errors.rejectValue( path, "error.qa.rating.empty" );
    }

    public void validateTextAnswer( TextAnswer answer, String path,
        Errors errors )
    {
        String text = answer.getText();
        if( StringUtils.hasText( text ) && !antiSamy.validate( text ) )
            errors.rejectValue( path, "error.html.invalid" );
    }

}
