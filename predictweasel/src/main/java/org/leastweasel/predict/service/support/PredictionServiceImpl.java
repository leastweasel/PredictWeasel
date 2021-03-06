/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.leastweasel.predict.domain.Competition;
import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.MatchResult;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.Prize;
import org.leastweasel.predict.domain.PrizePoints;
import org.leastweasel.predict.domain.Prizes;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.FixtureRepository;
import org.leastweasel.predict.repository.LeagueRepository;
import org.leastweasel.predict.repository.PredictionRepository;
import org.leastweasel.predict.repository.PrizePointsRepository;
import org.leastweasel.predict.repository.UserSubscriptionRepository;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.PredictionService;
import org.leastweasel.predict.web.domain.PredictionBean;
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
			List<Fixture> results = getResults(subscription.getLeague().getCompetition(), true);
			
			return createPredictionBeansForResults(results, subscription.getUser());
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Prediction> getPredictionsForMissingResults(UserSubscription subscription) {
		if (subscription != null) {
			// Get all the fixtures that have started, but that don't yet have a result,
			// ordered by match time so that the most recent is first.
			
			Sort sortOrder = new Sort(Direction.DESC, "matchTime");
			
			List<Fixture> fixtures = 
					fixtureRepository.findByCompetitionAndMatchTimeBeforeAndResultIsNull(subscription.getLeague().getCompetition(),
																						systemClock.getCurrentDateTime(),
																						sortOrder);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Got {} started fixtures with no result", fixtures.size());
			}

			return createPredictionBeansForResults(fixtures, subscription.getUser());
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Prediction> getPredictionsForAllResults(UserSubscription subscription) {
		
		if (subscription != null) {
			List<Fixture> results = getResults(subscription.getLeague().getCompetition(), false);
			
			return createPredictionBeansForResults(results, subscription.getUser());
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Prediction> getFixedPredictions(UserSubscription subscription) {
		// Get all the fixtures that have started, ordered by match time. We're interested
		// in the most recent results so we sort by descending match time.
		Sort sortOrder = new Sort(Direction.DESC, "matchTime");
		DateTime now = systemClock.getCurrentDateTime();
		
		List<Fixture> results = 
				fixtureRepository.findByCompetitionAndMatchTimeBefore(subscription.getLeague().getCompetition(),
																	 now, sortOrder);
	
		if (logger.isDebugEnabled()) {
			logger.debug("Got {} results", results.size());
		}
		
		List<Prediction> predictions = createPredictionBeansForResults(results, subscription.getUser());
		
		if (predictions == null) {
			predictions = new ArrayList<>();
		}

		return predictions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPredictionsForUpcomingFixtures(UserSubscription subscription,
												List<Prediction> predictionsToReturn) {
		
		int numberOfEditableFixtures = 0;
		
		if (subscription != null) {
			// Get all the fixtures that haven't started yet. We want the next match to start to
			// be the first in the list.
			Sort sortOrder = new Sort(Direction.ASC, "matchTime");
			
			List<Fixture> fixtures = 
					fixtureRepository.findByCompetitionAndMatchTimeAfter(subscription.getLeague().getCompetition(),
																		systemClock.getCurrentDateTime(),
																		sortOrder);

			numberOfEditableFixtures = fixtures.size();
			
			if (logger.isDebugEnabled()) {
				logger.debug("Got {} future fixtures", numberOfEditableFixtures);
			}

			// Trim the list down to the desired number.
			fixtures = getMostRelevant(fixtures, minimumNumberOfFixturesToDisplay);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Got the {} most relevant upcoming fixtures", fixtures.size());
			}

			// Get any existing predictions for these fixtures, or create a dummy one if none exists.
			generatePredictionsFromFixtures(subscription.getUser(), fixtures, predictionsToReturn);
		}
		
		return numberOfEditableFixtures;
	}

	/**
	 * {@inheritDoc}
	 */
	public void getPredictionsForFutureFixtures(UserSubscription subscription,
				  							   List<Prediction> predictionsToReturn) {

		if (subscription != null) {
			// Get all the fixtures that haven't started yet.
			Sort sortOrder = new Sort(Direction.ASC, "matchTime");

			List<Fixture> fixtures = 
					fixtureRepository.findByCompetitionAndMatchTimeAfter(subscription.getLeague().getCompetition(),
																		systemClock.getCurrentDateTime(),
																		sortOrder);

			if (logger.isDebugEnabled()) {
				logger.debug("Got {} future fixtures", fixtures.size());
			}

			// Get any existing predictions for these fixtures, or create a dummy one if none exists.
			generatePredictionsFromFixtures(subscription.getUser(), fixtures, predictionsToReturn);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Prediction> getPredictionsInLeagueForFixture(League league, Fixture fixture) {
		List<Prediction> predictions = null;

		if (league != null && fixture != null) {
			predictions = predictionRepository.findFixturePredictionsFromLeague(league, fixture);
		}

		if (predictions == null) {
			predictions = new ArrayList<>();
		}
		
		return predictions;
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

			if (predictedResult != null) {
				prediction = new Prediction();
				
				prediction.setFixture(fixture);
				prediction.setPredictor(subscription.getUser());
				prediction.setPredictedResult(predictedResult);
				
				prediction = predictionRepository.save(prediction);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("New prediction is null so nothing to save.");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Found prediction ID {} for user ID {} and fixture ID {}",
							 prediction.getId(),
							 subscription.getUser().getId(),
							 fixture.getId());
			}

			if (predictedResult == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Deleting existing prediction ID {} for user ID {} and fixture ID {}",
								 prediction.getId(),
								 subscription.getUser().getId(),
								 fixture.getId());
				}
				
				predictionRepository.delete(prediction);
			} else {
				prediction.setPredictedResult(predictedResult);
			}
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
	 * Generate a list of predictions for the given list of fixtures. We're also given the
	 * user who is to make those prdictions. If there's already a prediction by that user
	 * then we return it. Otherwise we wrap the fixture in a fake one.
	 * 
	 * @param user the user who is to have made the predictions
	 * @param fixtures the fixtures we want to convert into predictions
	 * @param predictionsToReturn the predictions are added here
	 */
	private	void generatePredictionsFromFixtures(User user, List<Fixture> fixtures, List<Prediction> predictionsToReturn) {
		for (Fixture fixture : fixtures) {
			
			Prediction prediction = 
					predictionRepository.findByPredictorAndFixture(user, fixture);

			// If there's no real prediction, wrap the fixture in a fake one so that
			// the user can see they've yet to enter one.
			
			if (prediction == null) {
				prediction = new Prediction();
				
				prediction.setFixture(fixture);
				prediction.setPredictor(user);
			}

			predictionsToReturn.add(prediction);
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
	
	/**
	 * Get the results (i.e. completed fixtures) from the given competition.
	 * 
	 * @param competition the competition whose fixtures we're after
	 * @param recentOnly if true then only return the most recent results
	 * @return the competition's results
	 */
	private List<Fixture> getResults(Competition competition, boolean recentOnly) {
		// Get all the fixtures that have a result, ordered by match time. We're interested
		// in the most recent results so we sort by descending match time.
		Sort sortOrder = new Sort(Direction.DESC, "matchTime");
		
		List<Fixture> results = 
				fixtureRepository.findByCompetitionAndResultIsNotNull(competition,
																	 sortOrder);
	
		if (logger.isDebugEnabled()) {
			logger.debug("Got {} results", results.size());
		}

		if (recentOnly) {
			// Trim the list down to the desired number.
			results = getMostRelevant(results, minimumNumberOfFixturesToDisplay);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Got the {} most relevant results", results.size());
			}
		}
		
		return results;
	}
	
	/**
	 * Create prediction beans from the given set of fixtures.
	 * 
	 * @param results the fixtures whose predictions we want to fetch
	 * @param predictor the person who made all the predictions
	 * @return a list of prediction beans for the fixtures
	 */
	private List<Prediction> createPredictionBeansForResults(List<Fixture> results, User predictor) {
		List<Prediction> predictions = new ArrayList<>();

		for (Fixture fixture : results) {
			Prediction prediction = predictionRepository.findByPredictorAndFixture(predictor, fixture);
			
			if (prediction == null) {
				prediction = new Prediction();
				
				prediction.setFixture(fixture);
				prediction.setPredictor(predictor);
			}

			// Wrap the prediction in a bean that will allow us to test whether the match has started.
			PredictionBean bean = new PredictionBean(prediction);
			bean.setStarted(fixture.getMatchTime().isBefore(systemClock.getCurrentDateTime()));
			
			predictions.add(bean);
		}
		
		return predictions;
	}
}
