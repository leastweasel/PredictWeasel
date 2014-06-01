/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.MatchResult;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller that allows a user to save a prediction. This is called as a JSON
 * request when the user tabs out of the prediction input field.
 */
@Controller
@RequestMapping(value="/league/savePrediction")
public class SavePredictionController {

	@Autowired
	private PredictionService predictionService;
	
    private static Logger logger = LoggerFactory.getLogger(SavePredictionController.class);

    @Autowired
    private MessageSource messageSource;
    
    /**
     * Submit the request to save the prediction.
      * 
	 * @param subscription the user and league for which we are receiving a prediction
     * @param fixtureId the 
     * @param predictedResult the user's prediction for the fixture 
     * @return ...nothing yet...
      */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String savePrediction(UserSubscription subscription,
								 @RequestParam(value = "fixture", required = true) Fixture fixture,
								 @RequestParam(value = "prediction", required = false) MatchResult predictedResult) {

		if (fixture != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Got a prediction to save: fixture ID = {}, predictedResult = {}", fixture.getId(), predictedResult);
			}
			
			// Update the prediction for this fixture, or create a new one.
			predictionService.createOrUpdatePrediction(subscription, fixture, predictedResult);
		}
		
		return "";
	}
}
