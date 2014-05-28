/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import javax.servlet.http.HttpSession;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.web.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that allows a user to navigate to the PredictWeasel home page. This will navigate to a
 * different page depending on whether the user is logged in or not, and whether the user is currently
 * playing in a {@link League} or not.
 */
@Controller
public class HomeController {
	/**
	 * Handle a GET request to navigate to the root URI. The league code in the user's session tells us
	 * what league the user is playing. If it's null (meaning there's no logged in user), or it's the 
	 * special value indicating that the user has no subscriptions, then we go to the landing page.
	 * <p>
	 * If it's the special value indicating that the user has multiple subscriptions, and hasn't chosen
	 * one yet, then we go to a page encouraging the player to pick one of the leagues.
	 * <p>
	 * If there's just one subscription, or the user has already chosen one, then we go to the main
	 * page for playing the game.     
	 * 
	 * @return the name of the view to navigate to
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String navigateToPage(HttpSession session) {

		String leagueCode = WebUtil.Session.getCurrentLeagueCode(session);
		
		if (leagueCode == null || leagueCode.equals(WebUtil.Session.NO_SUBSCRIPTIONS)) {
			return "landing";
		} else if (leagueCode == WebUtil.Session.MULTIPLE_SUBSCRIPTIONS) {
			return "redirect:/subscriptions";
		}
		
		return "redirect:/league/";
	}
}
