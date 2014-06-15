package org.leastweasel.predict.web;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * A possibly unnecessary class that gives us access to the session scoped Spring bean
 * containing a user's session settings. Because the settings bean is marked as an
 * AOP proxy we can be sure that injecting into a class (like this) is guaranteed to
 * give us the settings from the user's session. But it's not no clear whether that's
 * supposed to work when using a SpEL expression containing a reference to the
 * settings @{code e.g. &x64;sessionSettings}. Using this bean gives us more concise
 * expressions in a Thymeleaf page, anyway.    
 */
public class SessionAccess {
	@Autowired
	private SessionSettings sessionSettings;

	/**
	 * Is the user playing a league. Not only must the user have at least one subscription they
	 * must have selected one if they have several.
	 * 
	 * @return true if the user is currently playing a league
	 */
	public boolean getHasLeague() {
		return sessionSettings != null && sessionSettings.getCurrentLeagueCode() != null;
	}
	
	/**
	 * Is this user an administrator for the league they're currently playing.
	 * 
	 * @return true if the user is a league administrator
	 */
	public boolean getIsLeagueAdmin() {
		return sessionSettings != null && sessionSettings.isLeagueAdmin();
	}
	
	/**
	 * Get all of the session settings.
	 * 
	 * @return the session-scoped sessions bean
	 */
	public SessionSettings getSettings() {
		return sessionSettings;
	}
}
