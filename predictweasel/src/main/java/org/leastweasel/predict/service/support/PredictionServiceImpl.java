/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.MatchResult;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.Prize;
import org.leastweasel.predict.domain.PrizePoints;
import org.leastweasel.predict.domain.Prizes;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.FixtureRepository;
import org.leastweasel.predict.repository.LeagueRepository;
import org.leastweasel.predict.repository.PredictionRepository;
import org.leastweasel.predict.repository.PrizePointsRepository;
import org.leastweasel.predict.repository.UserSubscriptionRepository;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the {@link PredictionService}. 
 */
@Service
public class PredictionServiceImpl implements PredictionService {
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
	private Clock systemClock;
	
	@Autowired
	private Prizes prizes;
	
	@Value("${predictWeasel.minimumNumberOfFixturesToDisplay}")
	private int minimumNumberOfFixturesToDisplay;
	
	private static final Logger logger = LoggerFactory.getLogger(PredictionServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public List<Prediction> getPredictionsForRecentResults(UserSubscription subscription) {
		
		if (subscription != null) {
			// Get all the fixtures that have a result, ordered by match time. We're interested
			// in the most recent results so we sort by descending match time.
			Sort sortOrder = new Sort(Direction.DESC, "matchTime");
			
			List<Fixture> results = 
					fixtureRepository.findByCompetitionAndResultIsNotNull(subscription.getLeague().getCompetition(),
																		 sortOrder);
		
			if (logger.isDebugEnabled()) {
				logger.debug("Got {} results", results.size());
			}

			// Trim the list down to the desired number.
			results = getMostRelevant(results, minimumNumberOfFixturesToDisplay);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Got the {} most relevant results", results.size());
			}

			List<Prediction> predictions = new ArrayList<>();

			for (Fixture fixture : results) {
				Prediction prediction = predictionRepository.findByPredictorAndFixture(subscription.getUser(), fixture);
				
				if (prediction == null) {
					prediction = new Prediction();
					
					prediction.setFixture(fixture);
					prediction.setPredictor(subscription.getUser());
				}
				
				predictions.add(prediction);
			}
			
			return predictions;
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Prediction> getPredictionsForUpcomingFixtures(UserSubscription subscription) {
		
		if (subscription != null) {
			// Get all the fixtures that haven't started yet.
			List<Fixture> fixtures = 
					fixtureRepository.findByCompetitionAndMatchTimeAfter(subscription.getLeague().getCompetition(),
																		systemClock.getCurrentDateTime());

			if (logger.isDebugEnabled()) {
				logger.debug("Got {} future fixtures", fixtures.size());
			}

			// Trim the list down to the desired number.
			fixtures = getMostRelevant(fixtures, minimumNumberOfFixturesToDisplay);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Got the {} most relevant upcoming fixtures", fixtures.size());
			}

			// Get any existing predictions for these fixtures.
			List<Prediction> predictions = new ArrayList<>();

			for (Fixture fixture : fixtures) {
				Prediction prediction = 
						predictionRepository.findByPredictorAndFixture(subscription.getUser(), fixture);

				// If there's no real prediction, wrap the fixture in a fake one so that
				// the user can see they've yet to enter one.
				
				if (prediction == null) {
					prediction = new Prediction();
					
					prediction.setFixture(fixture);
					prediction.setPredictor(subscription.getUser());
				}

				predictions.add(prediction);
			}

			return predictions;
		}
	
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public Prediction createOrUpdatePrediction(UserSubscription subscription,
			   								  Fixture fixture,
			   								  MatchResult predictedResult) {

		Prediction prediction = 
				predictionRepository.findByPredictorAndFixture(subscription.getUser(), fixture);
		
		if (prediction == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("No prediction exists for user ID {} and fixture ID {}", 
							 subscription.getUser().getId(),
							 fixture.getId());
			}
			
			prediction = new Prediction();
			
			prediction.setFixture(fixture);
			prediction.setPredictor(subscription.getUser());
			prediction.setPredictedResult(predictedResult);
			
			prediction = predictionRepository.save(prediction);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Found prediction ID {} for user ID {} and fixture ID {}",
							 prediction.getId(),
							 subscription.getUser().getId(),
							 fixture.getId());
			}
			
			prediction.setPredictedResult(predictedResult);
		}
		
