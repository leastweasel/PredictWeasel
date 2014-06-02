/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@link Competition} objects.
 */
public interface CompetitionRepository extends CrudRepository<Competition, Long> {
    /**
     * Fetch all the competitions whose {@code active} flag matches the given parameter value.
     * They'll be sorted as requested in the second parameter.
     *
     * @param sortOrder defines the property we want to sort by, and the order (ascending or descending)
     * @return all of the matching competitions
     */
    List<Competition> findByActive(boolean isActive, Sort sortOrder);
}
