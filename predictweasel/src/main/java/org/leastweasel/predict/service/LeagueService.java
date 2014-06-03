/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;

/**
 * {@link League}-related methods.
 */
public interface LeagueService {
	/**
	 * Recalculate the points totals of all subscriptions for all leagues based on the 
	 * given Competition, probably because we have recently entered results for this
	 * competition's fixtures.
	 * 
	 * @param competition all leagues for this competition should have their
	 * 		  subscription points totals recalculated
	 * @return the number of leagues that had their points totals recalculated
	 */
	int recalculateCompetitionLeaguePointsTotals(Competition competition);
}