		return prediction;
	}

	/**
	 * {@inheritDoc}
	 */
	public void calculatePredictionScoresForFixture(Fixture fixture) {
		// find all the leagues played against this fixture's competition.
		List<League> leagues = leagueRepository.findByCompetition(fixture.getCompetition()); 

		if (logger.isDebugEnabled()) {
			logger.debug("Calculating points for {} leagues based on competition ID: {}", leagues.size(), 
																						 fixture.getCompetition().getId());
		}
		for (League league : leagues) {
		
		    // Get all the user subscriptions to this league.
			List<UserSubscription> subscriptions = userSubscriptionRepository.findByLeague(league);

			logger.debug("Calculating points for {} subscriptions to league with code: '{}'", subscriptions.size(), 
					 																		 league.getCode());
			
			for (UserSubscription subscription : subscriptions) { 
				MatchResult predictedResult = null;
				Prediction prediction = 
						predictionRepository.findByPredictorAndFixture(subscription.getUser(), fixture);
				
				// Calculate the value of the prediction using the prizes and scorer of the league.
				if (prediction != null) {
					predictedResult = prediction.getPredictedResult();
				}
				
				createPrizePoints(subscription, fixture, predictedResult, prizes.getPrizeForCode(league.getPrizeOneCode()));
				createPrizePoints(subscription, fixture, predictedResult, prizes.getPrizeForCode(league.getPrizeTwoCode()));
				createPrizePoints(subscription, fixture, predictedResult, prizes.getPrizeForCode(league.getPrizeThreeCode()));
			}
		}
	}
	
	/**
	 * Create a {@link PrizePoints} object for the given combination, or update an existing value. A subscription
	 * can have separate instances for each of its up-to-three prizes.
	 * 
	 * @param subscription the fixture the prediction as based on and the user who made it (won't be null)
	 * @param fixture the fixture the prediction was made for (won't be null)
	 * @param predictedResult the result predicted by the user
	 * @param prize the prize that should calculate the number of points scored
	 */
	private void createPrizePoints(UserSubscription subscription, Fixture fixture, MatchResult predictedResult, Prize prize) {
		if (prize != null) {
			int pointsScored = prize.calculatePointsScored(fixture, predictedResult);
			
			PrizePoints points = prizePointsRepository.findBySubscriptionAndFixtureAndPrizeCode(subscription, fixture, prize.getCode());

			if (points == null) {
				points = new PrizePoints();
	
				points.setFixture(fixture);
				points.setSubscription(subscription);
				points.setPrizeCode(prize.getCode());
			}

			points.setPointsScored(pointsScored);

			if (logger.isDebugEnabled()) {
				logger.debug("Calculated {} points for fixture ID: {}, subscription ID {}, prize code: {}",
								points.getPointsScored(),
								fixture.getId(),
								subscription.getId(),
								prize.getCode());
			}
			
			prizePointsRepository.save(points);
		}
	}
	
	/**
	 * Get the most relevant fixtures from the given list. What we want is a
	 * minimum number of fixtures with an additional requirement being that
	 * all fixtures from a given day are included if any of them are. So
	 * we could end up with:
	 * <ul>
     * <li>more than the minimum number, if there are more than we need on the final day</li>
     * <li>the minimum number, if there happen to be the right number on the final day</li>
     * <li>fewer than the minimum number, if the full list is shorter than that.</li>
     * </ul>
     * For this to work the fixtures must have been sorted, by start date, in the desired order.
	 *  
	 * @param allFixtures the full list of fixtures from which to choose
	 * @param minimumNumberRequired we want at least this many fixtures, if there are that many
	 * @return
	 */
	private List<Fixture> getMostRelevant(List<Fixture> allFixtures, int minimumNumberRequired) {
		if (allFixtures.size() <= minimumNumberRequired) {
			return allFixtures;
		}
		
		List<Fixture> desiredFixtures = new ArrayList<>();
		Set<LocalDate> usedDates = new HashSet<>(); 

		for (Fixture fixture : allFixtures) {
			LocalDate matchDate = fixture.getMatchTime().toLocalDate();

			/*
			 * If this is a new day then we can stop if we already have enough.
			 * Otherwise we carry on, ultimately adding all fixtures that are on this day.
			 */
			if (!usedDates.contains(matchDate)) {
				
				if (desiredFixtures.size() >= minimumNumberRequired) {
					break;
				} else {
					usedDates.add(matchDate);
				}
			}
			
			desiredFixtures.add(fixture);
		}
		
		return desiredFixtures;
	}
}
