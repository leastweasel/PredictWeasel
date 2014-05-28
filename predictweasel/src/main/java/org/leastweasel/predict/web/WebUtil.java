/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.leastweasel.predict.domain.League;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

/**
 * Utility functions for the web tier.
 */
public class WebUtil {
	/**
	 * Prefix to attach to all session attribute names.
	 */
	private static final String LEASTWEASEL_PREFIX = "org.leastweasel.predict.web.";

	/**
	 * Utility functions that interface with an HTTP session.
	 */
	public static class Session {
		/**
		 * Session attribute holding the code of the player's current {@link League}.
		 */
	    private static final String CURRENT_LEAGUE_CODE = LEASTWEASEL_PREFIX + "currentLeagueCode";

	    /**
	     * Value of <code>CURRENT_LEAGUE_CODE</code> indicating that the user has no
	     * subscriptions from which to choose. Note the leading underscore, which marks
	     * it as not being a real league code.
	     */
	    public static final String NO_SUBSCRIPTIONS = "_NO_SUBSCRIPTIONS"; 
	    
	    /**
	     * Value of <code>CURRENT_LEAGUE_CODE</code> indicating that the user has multiple
	     * subscriptions from which to choose. Note the leading underscore, which marks
	     * it as not being a real league code.
	     */
	    public static final String MULTIPLE_SUBSCRIPTIONS = "_MULTI_SUBSCRIPTIONS"; 
	    
	    /**
	     * Set the code of the current league.
	     * 
	     * @param session the session in which the object should be stored
	     * @param leagueCode the current league's code
	     */
	    public static void setCurrentLeagueCode(HttpSession session, String leagueCode) {
	    	if (StringUtils.isBlank(leagueCode)) {
	    		session.removeAttribute(CURRENT_LEAGUE_CODE);
	    	} else {
	    		session.setAttribute(CURRENT_LEAGUE_CODE, leagueCode);
	    	}
	    }

	    /**
	     * Get the code of the user's current league. This variation is passed the session object
	     * in which the league code is stored.
	     * 
	     * @param session the session in which the object is stored
	     * @return the code of the user's current league
	     */
	    public static String getCurrentLeagueCode(HttpSession session) {
	        return (String) session.getAttribute(CURRENT_LEAGUE_CODE);
	    }

	    /**
	     * Get the code of the user's current league. This variation is passed a {@link WebRequest}, from
	     * which we can fetch the associated session's attributes.
	     * 
	     * @param webRequest from which we can retrieve session attributes
	     * @return the code of the user's current league
	     */
	    public static String getCurrentLeagueCode(WebRequest webRequest) {
	        return (String) webRequest.getAttribute(CURRENT_LEAGUE_CODE, WebRequest.SCOPE_SESSION);
	    }
	}
	
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
