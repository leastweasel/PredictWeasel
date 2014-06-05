/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.List;

import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.web.controller.PersonalisedPlayerStanding;

/**
 * An interface for dealing with player standings.
 */
public interface StandingsService {
	/**
	 * Get the abbreviated standings for a league and prize. Abbreviated standings
	 * return the top few players only. If the player identified by the subscrption
	 * isn't in that list they'll be tacked on the end so the player knows where
	 * they stand.
	 * 
	 * @param defines the player and the league being played
	 * @param prizeNumber the index of the prize these standings are for(1, 2, 3)
	 * @return the player standings for the league and prize
	 */
	List<PersonalisedPlayerStanding> getAbbreviatedPrizeStandings(UserSubscription subscription, int prizeNumber);
	
	/**
	 * Get the full standings for a league and prize.
	 * 
	 * @param defines the player and the league being played
	 * @param prizeNumber the index of the prize these standings are for(1, 2, 3)
	 * @return the player standings for the league and prize
	 */
	List<PersonalisedPlayerStanding> getFullPrizeStandings(UserSubscription subscription, int prizeNumber);
}
