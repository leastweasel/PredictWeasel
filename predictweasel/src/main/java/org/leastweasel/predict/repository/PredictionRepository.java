/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@Prediction} objects.
 */
public interface PredictionRepository extends CrudRepository<Prediction, Long> {
    /**
     * Find the prediction made by the given {@link User} for the given {@link Fixture}.
     *
     * @param predictor the user who made the prediction
     * @param the fixture for which we want the user's prediction
     * @return a prediction (or null if none match)
     */
    Prediction findByPredictorAndFixture(User predictor, Fixture fixture);
    
    /**
     * Get the predictions made on a single fixture by the players of a given league.
     * 
     * @param league the league whose players' predictions we're after
     * @param fixture the fixture whose predictions we're after
     * @return the predictions made on a fixture for a given league
     */
	@Query("select p from Prediction p, UserSubscription us where p.predictor = us.user and us.league = ?1 and p.fixture = ?2")
    List<Prediction> findFixturePredictionsFromLeague(League league, Fixture fixture);
}
