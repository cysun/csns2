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
package csns.model.qa.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import csns.model.qa.AnswerSheet;
import csns.model.qa.ChoiceQuestion;
import csns.model.qa.RatingQuestion;
import csns.model.qa.dao.AnswerSheetDao;

@Repository
public class AnswerSheetDaoImpl implements AnswerSheetDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnswerSheet> findAnswerSheets( ChoiceQuestion choiceQuestion,
        Integer selection )
    {
        String query = "select answerSheet from AnswerSheet answerSheet "
            + "join answerSheet.sections section "
            + "join section.answers answer "
            + "join answer.selections selection "
            + "where answer.question = :question and selection = :selection "
            + "order by answerSheet.date asc";

        return entityManager.createQuery( query, AnswerSheet.class )
            .setParameter( "question", choiceQuestion )
            .setParameter( "selection", selection )
            .getResultList();
    }

    @Override
    public List<AnswerSheet> findAnswerSheets( RatingQuestion ratingQuestion,
        Integer rating )
    {
        String query = "select answerSheet from AnswerSheet answerSheet "
            + "join answerSheet.sections section "
            + "join section.answers answer "
            + "where answer.question = :question and answer.rating = :rating "
            + "order by answerSheet.date asc";

        return entityManager.createQuery( query, AnswerSheet.class )
            .setParameter( "question", ratingQuestion )
            .setParameter( "rating", rating )
            .getResultList();
    }

}
