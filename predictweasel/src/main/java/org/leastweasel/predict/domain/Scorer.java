/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

/**
 * An interface for classes that calculate the points scored for {@link Fixture}
 * predictions. It must be able to return a points score given a predicted
 * and actual score.
 */
public interface Scorer {
    /**
     * Get the points scored by the given prediction.
     * 
     * @param predictedScore the predicted scored
     * @param actualScore the actual score in the fixture
     * @return the number of points the prediction is worth
     */
    public int getPointsScored (MatchResult predictedScore,
                                MatchResult actualScore);
}
