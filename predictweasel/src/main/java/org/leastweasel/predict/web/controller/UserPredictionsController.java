/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.PrizePointsRepository;
import org.leastweasel.predict.repository.UserSubscriptionRepository;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.LeagueService;
import org.leastweasel.predict.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that allows a user to view all the predictions made by a single player
 * in his/her league. Note that only 'fixed' predictions are shown. These are for
 * fixtures that have already started and so another user wouldn't be able to take
 * advantage of seeing a prediction, as they wouldn't be able to change their own.
 */
@Controller
public class UserPredictionsController {

	@Autowired
	private PredictionService predictionService;

	@Autowired
	private LeagueService leagueService;

	@Autowired
	private PrizePointsRepository prizePointsRepository;
	
	@Autowired
	private UserSubscriptionRepository subscriptionRepository;
	
	@Autowired
	private Clock systemClock;
	
    private static Logger logger = LoggerFactory.getLogger(UserPredictionsController.class);

    /**
     * Navigate to the page. Fetch the predictions for the given user, who must have a
     * subscription to same league as the given subscription, and make them available to the view.
     * Do the same for the prizes offered by the league, and the user in question. 
     * 
	 * @param subscription the user and league the user is currently playing
     * @param user the user whose predictions we're after 
     * @param model model attributes should be added here 
     * @return the name of the view to navigate to         
     */
    @RequestMapping(value="/league/userPredictions", method = RequestMethod.GET)
	public String navigateToPage(UserSubscription subscription,
			 					@RequestParam(value = "user", required = true) User user,
			 					Model model) {

		if (user != null) {
			League league = subscription.getLeague();

			UserSubscription otherUserSubscription =
					subscriptionRepository.findByUserAndLeague(user, league);

			// There's nothing stopping a user from adding any user ID to the URL, so we
			// need to check that the user has a subscription to this league.
			
			if (otherUserSubscription == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("User ID {} does not have a subscription to league ID {} ",
								 user.getId(), league.getId());
				}
				
				return "invalidUser";
			}
			
			model.addAttribute("userPredictions", predictionService.getFixedPredictions(otherUserSubscription));
			model.addAttribute("user", user);
		}
		
		return "userPredictions";
	}
}
