/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.leastweasel.predict.domain.BlogPost;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.Prize;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.service.BlogService;
import org.leastweasel.predict.service.LeagueService;
import org.leastweasel.predict.service.PredictionService;
import org.leastweasel.predict.service.StandingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	private PredictionService predictionService;
	
	@Autowired
	private StandingsService standingsService;
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private BlogService blogService;
	
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
		
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering view of league with code: {} for user ID: {}",
						 subscription.getLeague().getCode(),
						 subscription.getUser().getId());
		}
		
		return "league";
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
	 * Set up the most recent results, and their predictions, so that we can show
	 * them in the view.     
	 * 
	 * @return a list of the predictions for the most recently completed fixtures
	 */
	@ModelAttribute("matchResults")
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
	@ModelAttribute("predictionFixtures")
	public List<Prediction> upcomingFixtures(UserSubscription subscription,
			 			    		 Model model) {
		
		List<Prediction> fixtures = new ArrayList<>();
		
		int numberOfFixtures = predictionService.getPredictionsForUpcomingFixtures(subscription, fixtures);
		
		model.addAttribute("moreFixtures", numberOfFixtures > fixtures.size()); 
		
		return fixtures;
	}

	/**
	 * Get the standings for the current league in each of the prize categories.    
	 * 
	 * @param subscription identifies the user and the league they're currently playing
	 * @return a list of the player standings, each of which is itself a list
	 */
	@ModelAttribute("prizeStandings")
	public List<List<PersonalisedPlayerStanding>> getPrizeStandings(UserSubscription subscription) {
		List<List<PersonalisedPlayerStanding>> prizeStandings = new ArrayList<>();
		
		prizeStandings.add(standingsService.getAbbreviatedPrizeStandings(subscription, 1));
		prizeStandings.add(standingsService.getAbbreviatedPrizeStandings(subscription, 2));
		prizeStandings.add(standingsService.getAbbreviatedPrizeStandings(subscription, 3));
		
		return prizeStandings;
	}
	
	/**
	 * Get the prizes offered by the current league.    
	 * 
	 * @param subscription identifies the user and the league they're currently playing
	 * @return a list of the prizes being played for
	 */
	@ModelAttribute("prizes")
	public List<Prize> getPrizeCategories(UserSubscription subscription) {
		
		return leagueService.getLeaguePrizes(subscription.getLeague());
	}
	
	/**
	 * Get the most recent post from the given league's blog.
	 * 
	 * @param subscription identifies the user and the league they're currently playing
	 * @return the league's blog's most recent post
	 */
	@ModelAttribute("blogPost")
	public BlogPost getLatestBlogPost(UserSubscription subscription) {
		return blogService.getLatestPostForLeague(subscription.getLeague());
	}
}
