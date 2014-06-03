/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A league, comprising a group of {@link User}s making predictions on the outcome of fixtures in a competition.
 * The players in a league can be competing for a number of prizes within the league. As well as its name, a
 * league is primarily identified by its code, which is automatically, and randomly, generated by the system
 * when the league is created. This value can be used by players to join a league if they do not have a
 * direct link to hand.
 * <p>
 * <i>This class maps to the LEAGUE table in the database</i>.
 */
@Entity
public class League implements Serializable {
	private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String code;

    private String prizeOneCode;
    
    private String prizeTwoCode;
    
    private String prizeThreeCode;
    
    private LeagueState state;
    
    private Competition competition;
    
    private User owner;

    private Set<UserSubscription> subscriptions;
    
    /**
     * Get the league's unique id.
     *
     * @return the league's unique id
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Set the league's unique id.
     *
     * @param id the league's unique id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the league's name.
     *
     * @return the league's name
     */
    @Column(unique = true)
    @NotNull
    @Size(min=1, max=30)
    public String getName() {
        return name;
    }

    /**
     * Set the league's name. Removes external whitespace and replaces blank strings with null.
     *
     * @param name the league's name
     */
    public void setName(String name) {
        this.name = StringUtils.trimToNull(name);
    }

    /**
     * Get the league's code.
     *
     * @return the league's code
     */
    @Column(unique = true)
    @NotNull
    public String getCode() {
        return code;
    }

    /**
     * Set the league's code. Removes external whitespace and replaces blank strings with null.
     *
     * @param code the league's code
     */
    public void setCode(String code) {
        this.code = StringUtils.trimToNull(code);
    }

    /**
     * Get the competition to which this game is connected.
     * 
     * @return the associated competition
     */
    @ManyToOne
    @JoinColumn(name = "competition_id")
    public Competition getCompetition() {
    		return competition;
    }

    /**
     * Set the competition for whose fixtures the players are providing predictions.
     *
     * @param competition the competition
     */
    public void setCompetition(Competition competition) {
		this.competition = competition;
	}

    /**
     * Get the {@link User} who created the league.
     * 
     * @return the creating user
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
	public User getOwner() {
		return owner;
	}

	/**
	 * Set the user who created the league.
	 * 
	 * @param owner the creating user
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * Get the collection of users who have subscribed to this League.
	 * 
	 * @return the league's subscribers
	 */
	@OneToMany(mappedBy="league")
	public Set<UserSubscription> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * Set the collection of subscriptions to this league.
	 * 
	 * @param subscriptions the new subscriptions
	 */
	public void setSubscriptions(Set<UserSubscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

    /**
     * Is this a public game that anyone play.
     * 
     * @return true if the game is public
    public boolean isPublic();
     */

	/**
     * Is this game open.
     * 
     * @return true if the game is open to new subscribers. Even if a game is not open it could well
     *         be still active, it's just that no new players can join.
     */
    @Transient
    public boolean isOpen() {
    	return state == LeagueState.OPEN;
    }

    /**
     * Is this game active.
     * 
     * @return true if the game is visible but not open to new subscribers.
     */
    @Transient
    public boolean isActive() {
    	return state == LeagueState.ACTIVE;
    }

    /**
     * Get the game's state.
     * 
     * @return the state of the game
     */
    @Enumerated(EnumType.STRING)
    public LeagueState getState() {
    	return state;
    }
    
    /**
     * Set the game's state.
     * 
     * @param state the state of the game
     */
    public void setState (LeagueState state) {
    	this.state = state;
    }

    /**
     * Get the code of this league's main prize. Will not be null.
     *  
     * @return the main prize's code
     */
    @NotBlank
    public String getPrizeOneCode() {
		return prizeOneCode;
	}

	public void setPrizeOneCode(String prizeOneCode) {
		this.prizeOneCode = prizeOneCode;
	}

    /**
     * Get the code of this league's secondary prize. May be null.
     *  
     * @return the secondary prize's code
     */
	public String getPrizeTwoCode() {
		return prizeTwoCode;
	}

	public void setPrizeTwoCode(String prizeTwoCode) {
		this.prizeTwoCode = prizeTwoCode;
	}

    /**
     * Get the code of this league's tertiary prize. May be null.
     *  
     * @return the tertiary prize's code
     */
	public String getPrizeThreeCode() {
		return prizeThreeCode;
	}

	public void setPrizeThreeCode(String prizeThreeCode) {
		this.prizeThreeCode = prizeThreeCode;
	}

	/**
     * Equality operator.
     *
     * @param other the object we're comparing against
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!(other instanceof League)) {
            return false;
        }

        final League league = (League) other;

        return new EqualsBuilder().append(id, league.id).isEquals();
    }
    
    /**
     * Generate a hash code for this object.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    /**
     * Get the description of the game. This is some welcoming text that is displayed to the user so
     * will be HTML.
     * 
     * @return the description of the game
    public String getDescription();
     */

    /**
     * Set the game's description.
     * 
     * @param description the description
    public void setDescription(String description) {
    	this.de
    }
     */

    /**
     * Get the game's message of the day.
     * 
     * @return the message of the day
    public GameMessage getMessageOfTheDay();
     */

    /**
     * Set the game's message of the day.
     * 
     * @param message the message of the day
    public void setMessageOfTheDay(GameMessage message);
     */
}
