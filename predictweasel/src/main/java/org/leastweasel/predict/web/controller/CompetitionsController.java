/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.service.CompetitionService;
import org.leastweasel.predict.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that allows an administrative user to navigate to the page from
 * which s/he can perform {@link Competition} based actions, such as entering
 * results for a competition's fixtures.
 */
@Controller
public class CompetitionsController {
	@Autowired
	private CompetitionService competitionService;
	
	private static final Logger logger = LoggerFactory.getLogger(CompetitionsController.class);

	/**
	 * Handle a GET request to navigate to the page from which s/he can perform
	 * {@link Competition} based actions, such as entering results for a competition's fixtures.     
	 * 
	 * @return the name of the view to navigate to (the competitions home page)
	 */
	@RequestMapping(value="/competitions", method = RequestMethod.GET)
	public String navigateToPage() {
		
		return "competitions";
	}
	
	/**
	 * Set up the list of all active competitions so that the user can pick one from a drop-down.     
	 * 
	 * @return a list of the active competitions on the system
	 */
	@ModelAttribute("competitions")
	public List<Competition> getActiveCompetitions() {
		return competitionService.getAllActiveCompetitions();
	}
}
