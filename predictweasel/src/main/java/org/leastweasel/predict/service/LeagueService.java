/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prize;

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
	
	/**
	 * Recalculate the league standings for all leagues. This will usually be in
	 * response to the application starting as the standings aren't persisted.
	 */
	void recalculateAllLeagueStandings();
	
	/**
	 * Recalculate the league standings for all leagues based on the given competition.
	 * This will often be in response to some new results being entered but, as
	 * standings aren't persisted, we'll need to recalculate them when the application starts.
	 * 
	 * @param competition all leagues for this competition should have their
	 * 		  standings recalculated
	 */
	void recalculateCompetitionLeagueStandings(Competition competition);
	
	/**
	 * Get the prizes offered by this league as a list. Will never return even an empty
	 * list as a league always has at least one prize.
	 * 
	 * @return league the league whose prizes we're after
	 */
	List<Prize> getLeaguePrizes(League league);
}
