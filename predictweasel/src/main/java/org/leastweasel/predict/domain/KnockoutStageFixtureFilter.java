/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

/**
 * a {@link FixtureFilter} that only accepts knock-out fixtures  
 */
public class KnockoutStageFixtureFilter implements FixtureFilter {
	/**
	 * Should we accept this {@link Fixture}. Yes if it's a knock-out fixture.
	 * 
	 * @param fixture the fixture to test
	 * @return true if the fixture is in the knock-out stage of the competition
	 */
	@Override
	public boolean accept(Fixture fixture) {
		return fixture.isKnockoutFixture();
	}
}
