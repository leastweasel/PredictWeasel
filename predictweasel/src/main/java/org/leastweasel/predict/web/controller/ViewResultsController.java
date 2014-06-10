/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.List;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.service.LeagueService;
import org.leastweasel.predict.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that allows a user to navigate to the page showing the full set of results for
 * a single {@link League}'s competition. The league in question will be the {@link User}'s 
 * 'current' league. It  will have been predetermined so is not passed in as a parameter.
 * <p>
 * Each result also shows the user's prediction, hence the need for a suer subscription.
 */
@Controller
public class ViewResultsController {
	@Autowired
	private PredictionService predictionService;
	
	@Autowired
	private LeagueService leagueService;
	
	private static final Logger logger = LoggerFactory.getLogger(ViewResultsController.class);

	/**
	 * Handle a GET request to navigate to the results page.     
	 * 
	 * @param subscription the user and league for which we will be displaying the results (and predictions)
	 * @return the name of the view to navigate to (the match results page)
	 */
	@RequestMapping(value="/league/results", method = RequestMethod.GET)
	public String navigateToPage(UserSubscription subscription) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering results view of league with code: {} for user ID: {}",
						 subscription.getLeague().getCode(),
						 subscription.getUser().getId());
		}
		
		return "allResults";
	}
	
	/**
	 * Set up the current league, so that we can show details of it in the view.     
	 * 
	 * @return the league currently being played by the logged in user
	 */
	@ModelAttribute("currentLeague")
	public League getCurrentLeague(UserSubscription subscription) {
		return subscription.getLeague();
	}
	
	/**
	 * Set up the match results, and their predictions, so that we can show
	 * them in the view.     
	 * 
	 * @return a list of the predictions for all the completed fixtures in the competition
	 */
	@ModelAttribute("matchResults")
	public List<Prediction> getMatchResults(UserSubscription subscription) {
		return predictionService.getPredictionsForAllResults(subscription);
	}
}
