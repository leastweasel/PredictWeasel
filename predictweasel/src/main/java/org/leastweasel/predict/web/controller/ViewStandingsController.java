/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prize;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.service.LeagueService;
import org.leastweasel.predict.service.StandingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that allows a user to navigate to the page showing the full standings
 * for a single {@link League}. The league in question will be the {@link User}'s 
 * 'current' league. It  will have been predetermined so is not passed in as a parameter.
 */
@Controller
public class ViewStandingsController {
	@Autowired
	private StandingsService standingsService;
	
	@Autowired
	private LeagueService leagueService;
	
	private static final Logger logger = LoggerFactory.getLogger(ViewStandingsController.class);

	/**
	 * Handle a GET request to navigate to the standings page.     
	 * 
	 * @param subscription the user and league for which we will be displaying the standings
	 * @return the name of the view to navigate to (the league standings page)
	 */
	@RequestMapping(value="/league/standings", method = RequestMethod.GET)
	public String navigateToPage(UserSubscription subscription) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering standings view of league with code: {} for user ID: {}",
						 subscription.getLeague().getCode(),
						 subscription.getUser().getId());
		}
		
		return "allStandings";
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
	 * Get the standings for the current league in each of the prize categories.    
	 * 
	 * @param subscription identifies the user and the league they're currently playing
	 * @return a list of the player standings, each of which is itself a list
	 */
	@ModelAttribute("prizeStandings")
	public List<List<PersonalisedPlayerStanding>> getPrizeStandings(UserSubscription subscription) {
		List<List<PersonalisedPlayerStanding>> prizeStandings = new ArrayList<>();
		
		prizeStandings.add(standingsService.getFullPrizeStandings(subscription, 1));
		prizeStandings.add(standingsService.getFullPrizeStandings(subscription, 2));
		prizeStandings.add(standingsService.getFullPrizeStandings(subscription, 3));
		
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
}
