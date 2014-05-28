/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

/**
 * Determines whether a {@link Fixture} is to be included in some calculation or
 * procedure. 
 */
public interface FixtureFilter {
	/**
	 * Should we accept the given fixture.
	 * 
	 * @param fixture the fixture to check
	 * @return true if we should accept the fixture
	 */
	boolean accept(Fixture fixture);
}
