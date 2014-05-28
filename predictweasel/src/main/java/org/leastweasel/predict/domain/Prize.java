/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import java.util.List;

/**
 * Defines what the players of PredictWeasel are playing for. A Prize defines how many points a 
 * {@link Prediction} is worth, as calculated by its associated {@link Scorer} instance, and whether a 
 * given {@link Fixture} should be counted towards that prize. For example, a prize for most
 * points scored during the knock-out stage would only accept fixtures within the knock-out stage.
 * <p> 
 * Each {@link League} can have one or more prizes associated with it. For each league's prize
 * the players will appear in a separate set of standings.
 */
public class Prize {
	private Scorer scorer;
	
	private List<FixtureFilter> fixtureFilters;

	/**
	 * Set the prize scorer, which will calculate how many points each prediction is worth in this prize.
	 * 
	 * @param scorer the scorer
	 */
	public void setScorer(Scorer scorer) {
		this.scorer = scorer;
	}

	/**
	 * Set the list of filters that will determine which {@link Fixture}s to include in the calculations.
	 * 
	 * @param fixtureFilters the list of filters
	 */
	public void setFixtureFilters(List<FixtureFilter> fixtureFilters) {
		this.fixtureFilters = fixtureFilters;
	}
}
