package org.leastweasel.predict.exception;

import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.UserSubscription;

/**
 * A {@link RuntimeException} indicating that a required {@link UserSubscription} could
 * not be resolved while processing a request. It's not the absence of a specific
 * subscription that's the problem. It's that there needs to be a 'current' 
 * {@link League} for this request and we're unable to determine what that is.
 */
public class NoUserSubscriptionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor. 
	 */
	public NoUserSubscriptionException() {
		super("Unable to determine current league");
	}
}
