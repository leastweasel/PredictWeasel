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
import org.leastweasel.predict.repository.FixtureRepository;
import org.leastweasel.predict.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	@Autowired
	private FixtureRepository fixtureRepository;
	
	@Autowired
	private PredictionService predictionService;
	
	private static final Logger logger = LoggerFactory.getLogger(LeagueController.class);

	/**
	 * Handle a GET request to navigate to a league's home page. From here the user can get
	 * a quick view of predictions, results, standings and the blog from the league in question.     
	 * 
	 * @param subscription the user and league for which we will be displaying the page
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
	
	/**
	 * Set up the most recent results, and their predictions, so that we can show
	 * them in the view.     
	 * 
	 * @return a list of the predictions for the most recently completed fixtures
	 */
	@ModelAttribute("recentResults")
	public List<Prediction> getRecentResults(UserSubscription subscription) {
		return predictionService.getPredictionsForRecentResults(subscription);
	}
	
	/**
	 * Set up a list of predictions for the next fixtures to be played, so that we can show
	 * them in the view. We'll also allow the user to create and later edit their predictions.    
	 * 
	 * @param subscription identifies the user and the league they're currently playing
	 * @return a list of the predictions for the next batch of fixtures to be played
	 */
	@ModelAttribute("upcomingFixtures")
	public List<Prediction> getUpcomingFixtures(UserSubscription subscription) {
		return predictionService.getPredictionsForUpcomingFixtures(subscription);
	}
}
