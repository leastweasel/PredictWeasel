/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.domain;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.leastweasel.predict.domain.User;

/**
 * The command object used during user-related actions.
 */
public class UserRequest {
    private User user = new User();
    private String confirmPassword;

    /**
     * Get details of the user involved in the request. The @Valid annotation means that when
     * instances of this class are validated, the validation will cascade down to the user object
     * too.
     */
    @Valid
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the confirmation of the user's password. This is only retrieved for validation: it is
     * not persisted. 
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Set the confirmation of the user's password. Removes external whitespace and replaces blank
     * strings with null.
     * 
     * @param confirmPassword the password confirmation
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = StringUtils.trimToNull(confirmPassword);
    }
}
