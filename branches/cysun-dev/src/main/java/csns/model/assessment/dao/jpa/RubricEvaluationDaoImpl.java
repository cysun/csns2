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

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.helper.RubricEvaluationStats;
import csns.model.academics.Course;
import csns.model.academics.Section;
import csns.model.assessment.Rubric;
import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.dao.RubricEvaluationDao;

@Repository
public class RubricEvaluationDaoImpl implements RubricEvaluationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @PostAuthorize("returnObject.evaluator.isSameUser(principal)")
    public RubricEvaluation getRubricEvaluation( Long id )
    {
        return entityManager.find( RubricEvaluation.class, id );
    }

    @Override
    public List<RubricEvaluationStats> getRubricEvaluationStats( Rubric rubric,
        Section section, RubricEvaluation.Type type )
    {
        return entityManager.createNamedQuery(
            "rubric.evaluation.stats.by.section", RubricEvaluationStats.class )
            .setParameter( "rubricId", rubric.getId() )
            .setParameter( "sectionId", section.getId() )
            .setParameter( "type", type.name() )
            .getResultList();
    }

    @Override
    public List<RubricEvaluationStats> getRubricEvaluationStats( Rubric rubric,
        Course course, RubricEvaluation.Type type, int beginYear, int endYear )
    {
        return entityManager.createNamedQuery(
            "rubric.evaluation.stats.by.course", RubricEvaluationStats.class )
            .setParameter( "rubricId", rubric.getId() )
            .setParameter( "courseId", course.getId() )
            .setParameter( "type", type.name() )
            .setParameter( "beginYear", beginYear )
            .setParameter( "endYear", endYear )
            .getResultList();
    }

    @Override
    public List<Integer> getRubricEvaluationYears( Rubric rubric, Course course )
    {
        return entityManager.createNamedQuery( "rubric.evaluation.years",
            Integer.class )
            .setParameter( "rubricId", rubric.getId() )
            .setParameter( "courseId", course.getId() )
            .getResultList();
    }

    @Override
    public List<RubricEvaluationStats> getRubricEvaluationCounts(
        Rubric rubric, Course course, int beginYear, int endYear )
    {
        return entityManager.createNamedQuery( "rubric.evaluation.counts",
            RubricEvaluationStats.class )
            .setParameter( "rubricId", rubric.getId() )
            .setParameter( "courseId", course.getId() )
            .setParameter( "beginYear", beginYear )
            .setParameter( "endYear", endYear )
            .getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("#evaluation.evaluator.isSameUser(principal)")
    public RubricEvaluation saveRubricEvaluation( RubricEvaluation evaluation )
    {
        return entityManager.merge( evaluation );
    }

}
