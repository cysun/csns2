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
package csns.model.core.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.core.Resource;
import csns.model.core.dao.ResourceDao;

@Repository
public class ResourceDaoImpl implements ResourceDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Resource getResource( Long id )
    {
        return entityManager.find( Resource.class, id );
    }

    @Override
    @Transactional
    public Resource saveResource( Resource resource )
    {
        return entityManager.merge( resource );
    }

}
