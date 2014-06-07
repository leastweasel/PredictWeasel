/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.domain;

import org.apache.commons.lang.StringUtils;
import org.leastweasel.predict.domain.User;

/**
 * The command object used for editing user details. This is based on the command object for
 * registering a user but has to handle the differences in changing passwords:
 * <ul>
 * <li>if the password fields are left blank then they don't want to change password</li>
 * <li>if they do want to change the password they have to enter the old value too.</li>
 * </ul>
 * 
 * So we store the new password here and only overwrite the one in the user object if the user
 * entered something.
 */
public class EditUserRequest extends UserRequest {
    private String oldPassword;
    private String newPassword;

    /**
     * Constructor.
     * 
     * @param userToEdit the user whose details are being edited
     */
    public EditUserRequest(User userToEdit) {
        super.setUser(userToEdit);
    }

    /**
     * Get the value for what the user claims is their old password, which will need to be
     * confirmed. The custom validation constraint will allow a value of null or a blank string.
     * Otherwise the value must be of a certain size.
     * 
     * @return the user's old password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Set the old password. Removes external whitespace and replaces blank strings with null.
     * We'll check this is correct before we allow the user to change to a new password. Note that
     * there are no validation constraints on this value. They're implied by the check against the
     * current password.
     * 
     * @param oldPassword the user's old password
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = StringUtils.trimToNull(oldPassword);
    }

    /**
     * Get the value for the user's new password. The custom validation constraint will allow a
     * value of null or a blank string. Otherwise the value must be of a certain size.
     * 
     * @return the user's old password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Set the new password. Removes external whitespace and replaces blank strings with null.
     * 
     * @param newPassword the user's intended new password
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = StringUtils.trimToNull(newPassword);
    }
}
