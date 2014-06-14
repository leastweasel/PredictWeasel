package org.leastweasel.predict.exception;

/**
 * A {@link RuntimeException} indicating that a certain fixture has not started.
 */
public class FixtureNotStartedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor. 
	 */
	public FixtureNotStartedException() {
		super("The fixture has not started yet");
	}
}
