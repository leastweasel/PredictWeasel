/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import org.leastweasel.predict.domain.PasswordReset;
import org.leastweasel.predict.domain.User;

/**
 * An interface for dealing with users.
 */
public interface UserService {

    /**
     * Find whether a user exists that matches the given user's username exactly. Matches active and
     * inactive users, but disregards users with the same id as the given user.
     * 
     * @param user a user whose username is the username of the user to fetch
     * @return true if a user with the given username exists
     */
    boolean userForUsernameExists(User user);

    /**
     * Find whether a user exists that matches the given name exactly. Matches active and inactive
     * users, but disregards users with the same id as the given user.
     * 
     * @param user a user whose name is the name of the user to fetch
     * @return true if a user with the given name exists
     */
    boolean userForNameExists(User user);

    /**
     * Attempt to register the given user details.
     * 
     * @param user the user to register
     * @return the registered user, with its unique id set from the database
     */
    User registerUser(User user);

    /**
     * Attempt to save the given user details.
     * 
     * @param user the user to save
     */
    void saveUser(User user);

    /**
     * Create the object that will allow the user with the given email address to reset their
     * password.
     * 
     * @param username the username of the user who wants to change their password
     */
    PasswordReset createPasswordReset(String username);

    /**
     * Get the {@link PasswordReset} with the given token. Returns null if no such password reset exists.
     * 
     * @param token identifies the password reset to fetch
     * @return the password reset with the given token, or null
     */
    PasswordReset getPasswordReset(String token);
    
    /**
     * Reset a user's password.
     * 
     * @param reset details from the request to reset the password
     * @param newPassword the user's new password
     * @param newReminder the user's new password reminder
     */
    void resetPassword(PasswordReset reset, String newPassword, String newReminder);
}
