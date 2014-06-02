/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.leastweasel.predict.service.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the {@link Clock} interface that is part way
 * between a {@link RealTimeClock} and a {@link FixedTimeClock}. The clock
 * maintains the time that has elapsed since it was constructed. When asked
 * for the current time the clock adds this variable part to a fixed time
 * also specified at construction.
 * <p> 
 * Instances should be constructed with a string representing the fixed time part. 
 */
public class ElapsedTimeClock implements Clock {
	private DateTime fixedTime;
	
	private DateTime constructionTime;
	
	private static final Logger logger = LoggerFactory.getLogger(ElapsedTimeClock.class);
	
	/**
	 * Constructor. 
	 * 
	 * @param fixedTimeStr a string representation of the time this
	 *        clock will always return
	 */
	public ElapsedTimeClock(String fixedTimeStr) {
		this.constructionTime = new DateTime();
		this.fixedTime = DateTime.parse(fixedTimeStr);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateTime getCurrentDateTime() {
		Duration elapsedTimeSinceStartup = new Duration(constructionTime, DateTime.now());
		
		DateTime now = fixedTime.plus(elapsedTimeSinceStartup);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Current time is: {}", now);
		}
		
		return now;
	}
}
