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
        String query = "from User where primaryEmail = :email";

        List<User> users = entityManager.createQuery( query, User.class )
            .setParameter( "email", email )
            .getResultList();
        return users.size() == 0 ? null : users.get( 0 );
    }

    @Override
    public List<User> searchUsers( String term )
    {
        term = term.toLowerCase();
        String query = "from User where cin = :term or lower(username) = :term "
            + "or lower(firstName) = :term or lower(lastName) = :term "
            + "or lower(firstName || ' ' || lastName) = :term "
            + "order by firstName asc";

        return entityManager.createQuery( query, User.class )
            .setParameter( "term", term )
            .getResultList();
    }

    @Override
    public List<User> searchUsersByPrefix( String term )
    {
        term = term.toLowerCase();
        String query = "from User where cin like :term || '%' "
            + "or lower(username) like :term || '%' "
            + "or lower(firstName) like :term || '%' "
            + "or lower(lastName) like :term || '%' "
            + "or lower(firstName || ' ' || lastName) like :term || '%' "
            + "order by firstName asc";

        return entityManager.createQuery( query, User.class )
            .setParameter( "term", term )
            .setMaxResults( 10 )
            .getResultList();
    }

    @Override
    @Transactional
    public User saveUser( User user )
    {
        return entityManager.merge( user );
    }

}
