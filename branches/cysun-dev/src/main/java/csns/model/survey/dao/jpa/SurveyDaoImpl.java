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
package csns.model.survey.dao.jpa;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Department;
import csns.model.survey.Survey;
import csns.model.survey.dao.SurveyDao;

@Repository
public class SurveyDaoImpl implements SurveyDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Survey getSurvey( Long id )
    {
        return entityManager.find( Survey.class, id );
    }

    @Override
    public List<Survey> getOpenSurveys( Department department )
    {
        Calendar now = Calendar.getInstance();
        String query = "from Survey where publishDate < :now "
            + "and (closeDate is null or closeDate > :now) "
            + "and department = :department and deleted = false "
            + "order by name asc";

        return entityManager.createQuery( query, Survey.class )
            .setParameter( "now", now )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    @PreAuthorize("principal.isFaculty(#department.abbreviation)")
    public List<Survey> getClosedSurveys( Department department )
    {
        Calendar now = Calendar.getInstance();
        String query = "from Survey where publishDate is not null and "
            + "closeDate is not null and closeDate < :now "
            + "and department = :department and deleted = false "
            + "order by closeDate desc";

        return entityManager.createQuery( query, Survey.class )
            .setMaxResults( 25 )
            .setParameter( "now", now )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    @PreAuthorize("principal.isFaculty(#department.abbreviation)")
    public List<Survey> getUnpublishedSurveys( Department department )
    {
        Calendar now = Calendar.getInstance();
        String query = "from Survey where "
            + "(closeDate is null or closeDate > :now) "
            + "and (publishDate is null or publishDate > :now) "
            + "and department = :department and deleted = false "
            + "order by closeDate desc";

        return entityManager.createQuery( query, Survey.class )
            .setParameter( "now", now )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    @PreAuthorize("principal.isFaculty(#department.abbreviation)")
    public List<Survey> getSurveys( Department department )
    {
        String query = "from Survey where department = :department "
            + "and deleted = false order by date desc";

        return entityManager.createQuery( query, Survey.class )
            .setParameter( "department", department )
            .setMaxResults( 30 )
            .getResultList();
    }

    @Override
    @PreAuthorize("principal.isFaculty(#department.abbreviation)")
    public List<Survey> searchSurveys( Department department, String term,
        int maxResults )
    {
        TypedQuery<Survey> query = entityManager.createNamedQuery(
            "survey.search", Survey.class );
        if( maxResults > 0 ) query.setMaxResults( maxResults );
        return query.setParameter( "departmentId", department.getId() )
            .setParameter( "term", term )
            .getResultList();
    }

    @Override
    @PreAuthorize("principal.isFaculty(#department.abbreviation)")
    public List<Survey> searchSurveysByPrefix( Department department,
        String term, int maxResults )
    {
        String query = "from Survey where lower(name) like :term || '%' "
            + "and deleted = false order by name asc";

        return entityManager.createQuery( query, Survey.class )
            .setParameter( "term", term.toLowerCase() )
            .setMaxResults( maxResults )
            .getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("principal.isFaculty(#survey.department.abbreviation)")
    public Survey saveSurvey( Survey survey )
    {
        return entityManager.merge( survey );
    }

}
