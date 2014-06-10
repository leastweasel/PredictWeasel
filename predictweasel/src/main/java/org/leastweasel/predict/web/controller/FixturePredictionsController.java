/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.Prize;
import org.leastweasel.predict.domain.PrizePoints;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.PrizePointsRepository;
import org.leastweasel.predict.repository.UserSubscriptionRepository;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.LeagueService;
import org.leastweasel.predict.service.PredictionService;
import org.leastweasel.predict.web.domain.FixturePredictionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that allows a user to view all the predictions made by the players in his/her
 * league for a single fixture.
 */
@Controller
public class FixturePredictionsController {

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
	
    private static Logger logger = LoggerFactory.getLogger(FixturePredictionsController.class);

    /**
     * Navigate to the page. Fetch the predictions for the given fixture, made by the players in the
     * same league as the given subscription, and make them available to the view.
     * Do the same for the prizes offered by the league, and the fixture in question. 
     * 
	 * @param subscription the user and league the user is currently playing
     * @param fixture the fixture whose predictions we're after 
     * @param model model attributes should be added here 
     * @return the name of the view to navigate to         
     */
    @RequestMapping(value="/league/fixturePredictions", method = RequestMethod.GET)
	public String navigateToPage(UserSubscription subscription,
			 					@RequestParam(value = "fixture", required = true) Fixture fixture,
			 					Model model) {

		if (fixture != null) {
			League league = subscription.getLeague();
			
			// There's nothing stopping a user from adding any fixture ID to the URL, so we
			// need to check that the fixture is from the current league's competition.
			
			if (!fixture.getCompetition().equals(league.getCompetition())) {
				if (logger.isDebugEnabled()) {
					logger.debug("Fixture ID {} is for competition ID: {}; league competition ID is {} ",
								 fixture.getId(), fixture.getCompetition().getId(), league.getCompetition().getId());
				}
				
				return "invalidFixture";
			}
			
			List<Prize> prizes = leagueService.getLeaguePrizes(subscription.getLeague());

			model.addAttribute("fixturePredictions", createPredictionBeans(league, fixture, prizes));
			model.addAttribute("fixture", fixture);
			model.addAttribute("prizes", prizes);
		}
		
		return "fixturePredictions";
	}
    
    /**
     * Create the beans to display the predictions for this fixture by all the players in the
     * given league.
     * 
     * @param league the league whose players' predictions we're going to fetch
     * @param fixture the fixture all the predictions are for
     * @param prizes the prizes offered by the league 
     * @return a list of all the league's players's predictions for the given fixture
     */
    private List<FixturePredictionBean> createPredictionBeans(League league, Fixture fixture, List<Prize> prizes) {
		List<Prediction> predictions = predictionService.getPredictionsInLeagueForFixture(league, fixture);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Adding {} predictions for fixture ID {}", predictions.size(), fixture.getId());
		}

		// Create a bean for each prediction that also contains the number of points
		// scored by the prediction for the current league.
		List<FixturePredictionBean> predictionBeans = new ArrayList<>(predictions.size());
		
		for (Prediction prediction : predictions) {
			FixturePredictionBean bean = new FixturePredictionBean(prediction);

			// Get the subscription to the current league for the user who made
			// the prediction. It should never return null but better this extra
			// check than an NPE.
			
			UserSubscription predictionSubscription =
					subscriptionRepository.findByUserAndLeague(prediction.getPredictor(), league);

			if (predictionSubscription != null) {
				// Get the number of points scored by this prediction (in this league)
				// for each prize category.
				
				for (Prize prize : prizes) {
					PrizePoints points = 
							prizePointsRepository.findBySubscriptionAndFixtureAndPrizeCode(predictionSubscription, 
																						  fixture, 
																						  prize.getCode());
				
					if (points != null) {
						if (logger.isTraceEnabled()) {
							logger.trace("Prediction for fixture ID: {} by subscription ID: {} scored {} points for prize code {}.",
										fixture.getId(), predictionSubscription.getId(), points.getPointsScored(), prize.getCode());
						}
					}
					
					bean.addPointsScored(points);
				}
				
				predictionBeans.add(bean);
			}
			
			// Sort them...
			Collections.sort(predictionBeans);
		}
		
		return predictionBeans;
    }
}
