/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

/**
 * A {@link Scorer} that calculates the number of points that should be scored
 * by a missing prediction. The default implementation is to give the player
 * a 0-0 prediction and calculate the score for that in the normal way using
 * a delegate scorer.
 */
public class DefaultMissingPredictionScoringModel implements Scorer {
	private static final MatchResult IMPOSED_PREDICTION = new MatchResult(0, 0);
	
    /**
     * The delegate actually calculates the score for the imposed prediction.
     */
    private final Scorer delegate;

    /**
     * Constructor.
     * 
     * @param delegateScorer the scorer to which we delegate the task once we've
     *        imposed a prediction on the user
     */
    public DefaultMissingPredictionScoringModel(Scorer delegateScorer) {
    		if (delegateScorer == null) {
    			throw new IllegalArgumentException("Delegate scorer cannot be null");
    		}
    		
    		this.delegate = delegateScorer;
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPointsScored(MatchResult predictedScore, MatchResult actualScore) {

        // We're not interested in incomplete scenarios.
        if (actualScore == null) {
            return 0;
        }

        return delegate.getPointsScored(IMPOSED_PREDICTION, actualScore);
	}
}
