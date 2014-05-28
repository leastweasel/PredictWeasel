/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

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
}
