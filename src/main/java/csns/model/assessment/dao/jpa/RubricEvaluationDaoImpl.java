/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.assessment.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.RubricEvaluation.Type;
import csns.model.assessment.Rubricable;
import csns.model.assessment.dao.RubricEvaluationDao;
import csns.model.core.User;

@Repository
public class RubricEvaluationDaoImpl implements RubricEvaluationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public RubricEvaluation getRubricEvaluation( Long id )
    {
        return entityManager.find( RubricEvaluation.class, id );
    }

    @Override
    public List<RubricEvaluation> getRubricEvaluations( Rubricable rubricable,
        User evaluator )
    {
        String query = "from RubricEvaluation where rubricable = :rubricable "
            + "and evaluator = :evaluator and deleted = false "
            + "order by evaluatee.lastName asc";

        return entityManager.createQuery( query, RubricEvaluation.class )
            .setParameter( "rubricable", rubricable )
            .setParameter( "evaluator", evaluator )
            .getResultList();
    }

    @Override
    public List<RubricEvaluation> getRubricEvaluations( Rubricable rubricable,
        Type type )
    {
        String query = "from RubricEvaluation where rubricable = :rubricable "
            + "and type = :type and deleted = false order by rubric.name asc";

        return entityManager.createQuery( query, RubricEvaluation.class )
            .setParameter( "rubricable", rubricable )
            .setParameter( "type", type )
            .getResultList();
    }

    @Override
    @Transactional
    public RubricEvaluation saveRubricEvaluation( RubricEvaluation evaluation )
    {
        return entityManager.merge( evaluation );
    }

}
