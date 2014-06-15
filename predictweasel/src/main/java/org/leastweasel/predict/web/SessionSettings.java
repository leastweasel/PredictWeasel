package org.leastweasel.predict.web;

/**
 * A session-scoped bean that holds various settings about the user.
 */
public class SessionSettings {
	private String currentLeagueCode;
	
	private boolean leagueAdmin;
	
	private boolean multipleSubscriptions;

	/**
	 * Get the code of the user's current league.
	 * 
	 * @return the user's current league's code
	 */
	public String getCurrentLeagueCode() {
		return currentLeagueCode;
	}

    /**
     * Set the code of the current league.
     * 
     * @param currentLeagueCode the current league's code
     */
	public void setCurrentLeagueCode(String currentLeagueCode) {
		this.currentLeagueCode = currentLeagueCode;
	}

	/**
	 * Is the current user an administrator for the league they're currently playing.
	 * 
	 * @return true if the user is an administrator for the current league
	 */
	public boolean isLeagueAdmin() {
		return leagueAdmin;
	}

	/**
	 * Set whether the user is an administrator for the league they're playing.
	 * 
	 * @param leagueAdmin true if the user is an administrator of the current league 
	 */
	public void setLeagueAdmin(boolean leagueAdmin) {
		this.leagueAdmin = leagueAdmin;
	}

	/**
	 * Does the user have more than one subscription to a league.
	 * 
	 * @return true if the user has more than one subscription
	 */
	public boolean getHasMultipleSubscriptions() {
		return multipleSubscriptions;
	}

	/**
	 * Set whether the user has more than one subscription to a league.
	 * 
	 * @param multipleSubscriptions true if the user has more than one subscription
	 */
	public void setHasMultipleSubscriptions(boolean multipleSubscriptions) {
		this.multipleSubscriptions = multipleSubscriptions;
	}
	
	/**
	 * Indicate that the user has no current subscription (possibly because he or she
	 * has no subscriptions at all). 
	 */
	public void noLeague() {
		setCurrentLeagueCode(null);
		setLeagueAdmin(false);
	}
}
