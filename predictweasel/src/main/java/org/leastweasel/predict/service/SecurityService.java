/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import org.leastweasel.predict.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Security-related methods. This interface extends Spring Security's 
 * {@link UserDetailsService} so that it can be used by the authentication
 * and remember-me features.
 */
public interface SecurityService extends UserDetailsService {
    /**
     * Get the currently logged-in user, or null if the user isn't logged-in.
     *
     * @return the currently logged-in user
     */
    User getLoggedInUser();

    /**
     * Manually log in a user, after they've registered, for example.
     *
     * @param user the user to log in
     */
    void loginUser(User user);

    /**
     * Encrypt the given user's plain text password. The result is stored in the user's password.
     * 
     * @param user the user whose password is to be encrypted
     */
    void encryptPassword(User user);

    /**
     * Verify that the given plain text password matches the encrypted version.
     * 
     * @param plainTextPassword the plain text version to verify
     * @param encryptedPassword the encrypted version that it should match
     * @return true if the plain text version, after encryption, matches the other
     */
    boolean verifyPasswordsMatch(String plainTextPassword, String encryptedPassword);
}
