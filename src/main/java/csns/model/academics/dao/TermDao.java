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

import csns.model.academics.Department;
import csns.model.academics.Term;
import csns.model.core.User;

public interface TermDao {

    /**
     * Returns the terms in which the instructor taught any class.
     */
    List<Term> getTermsByInstructor( User instructor );

    /**
     * Returns the terms in which the student took any class.
     */
    List<Term> getTermsByStudent( User student );

    /**
     * Returns the term in which the user evaluated any rubric as an external
     * evaluator.
     */
    List<Term> getTermsByEvaluator( User evaluator );

    /**
     * Returns the terms in which there were any sections for the department.
     */
    List<Term> getSectionTerms( Department department );

}
