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
package csns.model.survey.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.core.User;
import csns.model.survey.Survey;
import csns.model.survey.SurveyResponse;
import csns.model.survey.dao.SurveyResponseDao;

@Repository
public class SurveyResponseDaoImpl implements SurveyResponseDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SurveyResponse getSurveyResponse( Long id )
    {
        return entityManager.find( SurveyResponse.class, id );
    }

    @Override
    public SurveyResponse getSurveyResponse( Survey survey, User user )
    {
        String query = "from SurveyResponse where survey = :survey "
            + "and answerSheet.author = :user";

        List<SurveyResponse> results = entityManager.createQuery( query,
            SurveyResponse.class )
            .setParameter( "survey", survey )
            .setParameter( "user", user )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    public SurveyResponse findSurveyResponse( Long answerSheetId )
    {
        List<SurveyResponse> results = entityManager.createQuery(
            "from SurveyResponse where answerSheet.id = :answerSheetId",
            SurveyResponse.class )
            .setParameter( "answerSheetId", answerSheetId )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    @Transactional
    public SurveyResponse saveSurveyResponse( SurveyResponse surveyResponse )
    {
        return entityManager.merge( surveyResponse );
    }

}
