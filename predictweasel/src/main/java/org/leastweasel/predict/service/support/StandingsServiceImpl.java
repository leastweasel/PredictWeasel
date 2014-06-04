/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.ArrayList;
import java.util.List;

import org.leastweasel.predict.domain.PlayerStanding;
import org.leastweasel.predict.domain.StandingsCache;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.FixtureRepository;
import org.leastweasel.predict.repository.LeagueRepository;
import org.leastweasel.predict.repository.PredictionRepository;
import org.leastweasel.predict.repository.PrizePointsRepository;
import org.leastweasel.predict.repository.UserSubscriptionRepository;
import org.leastweasel.predict.service.StandingsService;
import org.leastweasel.predict.web.controller.PersonalisedPlayerStanding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * An implementation of the {@link StandingsService}. 
 */
@Service
public class StandingsServiceImpl implements StandingsService {
	@Autowired
	private FixtureRepository fixtureRepository;
	
	@Autowired
	private PredictionRepository predictionRepository;
	
	@Autowired
	private LeagueRepository leagueRepository;
	
	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;
	
	@Autowired
	private PrizePointsRepository prizePointsRepository;
	
	@Autowired
	private StandingsCache standingsCache;
	
	@Value("${predictWeasel.maximumNumberOfStandingsToDisplay}")
	private int maximumNumberOfStandingsToDisplay;
	
	private static final Logger logger = LoggerFactory.getLogger(StandingsServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public List<PersonalisedPlayerStanding> getAbbreviatedPrizeStandings(UserSubscription subscription, int prizeNumber) {
		return  getMostRelevant(getPrizeStandings(subscription, prizeNumber),
								subscription.getUser(),
								maximumNumberOfStandingsToDisplay);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<PlayerStanding> getPrizeStandings(UserSubscription subscription, int prizeNumber) {
		String prizeCode = subscription.getLeague().getPrizeCode(prizeNumber);
		
		if (prizeCode != null) {
			return standingsCache.getStandings(subscription.getLeague(), prizeCode);
		}
		
		return new ArrayList<>();
	}
	/**
	 * Get the most relevant standings from the given list. For this to work the standings must
	 * have been sorted, by position, in the desired order.
	 *  
	 * @param allStandings the full list of standings from which to choose
	 * @param playerToHighlight make sure this player's standing appears in the list, even if
	 * 		  it has to be tacked on to the end
	 * @param minimumNumberRequired we want at least this many standings, if there are that many
	 * @return
	 */
	private List<PersonalisedPlayerStanding> getMostRelevant(List<PlayerStanding> allStandings,
															User playerToHighlight,
															int maximumNumberRequired) {
		
		PlayerStanding highlightStanding = null;

		// Find the standing for the player to highlight.
		for (PlayerStanding standing : allStandings) {
			if (standing.getPlayer().equals(playerToHighlight)) {
				highlightStanding = standing;
				break;
			}
		}
		
		List<PersonalisedPlayerStanding> desiredStandings = new ArrayList<>();
		
		// Now add the desired number of standings to the list.
		boolean addedHighlightStanding = false;
		
		for (PlayerStanding standing : allStandings) {
			// If we haven't added the highlight player yet then reduce the
			// maximum by 1 as we're going to have to add it after.
			int maxNumber = maximumNumberRequired - (addedHighlightStanding ? 0 : 1);
			
			if (desiredStandings.size() >= maxNumber) {
				break;
			} else {
				if (!addedHighlightStanding && standing.getPlayer().equals(playerToHighlight)) {
					desiredStandings.add(new PersonalisedPlayerStanding(standing, true));
					addedHighlightStanding = true;
				} else {
					desiredStandings.add(new PersonalisedPlayerStanding(standing, false));
				}
			}
		}
		
		if (!addedHighlightStanding) {
			desiredStandings.add(new PersonalisedPlayerStanding(highlightStanding, true));
		}
		
		return desiredStandings;
	}
}
