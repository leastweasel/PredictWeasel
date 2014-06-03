/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.List;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prizes;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.LeagueRepository;
import org.leastweasel.predict.repository.PrizePointsRepository;
import org.leastweasel.predict.repository.UserSubscriptionRepository;
import org.leastweasel.predict.service.LeagueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An implementation of the {@link LeagueService}. 
 */
@Service
public class LeagueServiceImpl implements LeagueService {
	@Autowired
	private LeagueRepository leagueRepository;
	
	@Autowired
	private PrizePointsRepository prizePointsRepository;

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;
	
	@Autowired
	private Prizes prizes;
	
	private static final Logger logger = LoggerFactory.getLogger(LeagueServiceImpl.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int recalculateCompetitionLeaguePointsTotals(Competition competition) {
		if (competition != null) {
			List<League> leaguesToRecalculate = leagueRepository.findByCompetition(competition);
	
			if (logger.isDebugEnabled()) {
				logger.debug("About to recalulate points totals for {} leagues based on competition ID: {}",
							 leaguesToRecalculate.size(), competition.getId());
			}
			
			for (League league : leaguesToRecalculate) {
				logger.debug("About to recalulate points totals for league with code: {}", league.getCode());

				for (UserSubscription subscription : league.getSubscriptions()) {
					subscription.setPrizeOnePoints(getNumberOfPointsForPrize(subscription, league.getPrizeOneCode()));
					subscription.setPrizeTwoPoints(getNumberOfPointsForPrize(subscription, league.getPrizeTwoCode()));
					subscription.setPrizeThreePoints(getNumberOfPointsForPrize(subscription, league.getPrizeThreeCode()));
					
					userSubscriptionRepository.save(subscription);
				}
			}
			
			return leaguesToRecalculate.size();
		}
		
		return 0;
	}
	
	/**
	 * Add up the points scored by a player in a league for the given prize.
	 *  
	 * @param subscription the user making predictions in a particular league
	 * @param prizeCode we'll be adding up the points scored in this prize only
	 * @return the number of points scored by the user
	 */
	private int getNumberOfPointsForPrize(UserSubscription subscription, String prizeCode) {
		int points = 0;
		
		// Only the first prize is mandatory.
		
		if (prizeCode != null) {
			points = prizePointsRepository.getTotalSubscriptionPointsForPrize(subscription, prizeCode);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Total points for subscription ID: {} and prize code {} is {}",
							 subscription.getId(), prizeCode, points);
			}
		}
		
		return points;
	}
}
