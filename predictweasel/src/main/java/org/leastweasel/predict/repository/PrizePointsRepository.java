/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.PrizePoints;
import org.leastweasel.predict.domain.UserSubscription;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@link PrizePoints} objects.
 */
public interface PrizePointsRepository extends CrudRepository<PrizePoints, Long> {
	/**
	 * Find the instance matching the subscription, fixture and prize code. This is
	 * a unique combination so will return one entity or null.
	 * 
	 * @param subscription the fixture the prediction as based on and the user who made it (won't be null)
	 * @param fixture the fixture the prediction was made for (won't be null)
	 * @param prizeCode the prize that should calculate the number of points scored
	 */
	PrizePoints findBySubscriptionAndFixtureAndPrizeCode(UserSubscription subscription,
														Fixture fixture,
														String prizeCode);
}
