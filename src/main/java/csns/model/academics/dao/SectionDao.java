/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014,2017, Chengyu Sun (csun@calstatela.edu).
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
import csns.model.academics.Term;
import csns.model.academics.Section;
import csns.model.assessment.Rubric;
import csns.model.core.User;

public interface SectionDao {

    Section getSection( Long id );

    Section getSection( Term term, Course course, int number );

    /**
     * A special section is a section without an instructor. We use a special
     * section to hold the bulk-imported grades that don't belong to an actual
     * CSNS section.
     */
    Section getSpecialSection( Term term, Course course );

    List<Section> getSections( Department department, Term term );

    List<Section> getSections( Course course, Integer beginYear,
        Integer endYear );

    List<Section> getSectionsByInstructor( User instructor, Term term );

    List<Section> getSectionsByInstructor( User instructor, Term term,
        Course course );

    List<Section> getSectionsByStudent( User student, Term term );

    List<Section> getSectionsByEvaluator( User evaluator, Term term );

    List<Section> getSectionsByRubric( Rubric rubric );

    List<Section> searchSections( String text, int maxResults );

    Section addSection( Term term, Course course, User instructor );

    Section deleteSection( Section section );

    Section saveSection( Section section );

}
