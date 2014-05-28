/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.ArrayList;
import java.util.List;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.LeagueRepository;
import org.leastweasel.predict.repository.UserSubscriptionRepository;
import org.leastweasel.predict.service.SecurityService;
import org.leastweasel.predict.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An implementation of the {@link SubscriptionService}. 
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UserSubscriptionRepository subscriptionRepository;
	
	@Autowired
	private LeagueRepository leagueRepository;

	private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public List<UserSubscription> getSubscriptions(User user) {
		List<UserSubscription> subscriptions = subscriptionRepository.findByUser(user);
		
		if (subscriptions == null) {
			subscriptions = new ArrayList<>();
		}
		
		return subscriptions;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<UserSubscription> getSubscriptions() {
		return getSubscriptions(securityService.getLoggedInUser());
	}

	/**
	 * {@inheritDoc}
	 */
	public UserSubscription getSingleSubscription(User user) {
		if (user != null) {
			List<UserSubscription> subscriptions = getSubscriptions(user);

			if (logger.isDebugEnabled()) {
				logger.debug("User ID: {} has {} subscriptions", user.getId(), subscriptions.size());
			}
			
			if (subscriptions.size() == 1) {
				return subscriptions.get(0);
			}
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public UserSubscription getSingleSubscription() {
		return getSingleSubscription(securityService.getLoggedInUser());
	}

	/**
	 * {@inheritDoc}
	 */
	public UserSubscription getSubscriptionFromLeagueCode(String leagueCode) {
		User loggedInUser = securityService.getLoggedInUser();

		if (loggedInUser != null && leagueCode != null) {
			League league = leagueRepository.findByCode(leagueCode);
			
			if (league != null) {
				UserSubscription subscription = 
						subscriptionRepository.findByUserAndLeague(loggedInUser, league);
				
				if (logger.isDebugEnabled()) {
					if (subscription != null) {
						logger.debug("Found subscriptionID: {} for user ID: {} and league code: {}", 
									 subscription.getId(),
									 subscription.getUser().getId(),
									 leagueCode);
					} else {
						logger.debug("No subscription found for user ID: {} and league code: {}", 
								 loggedInUser.getId(),
								 leagueCode);
					}
				}
				
				return subscription;
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("No league found with code: {}", leagueCode);
				}
			}
		}
		
		return null;
	}
}
