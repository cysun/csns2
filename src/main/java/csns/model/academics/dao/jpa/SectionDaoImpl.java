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
package csns.model.academics.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Course;
import csns.model.academics.Quarter;
import csns.model.academics.Section;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;

@Repository
public class SectionDaoImpl implements SectionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Section getSection( Long id )
    {
        return entityManager.find( Section.class, id );
    }

    @Override
    public List<Section> getSections( Quarter quarter )
    {
        String query = "from Section where quarter = :quarter "
            + "order by course.code asc, number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "quarter", quarter )
            .getResultList();
    }

    @Override
    public List<Section> getSections( Course course )
    {
        String query = "from Section where course = :course "
            + "order by quarter desc, number asc";

        return entityManager.createQuery( query, Section.class )
            .setParameter( "course", course )
            .getResultList();
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
    public Section saveSection( Section section )
    {
        return entityManager.merge( section );
    }

}
