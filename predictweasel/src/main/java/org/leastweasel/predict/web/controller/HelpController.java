/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import org.leastweasel.predict.domain.League;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that allows a user to navigate to the PredictWeasel home page. This will navigate to a
 * different page depending on whether the user is logged in or not, and whether the user is currently
 * playing in a {@link League} or not.
 */
@Controller
public class HelpController {
	/**
	 * Handle a GET request to view a help page.     
	 * 
	 * @return the name of the view to navigate to (one of the help pages)
	 */
	@RequestMapping(value="/help/{page}", method = RequestMethod.GET)
	public String navigateToPage(@PathVariable String page) {

		return "help_" + page;
	}
}
