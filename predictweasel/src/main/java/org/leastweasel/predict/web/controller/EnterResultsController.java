/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.MatchResult;
import org.leastweasel.predict.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller that handles requests to enter one or more {@link MatchResult}s. There
 * are two ways to this:
 * <ul>
 * <li>Enter results for fixtures that don't yet have a result</li>
 * <li>Enter results for all results, whether they already have a result or not.</li>
 * </ul>
 * In the first case the results are saved one-by-one, via a request submitted when
 * the input field loses focus (the same as when entering predictions). In the
 * second case a number of results can be entered in a form and then submitted
 * together. 
 */
@Controller
public class EnterResultsController {
	@Autowired
	private CompetitionService competitionService;
	
	/**
	 * Handle a GET request to navigate to the page where the administrative user can enter
	 * missing results.     
	 * 
	 * @param competition the {@link Competition} for whose fixtures we want to enter results
	 * @return a model, containing the fixtures for which the user can enter results, and the
	 *         name of the view to navigate to (the enter missing results page)
	 */
	@RequestMapping(value="/competition/missingResults", method = RequestMethod.GET)
	public ModelAndView navigateToMissingResultsPage(@RequestParam(value = "competition", required = true) Competition competition) {
		
		ModelAndView mav = new ModelAndView("enterMissingResults");
		
		mav.addObject("fixtures", competitionService.getStartedFixturesWithNoResult(competition));
		
		return mav;
	}
}
