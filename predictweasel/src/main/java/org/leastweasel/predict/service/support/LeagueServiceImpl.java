/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.PlayerStanding;
import org.leastweasel.predict.domain.Prize;
import org.leastweasel.predict.domain.Prizes;
import org.leastweasel.predict.domain.StandingsCache;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.CompetitionRepository;
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
	private CompetitionRepository competitionRepository;
	
	@Autowired
	private PrizePointsRepository prizePointsRepository;

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;
	
	@Autowired
	private Prizes prizes;
	
	@Autowired
	private StandingsCache standingsCache;
	
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
	 * {@inheritDoc}
	 */
	@Override
	public void recalculateAllLeagueStandings() {
		List<Competition> competitions = competitionRepository.findByActive(true, null);
		
		for (Competition competition : competitions) {
			recalculateCompetitionLeagueStandings(competition);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void recalculateCompetitionLeagueStandings(Competition competition) {
		if (competition != null) {
			List<League> leaguesToRecalculate = leagueRepository.findByCompetition(competition);
	
			if (logger.isDebugEnabled()) {
				logger.debug("About to recalulate standings for {} leagues based on competition ID: {}",
							 leaguesToRecalculate.size(), competition.getId());
			}
			
			for (League league : leaguesToRecalculate) {
				logger.debug("About to recalulate standings for league with code: {}", league.getCode());
				
				List<PlayerStanding> standings = calculateLeaguePrizeStandings(league, league.getPrizeOneCode(), 1);
				standingsCache.addStandingsForLeagueAndPrize(league, league.getPrizeOneCode(), standings);
				
				if (league.getPrizeTwoCode() != null) {
					standings = calculateLeaguePrizeStandings(league, league.getPrizeTwoCode(), 2);
					standingsCache.addStandingsForLeagueAndPrize(league, league.getPrizeTwoCode(), standings);
				}

				if (league.getPrizeThreeCode() != null) {
					standings = calculateLeaguePrizeStandings(league, league.getPrizeThreeCode(), 3);
					standingsCache.addStandingsForLeagueAndPrize(league, league.getPrizeThreeCode(), standings);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Prize> getLeaguePrizes(League league) {
		List<Prize> leaguePrizes = new ArrayList<>();
		
		for (int i = 1; i < 4; i++) {
			String prizeCode = league.getPrizeCode(i);
			
			if (prizeCode != null) {
				Prize prize = prizes.getPrizeForCode(prizeCode);
				
				if (prize != null) {
					leaguePrizes.add(prize);
				}
			}
		}
		
		return leaguePrizes;
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
	
	/**
	 * Calculate the standings for a single league prize.
	 * 
	 * @param league the league whose standings we're going to calculate (won't be null)
	 * @param prizeCode the specific prize we're interested in
	 * @param prizeIndex which prize is this (1, 2 or 3?)
	 * @return the standings for the given league and prize
	 */
	private List<PlayerStanding> calculateLeaguePrizeStandings(League league, 
															  String prizeCode,
															  int prizeIndex) {
		
		List<PlayerStanding> prizeStandings = new ArrayList<>();
		Set<UserSubscription> leagueSubscriptions = league.getSubscriptions();
		
		if (logger.isDebugEnabled()) {
			logger.debug("About to calculate order of ");
		}
		
		for (UserSubscription subscription : leagueSubscriptions) {
			prizeStandings.add(new PlayerStanding(subscription.getUser(), 
												 subscription.getPrizePoints(prizeIndex)));
		}
		
		Collections.sort(prizeStandings);
		
        int lastPoints = Integer.MAX_VALUE;
        int position = 0;
        int ties = 0;

        for (PlayerStanding standing : prizeStandings) {
            if (standing.getPointsScored() < lastPoints) {
                position += ties + 1;
                lastPoints = standing.getPointsScored();
                ties = 0;
            } else {
                ties++;
            }

            standing.setPosition(position);
        }
        
        return prizeStandings;
	}
}
