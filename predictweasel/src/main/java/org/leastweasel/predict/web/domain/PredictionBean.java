package org.leastweasel.predict.web.domain;

import org.leastweasel.predict.domain.Prediction;

/**
 * A bean class representing a {@link Prediction}, but that decorates it in
 * a way that's of use to the view layer. 
 */
public class PredictionBean extends Prediction {
	private static final long serialVersionUID = 1L;

	private boolean started;
	
	/**
	 * Constructor.
	 * 
	 * @param prediction the prediction being decorated
	 */
	public PredictionBean(Prediction prediction) {
		super(prediction);
	}

	/**
	 * Has the fixture started.
	 * 
	 * @return true if the predicted fixture has started
	 */
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
}
