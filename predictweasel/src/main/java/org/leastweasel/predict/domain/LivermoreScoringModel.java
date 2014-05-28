/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

/**
 * A {@link Scorer} that uses the Livermore scoring model to calculate the worth
 * of predictions. The Livermore model is as follows:<p>
 * <ul>
 * <li>Correct outcome of fixture scores 7 points, incorrect scores 0.</li>
 * <li>Score 1 point for each goal where number of goals for a team correctly predicted.</li>
 * <li>Lose 1 point for each goal by which team's score was predicted incorrectly. </li>
 * <li>Score 1 point per goal for correctly guessing the difference in the two teams' scores. </li>
 * </ul>
 */
public class LivermoreScoringModel implements Scorer {
    /**
     * The number of points scored for getting the result right.
     */
    private final static int POINTS_FOR_CORRECT_RESULT = 7;

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

        if (predictedScore.isSameOutcome(actualScore)) {
            points += POINTS_FOR_CORRECT_RESULT;
        }

        // Check for the difference in the scores.

        int predictedDifference = predictedScore.getHomeScore() -
                                  predictedScore.getAwayScore();

        int actualDifference = actualScore.getHomeScore() -
                               actualScore.getAwayScore();

        if (predictedDifference == actualDifference) {
            points += Math.abs (predictedDifference);
        }

        // Check the individual scores.

        if (predictedScore.getHomeScore() == actualScore.getHomeScore()) {
            points += predictedScore.getHomeScore();
        } else {
            points -= Math.abs(predictedScore.getHomeScore() -
                               actualScore.getHomeScore());
        }

        if (predictedScore.getAwayScore() == actualScore.getAwayScore()) {
            points += predictedScore.getAwayScore();
        } else {
            points -= Math.abs(predictedScore.getAwayScore() -
                               actualScore.getAwayScore());
        }

        return points;
	}
}
