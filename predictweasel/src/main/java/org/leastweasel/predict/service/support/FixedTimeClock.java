/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import org.joda.time.DateTime;
import org.leastweasel.predict.service.Clock;

/**
 * An implementation of the {@link Clock} interface that always returns the
 * same value for the current time. Instances should be constructed with a
 * string representing that time. 
 */
public class FixedTimeClock implements Clock {
	private DateTime fixedTime;
	
	/**
	 * Constructor. 
	 * 
	 * @param fixedTimeStr a string representation of the time this
	 *        clock will always return
	 */
	public FixedTimeClock(String fixedTimeStr) {
		this.fixedTime = DateTime.parse(fixedTimeStr);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateTime getCurrentDateTime() {
		return fixedTime;
	}
}
