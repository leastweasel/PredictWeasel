/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;

/**
 * An interface for dealing with {@link Competition}s.
 */
public interface CompetitionService {
	/**
	 * Get all the active competitions for which {@link League}s can be created. Never returns null.
	 * 
	 * @return the active competitions, or an empty list if there are none
	 */
	List<Competition> getAllActiveCompetitions();
}
