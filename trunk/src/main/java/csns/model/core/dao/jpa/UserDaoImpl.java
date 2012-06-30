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
package csns.model.core.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.core.User;
import csns.model.core.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * A helper method for setting multiple parameters.
     */
    private TypedQuery<User> setParameters( TypedQuery<User> query,
        Object[] params )
    {
        for( int i = 0; i < params.length; ++i )
            query.setParameter( i + 1, params[i] );

        return query;
    }

    @Override
    public User getUser( Long id )
    {
        return entityManager.find( User.class, id );
    }

    @Override
    public User getUserByCin( String cin )
    {
        String query = "from User where cin = :cin and cinEncrypted = false "
            + "or cin = :encryptedCin and cinEncrypted = true";

        String encryptedCin = passwordEncoder.encodePassword( cin, null );
        List<User> users = entityManager.createQuery( query, User.class )
            .setParameter( "cin", cin )
            .setParameter( "encryptedCin", encryptedCin )
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
            + "where username = :username";

        List<User> users = entityManager.createQuery( query, User.class )
            .setParameter( "username", username )
            .getResultList();
        return users.size() == 0 ? null : users.get( 0 );
    }

    @Override
    public User getUserByEmail( String email )
    {
        List<User> users = entityManager.createQuery(
            "from User where email1 = :email", User.class )
            .setParameter( "email", email )
            .getResultList();
        return users.size() == 0 ? null : users.get( 0 );
    }

    @Override
    public List<User> searchUsers( String term )
    {
        term = term.toLowerCase();
        String query = "from User where cin = ?1 or lower(username) = ?2 "
            + "or lower(firstName) = ?3 or lower(lastName) = ?4 "
            + "or lower(firstName || ' ' || lastName) = ?5 "
            + "order by firstName asc";
        Object params[] = { term, term, term, term, term };

        return setParameters( entityManager.createQuery( query, User.class ),
            params ).getResultList();
    }

    @Override
    public List<User> searchUsersByPrefix( String term )
    {
        term = term.toLowerCase();
        String query = "from User where cin like ?1 || '%' "
            + "or lower(username) like ?2 || '%' "
            + "or lower(firstName) like ?3 || '%' "
            + "or lower(lastName) like ?4 || '%' "
            + "or lower(firstName || ' ' || lastName) like ?5 || '%' "
            + "order by firstName asc";
        Object params[] = { term, term, term, term, term };

        return setParameters( entityManager.createQuery( query, User.class ),
            params ).getResultList();
    }

    @Override
    @Transactional
    public User saveUser( User user )
    {
        return entityManager.merge( user );
    }

}
