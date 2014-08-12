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
package csns.model.core.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
        List<User> users = entityManager.createQuery(
            "from User where cin = :cin", User.class )
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
        if( ids == null || ids.length < 1 ) return new ArrayList<User>();

        CriteriaBuilder cbuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cquery = cbuilder.createQuery( User.class );
        Root<User> user = cquery.from( User.class );

        Predicate criteria = cbuilder.equal( user.get( "id" ), ids[0] );
        for( int i = 1; i < ids.length; ++i )
            criteria = cbuilder.or( criteria,
                cbuilder.equal( user.get( "id" ), ids[i] ) );
        cquery.where( criteria );

        cquery.orderBy( cbuilder.asc( user.get( "lastName" ) ),
            cbuilder.asc( user.get( "firstName" ) ) );

        return entityManager.createQuery( cquery ).getResultList();
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
    public List<User> searchUsers( String term )
    {
        term = term.toLowerCase();
        String query = "from User where cin = :term or lower(username) = :term "
            + "or lower(firstName) = :term or lower(lastName) = :term "
            + "or lower(firstName || ' ' || lastName) = :term "
            + "or lower(primaryEmail) = :term order by firstName asc";

        return entityManager.createQuery( query, User.class )
            .setParameter( "term", term )
            .getResultList();
    }

    @Override
    public List<User> searchUsersByPrefix( String term, int maxResults )
    {
        term = term.toLowerCase();
        String query = "from User where cin like :term || '%' "
            + "or lower(username) like :term || '%' "
            + "or lower(firstName) like :term || '%' "
            + "or lower(lastName) like :term || '%' "
            + "or lower(firstName || ' ' || lastName) like :term || '%' "
            + "or lower(primaryEmail) like :term || '%' "
            + "order by firstName asc";

        return entityManager.createQuery( query, User.class )
            .setParameter( "term", term )
            .setMaxResults( maxResults )
            .getResultList();
    }

    @Override
    public List<User> searchUsersByStanding( String dept, String symbol )
    {
        return entityManager.createNamedQuery( "user.search.by.standing",
            User.class )
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
