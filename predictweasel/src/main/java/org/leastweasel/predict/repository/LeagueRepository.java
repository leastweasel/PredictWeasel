/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;
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
}
