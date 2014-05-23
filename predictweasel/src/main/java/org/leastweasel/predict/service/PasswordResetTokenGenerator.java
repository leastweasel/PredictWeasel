/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import org.leastweasel.predict.domain.User;

/**
 * Generate a token for use in a password reset. This token must be unique.
 */
public interface PasswordResetTokenGenerator {
    /**
     * Generate a token for the given user.
     * 
     * @param user the user for whom the token must be generated
     * @return a token string
     */
    String generateToken(User user);
}
