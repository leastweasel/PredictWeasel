/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that allows a user to navigate to the home page of a single {@link League}. The
 * league in question will be the {@link User}'s 'current' league. It  will have been
 * predetermined so is not passed in as a parameter.
 * 
 * @see LeagueCodeResolvingHandlerInterceptor
 */
@Controller
public class LeagueController {
	
	private static final Logger logger = LoggerFactory.getLogger(LeagueController.class);

	/**
	 * Handle a GET request to navigate to a league's home page. From here the user can get
	 * a quick view of predictions, results, standings and the blog from the league in question.     
	 * 
	 * @return the name of the view to navigate to (the league home page)
	 */
	@RequestMapping(value="/league/", method = RequestMethod.GET)
	public String navigateToPage(UserSubscription subscription) {
		
		if (subscription == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Can't navigate to league page as user subscription is null");
			}
			
			return "redirect:/";
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering view of league with code: {} for user ID: {}",
						 subscription.getLeague().getCode(),
						 subscription.getUser().getId());
		}
		
		return "league";
	}
}
