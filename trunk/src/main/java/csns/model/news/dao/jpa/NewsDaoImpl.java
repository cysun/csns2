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
package csns.model.news.dao.jpa;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Department;
import csns.model.news.News;
import csns.model.news.dao.NewsDao;

@Repository
public class NewsDaoImpl implements NewsDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public News getNews( Long id )
    {
        return entityManager.find( News.class, id );
    }

    @Override
    public List<News> getNews( Department department )
    {
        String query = "from News where department = :department "
            + "and expireDate > :now order by id desc";

        return entityManager.createQuery( query, News.class )
            .setParameter( "department", department )
            .setParameter( "now", Calendar.getInstance() )
            .getResultList();
    }

    @Override
    @Transactional
    public News saveNews( News news )
    {
        return entityManager.merge( news );
    }

}
