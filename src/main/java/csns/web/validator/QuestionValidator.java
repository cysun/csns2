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
package csns.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import csns.model.academics.Assignment;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.RatingQuestion;
import csns.model.qa.TextQuestion;

@Component
public class QuestionValidator implements Validator {

    @Override
    public boolean supports( Class<?> clazz )
    {
        return Assignment.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "description",
            "error.field.required" );

        if( target instanceof ChoiceQuestion )
        {
            ChoiceQuestion question = (ChoiceQuestion) target;

            int minSelections = question.getMinSelections();
            int maxSelections = question.getMaxSelections();
            int numOfChoices = question.getNumOfChoices();

            if( maxSelections <= 0 ) maxSelections = 1;
            if( maxSelections > numOfChoices ) maxSelections = numOfChoices;
            if( minSelections < 0 ) minSelections = 0;
            if( minSelections > maxSelections ) minSelections = maxSelections;

            question.setMinSelections( minSelections );
            question.setMaxSelections( maxSelections );
        }

        if( target instanceof RatingQuestion )
        {
            RatingQuestion question = (RatingQuestion) target;
            if( question.getMinRating() > question.getMaxRating() )
                question.setMinRating( question.getMaxRating() );
        }

        if( target instanceof TextQuestion )
        {
            TextQuestion question = (TextQuestion) target;
            if( question.getTextLength() < 1 ) question.setTextLength( 1 );
        }
    }

}
