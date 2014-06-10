/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.PrizePoints;
import org.leastweasel.predict.domain.UserSubscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@link PrizePoints} objects.
 */
public interface PrizePointsRepository extends CrudRepository<PrizePoints, Long> {
	/**
	 * Find the instance matching the {@link UserSubscription}, {@link Fixture} and prize code. 
	 * This is a unique combination so will return one entity or null.
	 * 
	 * @param subscription the fixture the prediction was based on and the user who made it (won't be null)
	 * @param fixture the fixture the prediction was made for (won't be null)
	 * @param prizeCode the prize that should calculate the number of points scored
	 */
	PrizePoints findBySubscriptionAndFixtureAndPrizeCode(UserSubscription subscription,
														Fixture fixture,
														String prizeCode);

	/**
	 * Get the total number of points scored by a user in a particular league for
	 * the given prize. We can't do summing using the function name alone, so specify
	 * the query explicitly.
	 * 
	 *  @param subscription the combination of league and user
	 *  @param prizeCode the code of the prize we want a total for
	 *  @return the total number of points scored for the prize 
	 */
	@Query("select sum(pp.pointsScored) from PrizePoints pp where pp.subscription = ?1 and pp.prizeCode = ?2")
	Integer getTotalSubscriptionPointsForPrize(UserSubscription subscription, String prizeCode);
}
