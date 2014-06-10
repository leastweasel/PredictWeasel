package org.leastweasel.predict.web.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.Prediction;
import org.leastweasel.predict.domain.PrizePoints;

/**
 * Represents a {@link Prediction} from the point of view of the fixture
 * predictions screen. A prediction is for a fixture and isn't tied to a
 * {@link League}. But the fixture prediction screen IS tied to a league
 * and shows the number of points scored by the prediction in that league. 
 */
public class FixturePredictionBean extends Prediction implements Comparable<FixturePredictionBean> {
	private static final long serialVersionUID = 1L;

	private List<Integer> pointsPerPrize = new ArrayList<>();
	
	/**
	 * Constructor.
	 * 
	 * @param prediction the prediction being decorated
	 */
	public FixturePredictionBean(Prediction prediction) {
		super(prediction);
	}

	/**
	 * Add the number of points scored for a given prize.
	 * 
	 * @param points the number of points scored
	 */
	public void addPointsScored(PrizePoints points) {
		if (points != null) {
			pointsPerPrize.add(points.getPointsScored());
		} else {
			pointsPerPrize.add(0);
		}
	}

	/**
	 * Get the points scored by this prediction for each prize category.
	 * 
	 * @return the points scored
	 */
	public List<Integer> getPointsPerPrize() {
		return pointsPerPrize;
	}

	/**
	 * Compare two beans. The natural sort order for this bean is by the number of points
	 * scored in the first prize category, with the highest number of points first. Ties
	 * are then sorted by player name.
	 * 
	 * @param other the other bean to compare against
	 * @return the usual return value for a compare method
	 */
	public int compareTo(FixturePredictionBean other) {
		return new CompareToBuilder().append(other.pointsPerPrize.get(0), pointsPerPrize.get(0))
									 .append(getPredictor().getName(), other.getPredictor().getName())
									 .toComparison();
	}
}
