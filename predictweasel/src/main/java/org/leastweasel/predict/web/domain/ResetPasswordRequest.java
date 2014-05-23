/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.domain;

import static org.leastweasel.predict.domain.User.MIN_PASSWORD_LENGTH;

import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

/**
 * The command object used when a user resets their password and enters a new one.
 */
public class ResetPasswordRequest {
    private String password;

    private String confirmPassword;

    private String passwordReminder;
    
    /**
     * Get the user's new password.
     *  
     * @return the new password
     */
    @NotBlank
    @Size(min=MIN_PASSWORD_LENGTH)
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's new password. Removes external whitespace and replaces blank strings with
     * null.
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = StringUtils.trimToNull(password);
    }

    /**
     * Get the password reminder.
     * 
     * @return the password reminder
     */
    public String getPasswordReminder() {
        return passwordReminder;
    }

    public void setPasswordReminder(String passwordReminder) {
        this.passwordReminder = passwordReminder;
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
