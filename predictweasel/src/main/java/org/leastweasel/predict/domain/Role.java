/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A role that can be assigned to users of the system, granting that user access to a part of the
 * site's functionality. More than one role can be assigned to a user.
 * <p>
 * <i>This class maps to the ROLE table in the database</i>.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Role implements Comparable<Role>, Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

    private String name;

    private String description;

    private String grantedAuthority;

    /**
     * The authority granted to all users once they've logged in. This simplifies the authorisation
     * configuration.
     */
    public static final String USER_AUTHORITY = "ROLE_USER";

    /**
     * Default constructor.
     */
    public Role() {
    }

    /**
     * Constructor with granted authority. Useful for testing.
     *
     * @param grantedAuthority the authority granted if a user has this role
     */
    public Role(String grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
    }

    /**
     * Constructor.
     *
     * @param name the name of the role
     * @param grantedAuthority the granted authority
     * @param description some descriptive text
     */
    public Role(String name, String grantedAuthority, String description) {
        this.name = name;
        this.grantedAuthority = grantedAuthority;
        this.description = description;
    }

    /**
     * Get the descriptive text.
     *
     * @return the descriptive text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the descriptive text.
     *
     * @param description the descriptive text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the object's unique id.
     *
     * @return the unique id
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Set the object's unique id.
     *
     * @param id the unique id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the granted authority, a string beginning with "ROLE_". This string determines whether a
     * user will have access to a particular resource, be it a URL or domain object.
     *
     * @return the granted authority
     */
    public String getGrantedAuthority() {
        return grantedAuthority;
    }

    /**
     * Set the granted authority. This is the value of the role as far as the security system is
     * concerned. It should be used whenever a permission is required to access a particular
     * resource.
     *
     * @param grantedAuthority the granted authority
     */
    public void setGrantedAuthority(String grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
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

        if (getClass() != other.getClass()) {
            return false;
        }

        Role that = (Role) other;

        return new EqualsBuilder().append(grantedAuthority, that.grantedAuthority).isEquals();
    }

    /**
     * Generate a hash code for this object.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(grantedAuthority).toHashCode();
    }

    /**
     * Compare this role with another. Returns the result of the comparison between respective
     * granted authorities.
     *
     * @param role the role to compare to
     * @return -1 if this role is less than the other, 1 if it's greater, 0 otherwise
     */
    public int compareTo(Role role) {
        return new CompareToBuilder().append(grantedAuthority, role.grantedAuthority)
                                     .toComparison();
    }

    /**
     * Format the object as a String.
     *
     * @return the generated String
     */
    @Override
    public String toString() {
        return grantedAuthority;
    }
}
