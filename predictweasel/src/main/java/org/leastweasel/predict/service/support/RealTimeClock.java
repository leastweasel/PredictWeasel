/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import org.joda.time.DateTime;
import org.leastweasel.predict.service.Clock;

/**
 * An implementation of the {@link Clock} interface whose idea of the
 * current time matches the real world. 
 */
public class RealTimeClock implements Clock {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateTime getCurrentDateTime() {
		return new DateTime();
	}
}
