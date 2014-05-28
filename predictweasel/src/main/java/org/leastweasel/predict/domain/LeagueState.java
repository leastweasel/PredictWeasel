/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

/**
 * Indicates the state of a league. Leagues can be in one of three states.
 */
public enum LeagueState {
	/**
	 * Open to new subscribers, may have already started.
	 */
    OPEN,
    
    /**
     * No new subscribers but league may still be being played or just viewed.
     */
    ACTIVE,
    
    /**
     * Not ready for subscribers or long past its end date
     */
    CLOSED;
}
