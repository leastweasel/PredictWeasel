/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.Fixture;
import org.springframework.data.domain.Sort;
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
     * @param sortOrder defines the property we want to sort by, and the order (ascending or descending)
     * @return all of the competition's completed fixtures
     */
    List<Fixture> findByCompetitionAndResultIsNotNull(Competition competition, Sort sortOrder);

    /**
     * Fetch all the fixtures for the given competition that have not yet started.
     *
     * @param competition the competition whose fixtures we're after
     * @param cutoffTime any fixtures starting before this time will be ignored
     * @return all of the competition's fixtures that have yet to start
     */
    List<Fixture> findByCompetitionAndMatchTimeAfter(Competition competition, DateTime cutoffTime);
}
