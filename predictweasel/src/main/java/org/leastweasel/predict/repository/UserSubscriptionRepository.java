/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@link UserSubscription} objects.
 */
public interface UserSubscriptionRepository extends CrudRepository<UserSubscription, Long> {
    /**
     * Fetch all the user subscriptions for the given user.
     *
     * @param user the user whose subscriptions we're after
     * @return all of the user's subscriptions
     */
    List<UserSubscription> findByUser(User user);
    
    /**
     * Fetch the {@link User}'s subscription to the given {@link League}.
     *
     * @param user the user whose subscription we're after
     * @param league the specific subscription we want is for this league
     * @return the user's subscription, or null if one doesn't exist
     */
    UserSubscription findByUserAndLeague(User user, League league);
}
