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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import csns.model.survey.SurveyResponse;

@Component
public class SurveyResponseValidator implements Validator {

    @Autowired
    AnswerSheetValidator answerSheetValidator;

    @Override
    public boolean supports( Class<?> clazz )
    {
        return SurveyResponse.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        SurveyResponse response = (SurveyResponse) target;
        errors.pushNestedPath( "answerSheet" );
        ValidationUtils.invokeValidator( answerSheetValidator,
            response.getAnswerSheet(), errors );
    }

    public void validate( SurveyResponse response, int sectionIndex,
        Errors errors )
    {
       // errors.pushNestedPath( "answerSheet" );
        answerSheetValidator.validateSection( response.getAnswerSheet(),
            sectionIndex, errors );
    }

}
