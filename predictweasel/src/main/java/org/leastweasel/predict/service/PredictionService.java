/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.List;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.MatchResult;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;

/**
 * {@link Prediction}-related methods.
 */
public interface PredictionService {
	/**
	 * Get the predictions that were made by the given user for the most recently
	 * completed {@link Fixture}s.
	 * 
	 * @param subscription defines the {@link User} and the {@link League} they're playing
	 * @return the list of predictions for the most recently played fixtures
	 */
	List<Prediction> getPredictionsForRecentResults(UserSubscription subscription);

	/**
	 * Get the predictions that have been made (if any) by the given user for the next batch
	 * of fixtures to be played. If the player hasn't made a prediction for a particular
	 * fixture then the fixture will be wrapped in an empty prediction. 
	 * 
	 * @param subscription defines the {@link User} and the {@link League} they're playing
	 * @return the list of predictions for the next batch of fixtures to be played
	 */
	List<Prediction> getPredictionsForUpcomingFixtures(UserSubscription subscription);
	
	/**
	 * Either create, or update, a prediction by a user for the given fixture. If no
	 * prediction yet exists for the user then a new one is created. If one does
	 * already exist then it'll be updated.
	 * 
	 * @param subscription identifies the user whose prediction this is
	 * @param fixture the fixture for which the user is making a prediction
	 * @param predictedResult the predicted result of the fixture
	 * @return the affected prediction
	 */
	Prediction createOrUpdatePrediction(UserSubscription subscription,
									   Fixture fixture,
									   MatchResult predictedResult);
	
	/**
	 * Calculate the points scored for each prediction based on the given fixture.
	 * 
	 * @param fixture the fixture whose predictions we're going to evaluate
	 */
	void calculatePredictionScoresForFixture(Fixture fixture);
}
