/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.ArrayList;
import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.repository.CompetitionRepository;
import org.leastweasel.predict.service.CompetitionService;
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
}
