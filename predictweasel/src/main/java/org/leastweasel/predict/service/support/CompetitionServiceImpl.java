/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.ArrayList;
import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.MatchResult;
import org.leastweasel.predict.repository.CompetitionRepository;
import org.leastweasel.predict.repository.FixtureRepository;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.CompetitionService;
import org.leastweasel.predict.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * An implementation of the {@link CompetitionService}. 
 */
@Service
public class CompetitionServiceImpl implements CompetitionService {
	@Autowired
	private CompetitionRepository competitionRepository;

	@Autowired
	private FixtureRepository fixtureRepository;

	@Autowired
	private PredictionService predictionService;
	
	@Autowired
	private Clock systemClock;
	
	private static final Logger logger = LoggerFactory.getLogger(CompetitionServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public List<Competition> getAllActiveCompetitions() {
		
		// Sort the competitions by ascending name.
		Sort sortOrder = new Sort(Direction.ASC, "name");
		
		List<Competition> competitions = 
				competitionRepository.findByActive(true, sortOrder);

		if (competitions == null) {
			competitions = new ArrayList<>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Got {} competitions", competitions.size());
		}

		return competitions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Fixture> getStartedFixturesWithNoResult(Competition competition) {
		
		if (competition != null) {
			// Get all the fixtures that have started but have no result, ordered by match time. 
			// We're interested in the fixture that started longest ago so we sort by ascending match time.
			Sort sortOrder = new Sort(Direction.ASC, "matchTime");
			
			List<Fixture> fixtures = 
					fixtureRepository.findByCompetitionAndMatchTimeBeforeAndResultIsNull(competition,
																					    systemClock.getCurrentDateTime(),
																						sortOrder);
		
			if (logger.isDebugEnabled()) {
				logger.debug("Got {} fixtures that might need a result", fixtures.size());
			}
			
			return fixtures;
		}
		
		return new ArrayList<Fixture>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void saveResult(Fixture fixture, MatchResult result) {
		fixture.setResult(result);
		fixtureRepository.save(fixture);
		predictionService.calculatePredictionScoresForFixture(fixture);
	}
}
