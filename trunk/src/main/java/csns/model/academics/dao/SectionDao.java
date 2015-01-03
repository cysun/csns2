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
package csns.model.academics.dao;

import java.util.List;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.assessment.Rubric;
import csns.model.core.User;

public interface SectionDao {

    Section getSection( Long id );

    Section getSection( Quarter quarter, Course course, int number );

    List<Section> getSections( Department department, Quarter quarter );

    List<Section> getSectionsByInstructor( User instructor, Quarter quarter );

    List<Section> getSectionsByInstructor( User instructor, Quarter quarter,
        Course course );

    List<Section> getSectionsByStudent( User student, Quarter quarter );

    List<Section> getSectionsByEvaluator( User evaluator, Quarter quarter );

    List<Section> getSectionsByRubric( Rubric rubric );

    List<Section> searchSections( String term, int maxResults );

    Section addSection( Quarter quarter, Course course, User instructor );

    Section deleteSection( Section section );

    Section saveSection( Section section );

}
