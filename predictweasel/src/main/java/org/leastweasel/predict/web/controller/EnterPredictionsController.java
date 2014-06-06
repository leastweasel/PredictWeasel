/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.ArrayList;
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
 * Controller that allows a user to enter all possible predictions for a competition
 * (although it will be in the context of a {@link League}. The league in question
 * will be the {@link User}'s 'current' league. It  will have been predetermined
 * so is not passed in as a parameter.
 */
@Controller
public class EnterPredictionsController {
	@Autowired
	private PredictionService predictionService;
	
	@Autowired
	private LeagueService leagueService;
	
	private static final Logger logger = LoggerFactory.getLogger(EnterPredictionsController.class);

	/**
	 * Handle a GET request to navigate to the enter all predictions page.     
	 * 
	 * @param subscription the user and league for which we will be displaying the page
	 * @return the name of the view to navigate to (the enter all predictions page)
	 */
	@RequestMapping(value="/league/futurePredictions", method = RequestMethod.GET)
	public String navigateToPage(UserSubscription subscription) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering view for user ID: {} to enter predictions for fixtures from competition ID: {}",
						 subscription.getUser().getId(),
						 subscription.getLeague().getCompetition().getId());
		}
		
		return "allPredictions";
	}
	
	/**
	 * Set up a list of predictions for all fixtures to be played in this league's
	 * competition, so that we can show them in the view.    
	 * 
	 * @param subscription identifies the user and the league they're currently playing
	 * @return a list of the predictions for the fixtures still to be played
	 */
	@ModelAttribute("predictionFixtures")
	public List<Prediction> predictionFixtures(UserSubscription subscription) {
		
		List<Prediction> fixtures = new ArrayList<>();
		
		predictionService.getPredictionsForFutureFixtures(subscription, fixtures);
		
		return fixtures;
	}
}
