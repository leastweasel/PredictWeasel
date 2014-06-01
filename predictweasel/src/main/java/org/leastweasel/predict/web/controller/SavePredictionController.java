/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.domain.MatchResult;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.format.MatchResultFormatter;
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
 * Controller that allows a user to save a prediction. This is called, via a jQuery POST request, 
 * when the user tabs out of the prediction input field.
 */
@Controller
public class SavePredictionController {

	@Autowired
	private PredictionService predictionService;

	@Autowired
	private MatchResultFormatter matchResultFormatter;
	
    private static Logger logger = LoggerFactory.getLogger(SavePredictionController.class);

    @Autowired
    private MessageSource messageSource;
    
    /**
     * Submit the request to save the prediction. We return the formatted version of the updated
     * prediction so that we can display it in the form. 
      * 
	 * @param subscription the user and league for which we are receiving a prediction
     * @param fixture the fixture for which this is a prediction 
     * @param predictedResult the user's prediction for the fixture 
     * @return a map, which will be converted to a JSON object, containing the updated prediction text
      */
    @RequestMapping(value="/league/savePrediction", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Map<String, String> savePrediction(UserSubscription subscription,
								 			 @RequestParam(value = "fixture", required = true) Fixture fixture,
								 			 @RequestParam(value = "prediction", required = false) MatchResult predictedResult,
								 			 Locale locale) {

		if (fixture != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Got a prediction to save: fixture ID = {}, predictedResult = {}", fixture.getId(), predictedResult);
			}
			
			// Update the prediction for this fixture, or create a new one.
			predictionService.createOrUpdatePrediction(subscription, fixture, predictedResult);
		}

		Map<String, String> response = new HashMap<>();
		
		response.put("predictionText", matchResultFormatter.print(predictedResult, locale));
		
		return response;
	}
}
