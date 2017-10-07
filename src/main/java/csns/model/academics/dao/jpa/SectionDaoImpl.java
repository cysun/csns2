/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2017, Chengyu Sun (csun@calstatela.edu).
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
import csns.model.academics.Term;
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
    public Section getSection( Term term, Course course, int number )
    {
        String query = "from Section where term = :term "
            + "and course = :course and number = :number";

        List<Section> sections = entityManager
            .createQuery( query, Section.class )
            .setParameter( "term", term )
            .setParameter( "course", course )
            .setParameter( "number", number )
            .getResultList();
        return sections.size() == 0 ? null : sections.get( 0 );
    }

    @Override
    public Section getSpecialSection( Term term, Course course )
    {
        String query = "from Section s where term = :term "
            + "and course = :course and s.instructors is empty order by id asc";

        List<Section> sections = entityManager
            .createQuery( query, Section.class )
            .setParameter( "term", term )
            .setParameter( "course", course )
            .getResultList();
        return sections.size() == 0 ? null : sections.get( 0 );
    }

    @Override
    public List<Section> getSections( Department department, Term term )
    {
        String query = "from Section s where course.department = :department "
            + "and term = :term and s.instructors is not empty "
            + "order by course.code asc, number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "department", department )
            .setParameter( "term", term )
            .getResultList();
    }

    @Override
    public List<Section> getSections( Course course, Integer beginYear,
        Integer endYear )
    {
        return entityManager
            .createNamedQuery( "sections.by.year", Section.class )
            .setParameter( "courseId", course.getId() )
            .setParameter( "beginYear", beginYear )
            .setParameter( "endYear", endYear )
            .getResultList();
    }

    @Override
    public List<Section> getSectionsByInstructor( User instructor, Term term )
    {
        String query = "select section from Section section "
            + "join section.instructors instructor "
            + "where instructor = :instructor and section.term = :term "
            + "order by section.course.code asc, section.number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "instructor", instructor )
            .setParameter( "term", term )
            .getResultList();
    }

    @Override
    public List<Section> getSectionsByInstructor( User instructor, Term term,
        Course course )
    {
        String query = "select section from Section section "
            + "join section.instructors instructor "
            + "where instructor = :instructor and section.term = :term "
            + "and section.course = :course "
            + "order by section.course.code asc, section.number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "instructor", instructor )
            .setParameter( "term", term )
            .setParameter( "course", course )
            .getResultList();
    }

    @Override
    public List<Section> getSectionsByStudent( User student, Term term )
    {
        String query = "select section from Section section "
            + "join section.enrollments enrollment "
            + "where enrollment.student = :student and section.term = :term "
            + "order by section.course.code asc, section.number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "student", student )
            .setParameter( "term", term )
            .getResultList();
    }

    @Override
    public List<Section> getSectionsByEvaluator( User evaluator, Term term )
    {
        String query = "select distinct section from Section section "
            + "join section.rubricAssignments assignment "
            + "join assignment.externalEvaluators evaluator "
            + "where evaluator = :evaluator and section.term = :term "
            + "order by section.id asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "evaluator", evaluator )
            .setParameter( "term", term )
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
    public List<Section> searchSections( String text, int maxResults )
    {
        TypedQuery<Section> query = entityManager
            .createNamedQuery( "section.search", Section.class );
        if( maxResults > 0 ) query.setMaxResults( maxResults );
        return query.setParameter( "text", text ).getResultList();
    }

    @Override
    @Transactional
    public Section addSection( Term term, Course course, User instructor )
    {
        String query = "select max(number) from Section "
            + "where term = :term and course = :course";

        Integer result = entityManager.createQuery( query, Integer.class )
            .setParameter( "term", term )
            .setParameter( "course", course )
            .getSingleResult();
        Integer currentNum = result == null ? 0 : result;

        Section section = new Section();
        section.setTerm( term );
        section.setCourse( course );
        if( instructor != null ) section.getInstructors().add( instructor );
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
