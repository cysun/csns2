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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.core.Subscribable;
import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;

@Repository
public class SubscriptionDaoImpl implements SubscriptionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Subscription getSubscription( Long id )
    {
        return entityManager.find( Subscription.class, id );
    }

    @Override
    public Subscription getSubscription( Subscribable subscribable,
        User subscriber )
    {
        String query = "from Subscription where subscribable = :subscribable "
            + "and subscriber = :subscriber";

        List<Subscription> results = entityManager.createQuery( query,
            Subscription.class )
            .setParameter( "subscribable", subscribable )
            .setParameter( "subscriber", subscriber )
            .getResultList();
        return results.size() == 0 ? null : results.get( 0 );
    }

    @Override
    public List<Subscription> getSubscriptions( Subscribable subscribable )
    {
        String query = "from Subscription where subscribable = :subscribable "
            + "and notificationSent = false";

        return entityManager.createQuery( query, Subscription.class )
            .setParameter( "subscribable", subscribable )
            .getResultList();
    }

    @Override
    public List<Subscription> getSubscriptions( User subscriber, Class<?> clazz )
    {
        String query = "from Subscription where subscriber = :subscriber "
            + "and subscribable.class = :clazz";

        return entityManager.createQuery( query, Subscription.class )
            .setParameter( "subscriber", subscriber )
            .setParameter( "clazz", clazz.getCanonicalName() )
            .getResultList();
    }

    @Override
    @Transactional
    public Subscription subscribe( Subscribable subscribable, User subscriber )
    {
        Subscription subscription = getSubscription( subscribable, subscriber );
        if( subscription == null )
        {
            subscription = new Subscription( subscribable, subscriber );
            subscription = entityManager.merge( subscription );
        }
        return subscription;
    }

    @Override
    @Transactional
    public void unsubscribe( Subscribable subscribable, User subscriber )
    {
        Subscription subscription = getSubscription( subscribable, subscriber );
        if( subscription != null ) entityManager.remove( subscription );
    }

}
