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
	
		String leagueCode = null;
		
		// Check that the user is logged in.
		User loggedInUser = securityService.getLoggedInUser();
		
		if (loggedInUser != null) {
			// Check the user has at least one subscription.
			List<UserSubscription> subscriptions = subscriptionService.getSubscriptions(loggedInUser);

			if (!subscriptions.isEmpty()) {
				// Has current league code already been set in the session? If so, check that it's
				// still valid for the user.
				String sessionLeagueCode = WebUtil.Session.getCurrentLeagueCode(request.getSession());

				if (sessionLeagueCode != null) {
					if (isSubscribedLeague(subscriptions, sessionLeagueCode)) {
						if (logger.isDebugEnabled()) {
							logger.debug("User with ID {} already has a subscription to league code {} in the session", 
										 loggedInUser.getId(), sessionLeagueCode);
						}
						
						leagueCode = sessionLeagueCode;
					}
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("User with ID {} has no subscriptions", loggedInUser.getId());
				}
			
				// Set to a special league code so that we know the user has no subscriptions,
				// rather than the user hasn't selected one yet.
				leagueCode = WebUtil.Session.NO_SUBSCRIPTIONS;
			}
				
			// If still not got one, check for the cookie.
			if (leagueCode == null) {
				String cookieLeagueCode = WebUtil.Request.getCurrentLeagueCodeFromCookie(request);
				
				if (cookieLeagueCode != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Current league cookie set to {}", leagueCode);
					}
					
					if (isSubscribedLeague(subscriptions, cookieLeagueCode)) {
						if (logger.isDebugEnabled()) {
							logger.debug("User with ID {} has a subscription to league code fromCookie, {}", 
										 loggedInUser.getId(), cookieLeagueCode);
						}
						
						leagueCode = cookieLeagueCode;
					}
				}
			} 
				
			// If still not got one, check for single subscription.
			if (leagueCode == null) {
				if (subscriptions.size() == 1) {
					String singleLeagueCode = subscriptions.get(0).getLeague().getCode();
					
					if (isSubscribedLeague(subscriptions, singleLeagueCode)) {
						if (logger.isDebugEnabled()) {
							logger.debug("User with ID {} has a single subscription to league with code {}", loggedInUser.getId(), singleLeagueCode);
						}
						
						leagueCode = singleLeagueCode;
					}
				} else {
					// Must have multiple selections, so the user needs to select one of them.
					leagueCode = WebUtil.Session.MULTIPLE_SUBSCRIPTIONS;
				}
			} 
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("No user logged in");
			}
		}
		
		// This will remove the session attribute if the value is null.
		WebUtil.Session.setCurrentLeagueCode(request.getSession(), leagueCode);
		
		return true;
	}
	
	/**
	 * Determine whether the given league code is one for which the user has a subscription.
	 *  
	 * @param subscriptions the user's subscriptions
	 * @param leagueCode the league code to check
	 * @return true if the league code is one of the user's subscriptions
	 */
	private boolean isSubscribedLeague(List<UserSubscription> subscriptions, String leagueCode) {
		for (UserSubscription subscription : subscriptions) {
			if (subscription.getLeague().getCode().equals(leagueCode)) {
				return true;
			}
		}
		
		return false;
	}
}
