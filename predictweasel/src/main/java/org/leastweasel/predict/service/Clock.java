/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import org.joda.time.DateTime;

/**
 * An interface for getting the current time. Implementations might, for example,
 * return the actual current time or a fixed (or even elapsed) time. The latter
 * would be very useful in a test or development environment. 
 */
public interface Clock {
	/**
	 * Get the current date and time as a Joda {@ link DateTime}.
	 * 
	 * @return a Joda {@ link DateTime} instance representing 'now'
	 */
	DateTime getCurrentDateTime();
}
