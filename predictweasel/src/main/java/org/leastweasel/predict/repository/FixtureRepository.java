/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.Fixture;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@link Fixture} objects.
 */
public interface FixtureRepository extends CrudRepository<Fixture, Long> {
    /**
     * Fetch all the completed fixtures for the given competition.
     *
     * @param competition the competition whose fixtures we're after
     * @return all of the competition's completed fixtures
     */
    List<Fixture> findByCompetitionAndResultIsNotNull(Competition competition);
}
