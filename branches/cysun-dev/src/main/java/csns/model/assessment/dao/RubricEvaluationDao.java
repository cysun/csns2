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
package csns.model.assessment.dao;

import java.util.List;

import csns.helper.RubricEvaluationStats;
import csns.model.academics.Course;
import csns.model.academics.Section;
import csns.model.assessment.Rubric;
import csns.model.assessment.RubricEvaluation;

public interface RubricEvaluationDao {

    RubricEvaluation getRubricEvaluation( Long id );

    List<RubricEvaluationStats> getRubricEvaluationStats( Rubric rubric,
        Section section, RubricEvaluation.Type type );

    List<RubricEvaluationStats> getRubricEvaluationStats( Rubric rubric,
        Course course, RubricEvaluation.Type type, int beginYear, int endYear );

    List<Integer> getRubricEvaluationYears( Rubric rubric, Course course );

    List<RubricEvaluationStats> getRubricEvaluationCounts( Rubric rubric,
        Course course, int beginYear, int endYear );

    RubricEvaluation saveRubricEvaluation( RubricEvaluation evaluation );

}
