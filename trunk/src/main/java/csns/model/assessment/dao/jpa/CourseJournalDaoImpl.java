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

import csns.model.academics.Department;
import csns.model.assessment.CourseJournal;
import csns.model.assessment.dao.CourseJournalDao;

@Repository
public class CourseJournalDaoImpl implements CourseJournalDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CourseJournal getCourseJournal( Long id )
    {
        return entityManager.find( CourseJournal.class, id );
    }

    @Override
    public List<CourseJournal> getSubmittedCourseJournals( Department department )
    {
        String query = "from CourseJournal where approveDate is null and "
            + "submitDate is not null and section.course.department = :department";

        return entityManager.createQuery( query, CourseJournal.class )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    @Transactional
    public CourseJournal saveCourseJournal( CourseJournal courseJournal )
    {
        return entityManager.merge( courseJournal );
    }

}
