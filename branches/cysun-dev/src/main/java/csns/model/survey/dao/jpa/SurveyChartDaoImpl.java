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
package csns.model.survey.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Department;
import csns.model.survey.SurveyChart;
import csns.model.survey.dao.SurveyChartDao;

@Repository
public class SurveyChartDaoImpl implements SurveyChartDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SurveyChart getSurveyChart( Long id )
    {
        return entityManager.find( SurveyChart.class, id );
    }

    @Override
    public List<SurveyChart> getSurveyCharts( Department department )
    {
        String query = "from SurveyChart where department = :department "
            + "and deleted = false order by date desc";

        return entityManager.createQuery( query, SurveyChart.class )
            .setParameter( "department", department )
            .getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("principal.isFaculty(#chart.department.abbreviation)")
    public SurveyChart saveSurveyChart( SurveyChart chart )
    {
        return entityManager.merge( chart );
    }

}
