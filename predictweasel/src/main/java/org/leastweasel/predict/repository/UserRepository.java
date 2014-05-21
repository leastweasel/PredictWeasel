/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import org.leastweasel.predict.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@link org.leastweasel.predict.domain.User User} objects.
 */
public interface UserRepository extends CrudRepository<User, Long>{
    /**
     * Find a user that matches the given username exactly.
     *
     * @param username the username of the user to fetch
     * @return a User (or null if none match)
     */
    User findByUsername(String username);

    /**
     * Find a user that matches the given name exactly.
     *
     * @param name the name of the user to fetch
     * @return a User (or null if none match)
     */
    User findByName(String name);
}
