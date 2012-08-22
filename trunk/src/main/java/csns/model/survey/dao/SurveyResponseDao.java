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
package csns.model.survey.dao;

import java.util.List;

import csns.model.core.User;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.RatingQuestion;
import csns.model.survey.Survey;
import csns.model.survey.SurveyResponse;

public interface SurveyResponseDao {

    SurveyResponse getSurveyResponse( Long id );

    SurveyResponse getSurveyResponse( Survey survey, User user );

    List<SurveyResponse> getSurveyResponses( ChoiceQuestion choiceQuestion,
        Integer selection );

    List<SurveyResponse> getSurveyResponses( RatingQuestion ratingQuestion,
        Integer rating );

    SurveyResponse saveSurveyResponse( SurveyResponse surveyResponse );

}
