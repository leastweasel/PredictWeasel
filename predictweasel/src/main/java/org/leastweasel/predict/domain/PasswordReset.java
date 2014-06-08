/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * A request to reset a user's password. As user passwords are encrypted it isn't possible to send
 * plain text reminders (not that that would be a good idea anyway). So a user can request a
 * password reminder, which will include the user's password reminder if they've defined one. Either
 * way, the reminder email will contain a link to a password reset page. Part of that request will
 * be a key to an instance of this class.
 * <p>
 * Password resets are time limited and can only be used once, as they may be sitting in a user's
 * in box for any amount of time.
 * <p>
 * <i>This class maps to the PASSWORD_RESET table in the database</i>.
 */
@Entity
public class PasswordReset {
    private Long id;

    private DateTime expiryDate;
    
    private String token;
    
    private DateTime usedDate;

    private User user = new User();
    
    /**
     * Get the unique id. Note that this is the primary database key and not the value that will be
     * sent to the user.
     *
     * @return the unique id
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Set the unique id.
     *
     * @param id the unique id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the user for whom this reset request has been made. The user will have identified his or
     * herself by their username/email address.
     * <p>
     * Note that we need the fetch method on the user to remain eager (the default) so don't make
     * it lazy.
     *  
     * @return the user who may want to reset their password
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    /**
     * Get the user for whom this reset request has been made.
     * 
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the date and time at which this request expires. After this time the request will have no
     * effect.
     * 
     * @return the expiry date and time
     */
    @Convert(converter = DateTimeToDateConverter.class)
    public DateTime getExpiryDate() {
        return expiryDate;
    }

    /**
     * Set the expiry date and time.
     * 
     * @param expiryDate the expiry date to set
     */
    public void setExpiryDate(DateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Return the token that identifies this request in the real world. This is the value that will
     * appear in the reset request link.
     * 
     * @return the token
     */
    @Column(unique = true)
    public String getToken() {
        return token;
    }

    /**
     * Set the request token.
     * 
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get the date on which the reset request was used. Once this has been set then the request
     * cannot be used again.
     * 
     * @return the used date
     */
    @Convert(converter = DateTimeToDateConverter.class)
    public DateTime getUsedDate() {
        return usedDate;
    }

    /**
     * Set the date on which the reset request was used.
     * 
     * @param usedDate the used date to set
     */
    public void setUsedDate(DateTime usedDate) {
        this.usedDate = usedDate;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the business key values.
     *
     * @param other the object to compare
     * @return boolean true if the business keys are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!this.getClass().equals(other.getClass())) {
            return false;
        }

        PasswordReset reset = (PasswordReset) other;
        
        return new EqualsBuilder().append(token, reset.token)
                                  .isEquals();
    }
    
    /**
     * Generate a hash code for this object.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(token)
                                    .toHashCode();
    }

    /**
     * Format the object as a String.
     *
     * @return the generated String
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ID", id)
                                        .append("Token", token)
                                        .append("Expiry date", expiryDate)
                                        .append("Used date", usedDate)
                                        .append("User ID", user.getId())
                                        .toString();
    }
}

