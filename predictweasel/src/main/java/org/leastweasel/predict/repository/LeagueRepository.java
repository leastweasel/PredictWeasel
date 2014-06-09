/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.LeagueState;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@League} objects.
 */
public interface LeagueRepository extends CrudRepository<League, Long> {
    /**
     * Find the league with the given code.
     *
     * @param code the code of the league to fetch
     * @return a league (or null if none match)
     */
    League findByCode(String code);
    
    /**
     * Find the leagues based on the given {@link Competition}.
     *
     * @param competition the competition of the leagues to fetch
     * @return a list of leagues (or null if none match)
     */
    List<League> findByCompetition(Competition competition);
    
    /**
     * Fetch all the leagues in the given state.
     * 
     * @param state the state of the leagues we want to fetch
     * @return the leagues in the given state
     */
    List<League> findByState(LeagueState state);
    
    /**
     * Find out how many leagues there are that are in the given state.
     * 
     * @param state the state of the leagues we want to count
     * @return the number of leagues in the given state
     */
    Long countByState(LeagueState state);
}
