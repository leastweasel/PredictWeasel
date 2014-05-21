/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import javax.transaction.Transactional;

import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.repository.UserRepository;
import org.leastweasel.predict.service.SecurityService;
import org.leastweasel.predict.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UserRepository userRepository;
	
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    /**
     * {@inheritDoc}
     */
    public boolean userForUsernameExists(User user) {
        if (user == null || user.getUsername() == null) {
            return false;
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("Checking for user with username \"{}\"", user.getUsername());
        }
        	
    	User existingUser = userRepository.findByUsername(user.getUsername());
    	boolean found = false;
    	
    	if (existingUser != null) {
    		// Ignore the existing user if we're updating the same user.
    		if (user.getId() == null || !user.getId().equals(existingUser.getId())) {
    			found = true;
    		}
    	}

        if (logger.isDebugEnabled()) {
        	logger.debug("Found user with username {}: {}", user.getUsername(), found);
        }
        	
    	return found;
    }

    /**
     * {@inheritDoc}
     */
    public boolean userForNameExists(User user) {
        if (user == null || user.getName() == null) {
            return false;
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("Checking for user with name \"{}\"", user.getName());
        }
        	
    	User existingUser = userRepository.findByName(user.getName());
    	boolean found = false;
    	
    	if (existingUser != null) {
    		// Ignore the existing user if we're updating the same user.
    		if (user.getId() == null || !user.getId().equals(existingUser.getId())) {
    			found = true;
    		}
    	}

        if (logger.isDebugEnabled()) {
        	logger.debug("Found user with name {}: {}", user.getName(), found);
        }
    	
    	return found;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public User registerUser(User user) {
    	if (logger.isDebugEnabled()) {
            logger.debug("Registering user with username: \"{}\"", user.getUsername());
    	}
    	
    	// We should only ever be registering new users.
        if (user.getId() != null) {
            throw new IllegalArgumentException("Attempting to register a user that is "
                                               + "already persistent: " + user.getId());
        }

        // Encrypt the plain text password.
        securityService.encryptPassword(user);
        
        // Insert the object.
        User insertedUser = userRepository.save(user);

    	if (logger.isDebugEnabled()) {
    		logger.debug("Inserted user, ID: {}", insertedUser.getId());
    	}
    	
        // Login the user.
        securityService.loginUser(insertedUser);

        return insertedUser;
    }
}