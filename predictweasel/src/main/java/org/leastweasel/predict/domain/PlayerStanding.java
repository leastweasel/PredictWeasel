/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Where a player's score stands within a {@link Prize}. These are calculated on demand rather than
 * being persisted to the database (they can be cached for increased performance as they only change
 * when new results are entered).
 * <p>
 * Player standings sort naturally on the number of points scored, then by player name.
 */
public class PlayerStanding implements Comparable<PlayerStanding> {
	private final User player;
	
	private int position;
	
	private final int pointsScored;
	
    /**
     * Constructor.
     * 
     * @param player the user whose points and position this represents
     * @param position the position in the standings
     * @param pointsScored the number of points scored
     */
    public PlayerStanding(User player, int position, int pointsScored) {
        this.position = position;
        this.player = player;
        this.pointsScored = pointsScored;
    }

    /**
     * Constructor, leaving the position to be set later.
     * 
     * @param player the user whose points and position this represents
     * @param pointsScored the number of points scored
     */
    public PlayerStanding(User player, int pointsScored) {
        this.player = player;
        this.pointsScored = pointsScored;
    }

    /**
     * Get the User whose standing this is.
     * 
     * @return the player playing in a prize
     */
    public User getPlayer() {
		return player;
	}

	/**
     * Get the player's position (i.e. ranking).
     * 
     * @return the player's position. The leader has a position of 1.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Set the player's position (i.e. ranking).
     * 
     * @param position the player's position. The leader has a position of 1.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
 	 * Get the number of points the player has scored.
 	 * 
 	 * @return the number of points
 	 */
	public int getPointsScored() {
		return pointsScored;
	}

    /**
     * Equality operator.
     *
     * @param other the object we're comparing against
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!(other instanceof PlayerStanding)) {
            return false;
        }

        final PlayerStanding standing = (PlayerStanding) other;

        return new EqualsBuilder().append(player.getName(), standing.player.getName())
        							 .append(pointsScored, standing.pointsScored)
                                  .isEquals();
    }

    /**
     * Generate a hash code for this object.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(player.getName())
        							   .append(pointsScored)
                                    .toHashCode();
    }
    
    /**
     * Compare two instances. Note that the comparison is done back to front
     * for what is considered normal because we want the highest point scores
     * to appear first in the list. But we want names to sort normally when
     * the scores are tied.
     * 
     * @param o the other player standing to compare
     * @return the usual result for a comparison, but reversed
     */
    @Override
    public int compareTo(PlayerStanding o) {
	    	return new CompareToBuilder().append(o.pointsScored, pointsScored)
	    								.append(player.getName(), o.player.getName())
	    								.toComparison();
    }
}

