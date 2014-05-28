/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

/**
 * Where a player's score stands within a {@link Prize}. These are calculated on demand rather than
 * being persisted to the database (they can be cached for increaed performance as they only change
 * when rew results are entered).
 */
public class PlayerStanding {
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
}

