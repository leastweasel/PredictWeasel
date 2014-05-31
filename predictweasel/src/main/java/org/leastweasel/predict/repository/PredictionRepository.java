/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.User;
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
}
