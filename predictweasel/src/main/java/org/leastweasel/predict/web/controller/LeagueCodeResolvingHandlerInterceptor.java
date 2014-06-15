/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.service.SecurityService;
import org.leastweasel.predict.service.SubscriptionService;
import org.leastweasel.predict.web.SessionSettings;
import org.leastweasel.predict.web.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Intercepts configured requests to set up the 'current' {@link League} code in the
 * player's HTTP session. The current league is the one they're playing at the
 * moment.
 */
@Component
public class LeagueCodeResolvingHandlerInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private SessionSettings sessionSettings;
	
	private static final Logger logger = LoggerFactory.getLogger(LeagueCodeResolvingHandlerInterceptor.class);
	
	/**
	 * Process the request before the controller gets it. A number of factors can affect
	 * what the current game is:
	 * <ul>
	 * <li>If there's no logged in user, there's no current game</li>
	 * <li>If there's a cookie with a league code in the request then that's the current league</li>
	 * <li>If the user only has one active subscription then that's the current league</li>
	 * </ul>
	 * We also make sure that the user actually has a subscription to the league whose code we
	 * save in the session.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
	
		UserSubscription currentSubscription  = null;
		
		// Check that the user is logged in.
		User loggedInUser = securityService.getLoggedInUser();
		
		if (loggedInUser == null) {
			sessionSettings.noLeague();
			
			return true;
		}
		
		// Check the user has at least one subscription.
		List<UserSubscription> subscriptions = subscriptionService.getSubscriptions(loggedInUser);

		if (subscriptions.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.debug("User with ID {} has no subscriptions", loggedInUser.getId());
			}
		
			sessionSettings.noLeague();
		}
		
		// Has current league code already been set in the session? If so, check that it's
		// still valid for the user.
		String sessionLeagueCode = sessionSettings.getCurrentLeagueCode();

		if (sessionLeagueCode != null) {
			currentSubscription = getSubscription(subscriptions, sessionLeagueCode);
			
			if (currentSubscription != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("User with ID {} already has a subscription to league code {} in the session", 
								 loggedInUser.getId(), sessionLeagueCode);
				}
			}
		}
			
		// If still not got one, check for the cookie.
		if (currentSubscription == null) {
			String cookieLeagueCode = WebUtil.Request.getCurrentLeagueCodeFromCookie(request);
			
			if (cookieLeagueCode != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Current league cookie set to {}", cookieLeagueCode);
				}
				
				currentSubscription = getSubscription(subscriptions, cookieLeagueCode);
				
				if (currentSubscription != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("User with ID {} has a subscription to league code from cookie, {}", 
									 loggedInUser.getId(), cookieLeagueCode);
					}
				}
			}
		} 
			
		// If still not got one, check for single subscription.
		if (currentSubscription == null) {
			if (subscriptions.size() == 1) {
				String singleLeagueCode = subscriptions.get(0).getLeague().getCode();
				
				currentSubscription = getSubscription(subscriptions, singleLeagueCode);
				
				if (currentSubscription != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("User with ID {} has a single subscription to league with code {}", loggedInUser.getId(), singleLeagueCode);
					}
				}
			}
		} 
		
		// Update the league-related session settings for the current user.
		
		sessionSettings.setHasMultipleSubscriptions(subscriptions.size() > 1);
		
		if (currentSubscription != null) {
			sessionSettings.setCurrentLeagueCode(currentSubscription.getLeague().getCode());
			sessionSettings.setLeagueAdmin(currentSubscription.getLeague().getOwner().equals(loggedInUser));
			
			if (sessionSettings.isLeagueAdmin()) {
				if (logger.isDebugEnabled()) {
					logger.debug("User ID {} is admin for league with code {}",
								 loggedInUser.getId(), currentSubscription.getLeague().getId());
				}
			}
		} else {
			sessionSettings.noLeague();
		}
		
		return true;
	}
	
	/**
	 * Determine whether the given league code is one for which the user has a subscription.
	 *  
	 * @param subscriptions the user's subscriptions
	 * @param leagueCode the league code to check
	 * @return the subscription for the league code, or null if there isn't one
	 */
	private UserSubscription getSubscription(List<UserSubscription> subscriptions, String leagueCode) {
		for (UserSubscription subscription : subscriptions) {
			if (subscription.getLeague().getCode().equals(leagueCode)) {
				return subscription;
			}
		}
		
		return null;
	}
}
