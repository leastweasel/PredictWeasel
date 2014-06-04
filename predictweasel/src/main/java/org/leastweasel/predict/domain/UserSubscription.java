/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A {@link League} that a {@link User} has subscribed to.
 */
@Entity
public class UserSubscription implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
    private League league;

    private User user;

    private int prizeOnePoints;
    
    private int prizeTwoPoints;
    
    private int prizeThreePoints;
    
    /**
     * Get the subscription's unique id (generated by its data source). Will be null if not persistent.
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the league to which the user has subscribed.
     * 
     * @return the subscribed to league
     */
    @ManyToOne
    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
		this.league = league;
	}

	/**
     * Get the user this subscription is for.
     */
    @ManyToOne
    public User getUser() {
        return user;
    }

	public void setUser(User user) {
		this.user = user;
	}

    public int getPrizeOnePoints() {
		return prizeOnePoints;
	}

	public void setPrizeOnePoints(int prizeOnePoints) {
		this.prizeOnePoints = prizeOnePoints;
	}

	public int getPrizeTwoPoints() {
		return prizeTwoPoints;
	}

	public void setPrizeTwoPoints(int prizeTwoPoints) {
		this.prizeTwoPoints = prizeTwoPoints;
	}

	public int getPrizeThreePoints() {
		return prizeThreePoints;
	}

	public void setPrizeThreePoints(int prizeThreePoints) {
		this.prizeThreePoints = prizeThreePoints;
	}

	/**
	 * Get the number of points scored in the prize with the given index.
	 * Convenience method as prizes aren't stored in an array or
	 * collection of some sort.
	 * 
	 * @param prizeNumber the index number of the prize whose points we want
	 * @return the number of points scored for that prize
	 */
	@Transient
	public int getPrizePoints(int prizeNumber) {
		switch(prizeNumber) {
		case 1:
			return getPrizeOnePoints();
		case 2:
			return getPrizeTwoPoints();
		case 3:
			return getPrizeThreePoints();
		default:
			throw new IllegalArgumentException("Prize number index out of range(1-3): " + prizeNumber);
		}
	}
	
	/**
     * Comparison operator.
     * 
     * @param o the object being compared
     * @return true if the objects are the same
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!(other instanceof UserSubscription)) {
            return false;
        }

        final UserSubscription subscription = (UserSubscription) other;

        return new EqualsBuilder().append(id, subscription.id)
                                  .isEquals();
    }

    /**
     * Generate a hash code for this object. To be consistent with the <code>equals</code> method,
     * only the ID is included in the calculation.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                                    .toHashCode();
    }

}
