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
package csns.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import csns.model.qa.AnswerSheet;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.Question;
import csns.model.qa.RatingQuestion;
import csns.model.qa.dao.AnswerSheetDao;
import csns.model.qa.dao.QuestionDao;
import csns.model.survey.Survey;
import csns.model.survey.SurveyResponse;
import csns.model.survey.dao.SurveyDao;
import csns.model.survey.dao.SurveyResponseDao;

@Controller
@SessionAttributes("response")
public class SurveyResponseController {

    @Autowired
    private SurveyDao surveyDao;

    @Autowired
    private SurveyResponseDao surveyResponseDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AnswerSheetDao answerSheetDao;

    @RequestMapping("/department/{dept}/survey/response/view")
    public String view( @RequestParam Long answerSheetId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "response",
            surveyResponseDao.findSurveyResponse( answerSheetId ) );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "survey/response/view";
    }

    @RequestMapping("/department/{dept}/survey/response/list")
    public String list( @RequestParam Long surveyId, ModelMap models )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        List<AnswerSheet> answerSheets = new ArrayList<AnswerSheet>();
        for( SurveyResponse response : survey.getResponses() )
            answerSheets.add( response.getAnswerSheet() );

        models.put( "survey", survey );
        models.put( "answerSheets", answerSheets );
        return "survey/response/list";
    }

    @RequestMapping(value = "/department/{dept}/survey/response/list",
        params = "questionId")
    public String list( @RequestParam Long surveyId,
        @RequestParam Long questionId,
        @RequestParam(required = false) Integer selection,
        @RequestParam(required = false) Integer rating, ModelMap models )
    {
        assert selection != null || rating != null;

        Survey survey = surveyDao.getSurvey( surveyId );
        Question question = questionDao.getQuestion( questionId );
        List<AnswerSheet> answerSheets = selection != null
            ? answerSheetDao.findAnswerSheets( (ChoiceQuestion) question,
                selection ) : answerSheetDao.findAnswerSheets(
                (RatingQuestion) question, rating );

        models.put( "survey", survey );
        models.put( "question", question );
        models.put( "answerSheets", answerSheets );
        return "survey/response/list";
    }

}
