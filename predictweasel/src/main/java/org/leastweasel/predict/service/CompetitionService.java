/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.MatchResult;

/**
 * An interface for dealing with {@link Competition}s and their {@link Fixture}s.
 */
public interface CompetitionService {
	/**
	 * Get all the active competitions for which {@link League}s can be created. Never returns null.
	 * 
	 * @return the active competitions, or an empty list if there are none
	 */
	List<Competition> getAllActiveCompetitions();
	
	/**
	 * Get all the fixtures that we might want to enter an initial result for. Never returns null.
	 * We can't know when a match has finished but we can, at least,
	 * identify fixtures that have started but that don't have a result yet.
	 * <p>
	 * Note that this only returns fixtures that have no result.
	 * 
	 * @param competition the Competition whose fixtures we're after
	 * @return a list of fixtures that have kicked off but have no result yet, or
	 * 		  an empty list if there aren't any
	 */
	List<Fixture> getStartedFixturesWithNoResult(Competition competition);
	
	/**
	 * Update the result of the given fixture.
	 * 
	 * @param fixture the fixture to update
	 * @param result the fixture's result
	 */
	void saveResult(Fixture fixture, MatchResult result);
}
