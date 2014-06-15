/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.leastweasel.predict.domain.League;
import org.springframework.web.util.WebUtils;

/**
 * Utility functions for the web tier.
 */
public class WebUtil {
	/**
	 * Utility functions that interface with an HTTP servlet request.
	 */
	public static class Request {
		/**
		 * Name of the cookie holding the code of the player's current {@link League}.
		 */
	    private static final String CURRENT_LEAGUE_CODE = "currentLeagueCode";

		/**
		 * Get the value of the cookie holding the code of the user's current league. Returns null if
		 * there's no such cookie in the request.
		 * 
		 * @param request the servlet request in which the cookie might be present
		 * @return the value of the cookie, or null if it's not in the request
		 */
	    public static String getCurrentLeagueCodeFromCookie(HttpServletRequest request) {
	    	Cookie cookie = WebUtils.getCookie(request, CURRENT_LEAGUE_CODE);
	    	
	    	if (cookie == null) {
	    		return null;
	    	} else {
	    		return cookie.getValue();
	    	}
	    }
	}
}
