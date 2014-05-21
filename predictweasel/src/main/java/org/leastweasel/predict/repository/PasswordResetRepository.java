/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import org.leastweasel.predict.domain.PasswordReset;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@link org.leastweasel.predict.domain.PasswordReset PasswordReset} objects.
 */
public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {
    /**
     * Find a password reset that matches the given token.
     *
     * @param token the token of the password reset to fetch
     * @return a PasswordReset (or null if none match)
     */
    PasswordReset findByToken(String token);
}
