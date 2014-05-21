/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that allows a user to navigate to the PredictWeasel home page. This will navigate to a
 * different page depending on whether the user is logged in or not. When logged in the user goes to
 * the home page proper, containing details specific to the user and the leagues he or she is involved
 * in. If not logged in then the user is taken to the landing page which contains impersonal
 * information.
 * <p>
 * TODO: navigation to pages other than the landing page. 
 */
@Controller
public class HomeController {
	/**
	 * Handle a GET request to navigate to the root URI.
	 * 
	 * @return the name of the view to navigate to
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String navigateToPage() {
		return "landing";
	}
}
