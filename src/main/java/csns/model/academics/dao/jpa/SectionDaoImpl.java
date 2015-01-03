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
package csns.model.academics.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.SectionDao;
import csns.model.assessment.Rubric;
import csns.model.core.User;

@Repository
public class SectionDaoImpl implements SectionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @PostAuthorize("returnObject.isInstructor(principal) or returnObject.isEnrolled(principal) or principal.faculty")
    public Section getSection( Long id )
    {
        return entityManager.find( Section.class, id );
    }

    @Override
    public Section getSection( Quarter quarter, Course course, int number )
    {
        String query = "from Section where quarter = :quarter "
            + "and course = :course and number = :number";

        List<Section> sections = entityManager.createQuery( query,
            Section.class )
            .setParameter( "quarter", quarter )
            .setParameter( "course", course )
            .setParameter( "number", number )
            .getResultList();
        return sections.size() == 0 ? null : sections.get( 0 );
    }

    public List<Section> getUndergraduateSections( Department department,
        Quarter quarter )
    {
        String query = "select s from Section s, "
            + "Department d join d.undergraduateCourses c "
            + "where d = :department and s.quarter = :quarter and s.course = c "
            + "and s.instructors is not empty "
            + "order by c.code asc, s.number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "department", department )
            .setParameter( "quarter", quarter )
            .getResultList();
    }

    public List<Section> getGraduateSections( Department department,
        Quarter quarter )
    {
        String query = "select s from Section s, "
            + "Department d join d.graduateCourses c "
            + "where d = :department and s.quarter = :quarter and s.course = c "
            + "and s.instructors is not empty "
            + "order by c.code asc, s.number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "department", department )
            .setParameter( "quarter", quarter )
            .getResultList();
    }

    @Override
    public List<Section> getSections( Department department, Quarter quarter )
    {
        List<Section> sections = new ArrayList<Section>();
        sections.addAll( getUndergraduateSections( department, quarter ) );
        sections.addAll( getGraduateSections( department, quarter ) );
        return sections;
    }

    @Override
    public List<Section> getSectionsByInstructor( User instructor,
        Quarter quarter )
    {
        String query = "select section from Section section "
            + "join section.instructors instructor "
            + "where instructor = :instructor and section.quarter = :quarter "
            + "order by section.course.code asc, section.number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "instructor", instructor )
            .setParameter( "quarter", quarter )
            .getResultList();
    }

    @Override
    public List<Section> getSectionsByInstructor( User instructor,
        Quarter quarter, Course course )
    {
        String query = "select section from Section section "
            + "join section.instructors instructor "
            + "where instructor = :instructor and section.quarter = :quarter "
            + "and section.course = :course "
            + "order by section.course.code asc, section.number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "instructor", instructor )
            .setParameter( "quarter", quarter )
            .setParameter( "course", course )
            .getResultList();
    }

    @Override
    public List<Section> getSectionsByStudent( User student, Quarter quarter )
    {
        String query = "select section from Section section "
            + "join section.enrollments enrollment "
            + "where enrollment.student = :student and section.quarter = :quarter "
            + "order by section.course.code asc, section.number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "student", student )
            .setParameter( "quarter", quarter )
            .getResultList();
    }

    @Override
    public List<Section> getSectionsByEvaluator( User evaluator, Quarter quarter )
    {
        String query = "select distinct section from Section section "
            + "join section.rubricAssignments assignment "
            + "join assignment.externalEvaluators evaluator "
            + "where evaluator = :evaluator and section.quarter = :quarter "
            + "order by section.id asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "evaluator", evaluator )
            .setParameter( "quarter", quarter )
            .getResultList();
    }

    @Override
    public List<Section> getSectionsByRubric( Rubric rubric )
    {
        String query = "select distinct section from Section section "
            + "join section.rubricAssignments assignment "
            + "where assignment.rubric = :rubric order by section.id asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "rubric", rubric )
            .getResultList();
    }

    @Override
    public List<Section> searchSections( String term, int maxResults )
    {
        TypedQuery<Section> query = entityManager.createNamedQuery(
            "section.search", Section.class );
        if( maxResults > 0 ) query.setMaxResults( maxResults );
        return query.setParameter( "term", term ).getResultList();
    }

    @Override
    @Transactional
    public Section addSection( Quarter quarter, Course course, User instructor )
    {
        String query = "select max(number) from Section "
            + "where quarter = :quarter and course = :course";

        Integer result = entityManager.createQuery( query, Integer.class )
            .setParameter( "quarter", quarter )
            .setParameter( "course", course )
            .getSingleResult();
        Integer currentNum = result == null ? 0 : result;

        Section section = new Section();
        section.setQuarter( quarter );
        section.setCourse( course );
        section.getInstructors().add( instructor );
        section.setNumber( currentNum + 1 );

        return saveSection( section );
    }

    @Override
    @Transactional
    @PreAuthorize("authenticated and (principal.admin or #section.isInstructor(principal))")
    public Section deleteSection( Section section )
    {
        section.getInstructors().clear();
        section.setDeleted( true );
        return entityManager.merge( section );
    }

    @Override
    @Transactional
    @PreAuthorize("authenticated and (principal.admin or #section.isInstructor(principal))")
    public Section saveSection( Section section )
    {
        return entityManager.merge( section );
    }

}
