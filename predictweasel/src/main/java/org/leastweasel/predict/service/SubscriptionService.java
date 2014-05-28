/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.List;

import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;

/**
 * An interface for dealing with user subscriptions.
 */
public interface SubscriptionService {
	/**
	 * Get all the subscription for the given user. Never returns null.
	 * 
	 * @param user the user whose subscriptions we're after
	 * @return the user's subscriptions, or an empty list if there are none
	 */
	List<UserSubscription> getSubscriptions(User user);
	
	/**
	 * Get all the subscription for the currently logged in user. Never returns null.
	 * 
	 * @return the logged in user's subscriptions, or an empty list if there are none
	 */
	List<UserSubscription> getSubscriptions();
	
	/**
	 * Get a single subscription for the give user. If the user has other than a single
	 * subscription this will return null.
	 * 
	 * @param user the user whose subscriptions we're after
	 * @return the user's single subscription, or null if the user has any other number of subscriptions
	 */
	UserSubscription getSingleSubscription(User user);

	/**
	 * Get a single subscription for the currently logged in user. If the user has other than a single
	 * subscription this will return null.
	 * 
	 * @return the logged in user's single subscription, or null if the user has any other number of subscriptions
	 */
	UserSubscription getSingleSubscription();

	/**
	 * Get a subscription to the league with the given code for the currently logged in user. Returns null if the user
	 * has no subscription to that league.
	 * 
	 * @return the logged in user's subscription to the league, or null if the user has no subscription to that league
	 */
	UserSubscription getSubscriptionFromLeagueCode(String leagueCode);
}
