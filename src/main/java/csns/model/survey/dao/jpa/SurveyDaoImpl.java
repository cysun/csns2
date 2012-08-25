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
package csns.model.survey.dao.jpa;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public List<Survey> getCurrentSurveys( Department department )
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
    public List<Survey> getSurveys( Department department )
    {
        String query = "from Survey where department = :department "
            + "and deleted = false order by date desc";

        return entityManager.createQuery( query, Survey.class )
            .setParameter( "department", department )
            .setMaxResults( 25 )
            .getResultList();
    }

    @Override
    @Transactional
    public Survey saveSurvey( Survey survey )
    {
        return entityManager.merge( survey );
    }

}
