/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.List;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.League;
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
}
