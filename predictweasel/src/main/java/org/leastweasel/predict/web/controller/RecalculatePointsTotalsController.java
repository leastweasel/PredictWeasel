/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.Locale;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that allows an administrative user to trigger a recalculation
 * of the player points totals for the subscribed players of a {@link Competition}'s 
 * {@link League}s. This would typically be because they've finished entering a number
 * of results. The affected user subscriptions are updated with the new totals.
 */
@Controller
public class RecalculatePointsTotalsController {
	@Autowired
	private LeagueService leagueService;
	
    @Autowired
    private MessageSource messageSource;
    
	/**
	 * Handle a GET request to recalculate the points totals.     
	 * 
	 * @param competition the ID of the competition whose subscription points totals need recalculating
     * @param redirectAttributes any attributes we want to survive across the redirect should be added here 
     * @param locale the locale used in the request
	 * @return the name of the view to go to next
	 */
	@RequestMapping(value="/competition/recalculatePointsTotals", method = RequestMethod.GET)
	public String triggerRecalculation(@RequestParam("competition") Competition competition,
									  Locale locale,
									  RedirectAttributes redirectAttributes) {
	
		int numberOfLeagues = leagueService.recalculateCompetitionLeaguePointsTotals(competition);

        // Indicate on the screen that the recalculation was successful. This can only be done right
        // at the end as it's important that the redirect happens.
        String message = messageSource.getMessage("flash.points.recalculated", 
        										  	 new Object [] { numberOfLeagues }, 
        										  	 locale);
        
        FlashMessageHelper.addSuccessMessage(redirectAttributes, message);

		return "redirect:/competitions";
	}
}
