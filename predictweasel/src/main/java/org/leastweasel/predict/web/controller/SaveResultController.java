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
import org.leastweasel.predict.format.MatchResultFormatter;
import org.leastweasel.predict.service.CompetitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller that allows an administrative user to save the result of a fixture.
 * This is called, via a jQuery POST request, when the user tabs out of the result
 * input field.
 */
@Controller
public class SaveResultController {

	@Autowired
	private CompetitionService competitionService;

	@Autowired
	private MatchResultFormatter matchResultFormatter;
	
    private static Logger logger = LoggerFactory.getLogger(SaveResultController.class);

    /**
     * Submit the request to save the result. We return the formatted version of the updated
     * result so that we can display it in the form.
     * <p>
     * Note that we deliberately pass in a String for the new result, rather than let Spring
     * convert it to a {@link MatchResult} instance. This works well if the entered text is well
     * formed, but causes an HTTP error status if it isn't. 
      * 
      * @param fixture the fixture for which this is the result 
      * @param resultText the result of the fixture, exactly as the user entered it 
      * @return a map, which will be converted to a JSON object, containing the updated result text and,
      * 			if the input is badly formed, an error message to display         
      */
    @RequestMapping(value="/competition/saveResult", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Map<String, String> saveResult(@RequestParam(value = "fixture", required = true) Fixture fixture,
								 		 @RequestParam(value = "resultText", required = false) String resultText,
								 		 Locale locale) {

		Map<String, String> response = new HashMap<>();

		try {
			MatchResult matchResult = matchResultFormatter.parse(resultText, locale);
			
			if (fixture != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Got a fixture to save: fixture ID = {}, result = {}", fixture.getId(), matchResult);
				}
				
				// Set the result for this fixture.
				competitionService.saveResult(fixture, matchResult);
			}
	
			response.put("resultText", matchResultFormatter.print(matchResult, locale));
			
		} catch(Exception e) {
			response.put("errorText", "Invalid result format: " +resultText);
		}
		
		return response;
	}
}
