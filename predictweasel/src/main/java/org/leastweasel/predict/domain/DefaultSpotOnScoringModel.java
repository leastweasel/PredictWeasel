/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

/**
 * A {@link Scorer} that rewards predictions that are spot on.
 */
public class DefaultSpotOnScoringModel implements Scorer {
    /**
     * The number of points scored for getting the result spot-on.
     */
    private final static int POINTS_FOR_CORRECT_RESULT = 1;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPointsScored(MatchResult predictedScore, MatchResult actualScore) {

        // We're not interested in incomplete scenarios.
        if ((predictedScore == null) || (actualScore == null)) {
            return 0;
        }

        int points = 0;

        // Check for a correct result.

        if (predictedScore.getHomeScore() == actualScore.getHomeScore() &&
        		predictedScore.getAwayScore() == actualScore.getAwayScore()) {
        	
            points += POINTS_FOR_CORRECT_RESULT;
        }

        return points;
	}
}
