/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A cache of {@link PlayerStanding}s.
 */
public class StandingsCache {
	private Map<League, Map<String, List<PlayerStanding>>> leagueStandings =
			new HashMap<League, Map<String,List<PlayerStanding>>>();

	private static final Logger logger = LoggerFactory.getLogger(StandingsCache.class);
	
	/**
	 * Add the given list of player standings to the cache, replacing any that are
	 * already there.
	 * 
	 * @param league the league for which the standing apply
	 * @param prizeCode the specific prize code for which they apply
	 * @param standings the new standings
	 */
	public void addStandingsForLeagueAndPrize(League league, String prizeCode, List<PlayerStanding> standings) {
		Map<String, List<PlayerStanding>> standingsForLeague = leagueStandings.get(league);
	
		if (logger.isDebugEnabled()) {
			logger.debug("Adding {} standings for league ID: {} and prize code: {}", 
						 standings.size(), league.getId(), prizeCode);
		}
		if (standingsForLeague == null) {
			standingsForLeague = new HashMap<String, List<PlayerStanding>>();
			leagueStandings.put(league, standingsForLeague);
		}
		
		standingsForLeague.put(prizeCode, standings);
	}
	
	/**
	 * Get the standings for the given league and prize code.
	 * 
	 * @param league the league for which the standing apply
	 * @param prizeCode the specific prize code for which they apply
	 * @return the player standings
	 */
	public List<PlayerStanding> getStandings(League league, String prizeCode) {
		Map<String, List<PlayerStanding>> standingsForLeague = leagueStandings.get(league);
		
		if (standingsForLeague != null) {
			List<PlayerStanding> standingsForPrize = standingsForLeague.get(prizeCode);
			
			if (standingsForPrize != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Got {} cached standings for league ID: {} and prize code: {}",
								 standingsForPrize.size(), league.getId(), prizeCode);
				}
				
				return standingsForPrize;
			}
		}
		
		return new ArrayList<>();
	}
}