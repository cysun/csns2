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
package csns.model.core.dao.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUser( Long id )
    {
        return entityManager.find( User.class, id );
    }

    @Override
    public User getUserByCin( String cin )
    {
        List<User> users = entityManager
            .createQuery( "from User where cin = :cin", User.class )
            .setParameter( "cin", cin )
            .getResultList();
        return users.size() == 0 ? null : users.get( 0 );
    }

    @Override
    public User getUserByUsername( String username )
    {
        // This method is mainly used by the security code which usually needs
        // both the user credentials (i.e. username and password) and the
        // granted authorities (i.e. roles), so here we load the roles
        // collection "eagerly" using a join fetch to avoid a second query.
        String query = "from User user left join fetch user.roles "
            + "where lower(username) = :username";

        List<User> users = entityManager.createQuery( query, User.class )
            .setParameter( "username", username.toLowerCase() )
            .getResultList();
        return users.size() == 0 ? null : users.get( 0 );
    }

    @Override
    public User getUserByEmail( String email )
    {
        String query = "from User where lower(primaryEmail) = :email";

        List<User> users = entityManager.createQuery( query, User.class )
            .setParameter( "email", email.toLowerCase() )
            .getResultList();
        return users.size() == 0 ? null : users.get( 0 );
    }

    @Override
    public List<User> getUsers( Long ids[] )
    {
        if( ids == null || ids.length == 0 ) return new ArrayList<User>();
        
        String query = "from User where id in (:ids) "
            + "order by lastName asc, firstName asc";

        return entityManager.createQuery( query, User.class )
            .setParameter( "ids", Arrays.asList( ids ) )
            .getResultList();
    }

    @Override
    public List<User> getUsers( String lastName, String firstName )
    {
        String query = "from User where lower(lastName) = :lastName "
            + "and lower(firstName) = :firstName";

        return entityManager.createQuery( query, User.class )
            .setParameter( "lastName", lastName.toLowerCase() )
            .setParameter( "firstName", firstName.toLowerCase() )
            .getResultList();
    }

    @Override
    public List<User> searchUsers( String text )
    {
        text = text.toLowerCase();
        String query = "from User where cin = :text or lower(username) = :text "
            + "or lower(firstName) = :text or lower(lastName) = :text "
            + "or lower(firstName || ' ' || lastName) = :text "
            + "or lower(primaryEmail) = :text order by firstName asc";

        return entityManager.createQuery( query, User.class )
            .setParameter( "text", text )
            .getResultList();
    }

    @Override
    public List<User> searchUsersByPrefix( String text, int maxResults )
    {
        text = text.toLowerCase();
        String query = "from User where cin like :text || '%' "
            + "or lower(username) like :text || '%' "
            + "or lower(firstName) like :text || '%' "
            + "or lower(lastName) like :text || '%' "
            + "or lower(firstName || ' ' || lastName) like :text || '%' "
            + "or lower(primaryEmail) like :text || '%' "
            + "order by firstName asc";

        return entityManager.createQuery( query, User.class )
            .setParameter( "text", text )
            .setMaxResults( maxResults )
            .getResultList();
    }

    @Override
    public List<User> searchUsersByStanding( String dept, String symbol )
    {
        return entityManager
            .createNamedQuery( "user.search.by.standing", User.class )
            .setParameter( "dept", dept )
            .setParameter( "symbol", symbol.toUpperCase() )
            .getResultList();
    }

    @Override
    @Transactional
    public User saveUser( User user )
    {
        return entityManager.merge( user );
    }

}
