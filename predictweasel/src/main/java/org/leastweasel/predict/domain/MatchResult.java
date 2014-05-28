/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The outcome of a {@link Fixture}. This can be used as either the actual result of a fixture or the
 * predicted result.
 */
@Embeddable
public class MatchResult {
    /**
     * The home team's score.
     */
    private Integer homeScore;

    /**
     * The away team's score.
     */
    private Integer awayScore;

    /**
     * Constructor.
     */
    public MatchResult() {
    }

    /**
     * Constructor.
     *
     * @param homeScore the number of goals/points scored by the home team
     * @param awayScore the away team's score
     */
    public MatchResult(Integer homeScore, Integer awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    /**
     * Get the home score.
     * 
     * @return the home score
     */
    public Integer getHomeScore() {
        return homeScore;
    }

    /**
     * Set the home score.
     * 
     * @param homeScore the home score
     */
    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    /**
     * Get the away score.
     * 
     * @return the away score
     */
    public Integer getAwayScore() {
        return awayScore;
    }

    /**
     * Set the away score.
     * 
     * @param awayScore the away score
     */
    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    /**
     * Is this result the same outcome as the other (i.e. home win, away win, draw?).
     *
     * @param other the result we're comparing with
     * @return true if the two outcomes are the same
     */
    @Transient
    public boolean isSameOutcome(MatchResult other) {
        if (other == null) {
            return false;
        }

        // Can't really compare results with nulls in them, even if they're all null, as that means
        // that it's not a proper result.

        if (homeScore == null || awayScore == null || 
        		other.homeScore == null || other.awayScore == null) {
            return false;
        }

        if (((homeScore > awayScore) && (other.getHomeScore() > other.getAwayScore()))
                || ((homeScore == awayScore) && (other.getHomeScore() == other.getAwayScore()))
                || ((homeScore < awayScore) && (other.getHomeScore() < other.getAwayScore()))) {
            return true;
        }

        return false;
    }

    /**
     * Is this result complete. True if both scores are non-null.
     *
     * @return true if both scores are non-null
     */
    @Transient
    public boolean isComplete() {
        return homeScore != null && awayScore != null;
    }

    /**
     * Equality operator.
     *
     * @param other the object we're comparing against.
     * @return true if the home and away scores match
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!this.getClass().equals(other.getClass())) {
            return false;
        }

        final MatchResult matchResult = (MatchResult) other;

        return new EqualsBuilder().append(homeScore, matchResult.homeScore)
                                  .append(awayScore, matchResult.awayScore)
                                  .isEquals();
    }

    /**
     * Generate a hash code for this object.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(homeScore)
                                    .append(awayScore)
                                    .toHashCode();
    }

    /**
     * Format the object as a String.
     *
     * @return the generated String
     */
    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();

        buff.append(homeScore);
        buff.append("-");
        buff.append(awayScore);

        return buff.toString();
    }
}
