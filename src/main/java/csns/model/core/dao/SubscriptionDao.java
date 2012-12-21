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
package csns.model.core.dao;

import java.util.List;

import csns.model.core.Subscribable;
import csns.model.core.Subscription;
import csns.model.core.User;

public interface SubscriptionDao {

    Subscription getSubscription( Long id );

    Subscription getSubscription( Subscribable subscribable, User subscriber );

    List<Subscription> getSubscriptions( Subscribable subscribable );

    List<Subscription> getSubscriptions( User subscriber, Class<?> clazz );

    long getSubscriptionCount( Subscribable subscribable );

    Subscription subscribe( Subscribable subscribable, User subscriber );

    void unsubscribe( Subscribable subscribable, User subscriber );

    Subscription saveSubscription( Subscription subscription );

    int autoUnsubscribe();

}
