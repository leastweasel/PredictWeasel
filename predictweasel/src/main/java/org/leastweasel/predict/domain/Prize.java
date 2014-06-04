/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

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
	private final Scorer scorer;
	
	private FixtureFilter [] fixtureFilters;
	
	private final String code;
	
	private final String name;
	
	private Scorer missingPredictionScorer;

	/**
	 * Constructor.
	 * 
	 * @param code the prize's code
	 * @param scorer calculates how many points each prediction is worth in this prize
	 */
	public Prize(String code, String name, Scorer scorer) {
		this.code = code;
		this.name = name;
		this.scorer = scorer;
	}
	
	/**
	 * Calculate the score of the given prediction. We can't just pass in a prediction because,
	 * although a Prediction contains both the fixture and predicted result, the prediction
	 * might be null, so we wouldn't know the fixture.

	 * @param fixture the fixture for which we're calculating the points scored
	 * @param predictedScore the predicted score, which may be null
	 * @return the number of points scored by the prediction in this prize
	 */
	public int calculatePointsScored(Fixture fixture, MatchResult predictedScore) {
		
		// If this prize doesn't accept all fixtures then check whether this fixture is a counter.
		
		if (fixtureFilters != null) {
			for (FixtureFilter filter : fixtureFilters) {
				if (!filter.accept(fixture)) {
					return 0;
				}
			}
		}
		
		if (predictedScore == null) {
			// A missing prediction often incurs a penalty, so get its score separately.
			if (missingPredictionScorer != null) {
				return missingPredictionScorer.getPointsScored(null, fixture.getResult());
			} else {
				return 0;
			}
		} else {
			return scorer.getPointsScored(predictedScore, fixture.getResult());
		}
	}
	
	/**
	 * Set the scorer to use if the user fails to enter a prediction.
	 * 
	 * @param scorer the scorer to use
	 */
	public void setMissingPredictionScorer(Scorer scorer) {
		this.missingPredictionScorer = scorer;
	}

	/**
	 * Set the list of filters that will determine which {@link Fixture}s to include in the calculations.
	 * 
	 * @param fixtureFilters the list of filters
	 */
	public void setFixtureFilters(FixtureFilter... fixtureFilters) {
		this.fixtureFilters = fixtureFilters;
	}

	/**
	 * Get the code, which is how this prize is referenced elsewhere in the application. Prize
	 * codes must be unique across the application.
	 * 
	 * @return the prize's code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Get the name, which is how this prize is displayed to the user.
	 * 
	 * @return the prize's name
	 */
	public String getName() {
		return name;
	}
}
