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
import org.leastweasel.predict.service.Clock;
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
	
	@Autowired
	private Clock systemClock;
	
    private static Logger logger = LoggerFactory.getLogger(SavePredictionController.class);

    @Autowired
    private MessageSource messageSource;
    
    /**
     * Submit the request to save the prediction. We return the formatted version of the updated
     * prediction so that we can display it in the form.
     * <p>
     * Note that we deliberately pass in a String for the new prediction, rather than let Spring
     * convert it to a {@link MatchResult} instance. This works well if the entered text is well
     * formed, but causes an HTTP error status if it isn't. 
      * 
	  * @param subscription the user and league for which we are receiving a prediction
      * @param fixture the fixture for which this is a prediction 
      * @param predictedResultText the user's prediction for the fixture, exactly as they entered it 
      * @return a map, which will be converted to a JSON object, containing the updated prediction text and,
      * 			if the input is badly formed, an error message to display         
      */
    @RequestMapping(value="/league/savePrediction", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Map<String, String> savePrediction(UserSubscription subscription,
								 			 @RequestParam(value = "fixture", required = true) Fixture fixture,
								 			 @RequestParam(value = "predictionText", required = false) String predictedResultText,
								 			 Locale locale) {

		Map<String, String> response = new HashMap<>();

		try {
			MatchResult predictedResult = matchResultFormatter.parse(predictedResultText, locale);
			
			if (fixture != null) {
				if (!fixture.getMatchTime().isAfter(systemClock.getCurrentDateTime())) {
					response.put("errorText", "Sorry, the match has already started");
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Got a prediction to save: fixture ID = {}, predictedResult = {}", fixture.getId(), predictedResult);
					}
					
					// Update the prediction for this fixture, or create a new one.
					predictionService.createOrUpdatePrediction(subscription, fixture, predictedResult);
				}
			}
	
			response.put("predictionText", matchResultFormatter.print(predictedResult, locale));
			
		} catch(Exception e) {
			response.put("errorText", "Invalid prediction format: " + predictedResultText);
		}
		
		return response;
	}
}
