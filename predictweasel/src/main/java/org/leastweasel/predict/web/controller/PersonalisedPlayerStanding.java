package org.leastweasel.predict.web.controller;

import org.leastweasel.predict.domain.PlayerStanding;
import org.leastweasel.predict.domain.User;

/**
 * A personalised version of a {@link PlayerStanding}. Player standings are
 * cached and shared among all users. We would like to highlight the logged
 * in user's standing, but that's obviously different for each user. So we
 * decorate the cached version with one of these so we can add extra
 * properties as needed.
 */
public class PersonalisedPlayerStanding extends PlayerStanding {
	private boolean highlighted;
	
    /**
     * Constructor.
     * 
     * @param player the user whose points and position this represents
     * @param position the position in the standings
     * @param pointsScored the number of points scored
     * @param highlighted is this standing one that should be highlighted?
     */
    public PersonalisedPlayerStanding(User player, int position, int pointsScored, boolean highlighted) {
    		super(player, position, pointsScored);
    		
    		this.highlighted = highlighted;
    }
    
    /**
     * Constructor.
     * 
     * @param standing the standing that is being personalised 
     * @param highlighted is this standing one that should be highlighted?
     */
    public PersonalisedPlayerStanding(PlayerStanding standing, boolean highlighted) {
    		this(standing.getPlayer(), standing.getPosition(), standing.getPointsScored(), highlighted);
    }

    /**
     * Should we highlight this standing in the view.
     * 
     * @return true if this standing should be highlighted
     */
	public boolean isHighlighted() {
		return highlighted;
	}
}
