/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.List;

import org.joda.time.DateTime;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.LeagueState;
import org.leastweasel.predict.domain.PasswordReset;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.repository.LeagueRepository;
import org.leastweasel.predict.repository.PasswordResetRepository;
import org.leastweasel.predict.repository.UserRepository;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.PasswordResetTokenGenerator;
import org.leastweasel.predict.service.SecurityService;
import org.leastweasel.predict.service.SubscriptionService;
import org.leastweasel.predict.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private LeagueRepository leagueRepository;
	
    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private PasswordResetTokenGenerator passwordResetTokenGenerator;
    
    @Autowired
    private Clock systemClock;
    
    @Value("${predictWeasel.passwordResetExpiryIntervalDays}")
    private int passwordResetExpiryIntervalInDays;
    
    @Value("${predictWeasel.allowAutoSubscription}")
    private boolean allowAutoSubscription;
    
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
    	
        // If there's only one open league, and we're configured to allow auto-subscription,
        // then automatically subscribe the user to that league.
        
        if (allowAutoSubscription && leagueRepository.countByState(LeagueState.OPEN) == 1) {
        	
        	List<League> openLeagues = leagueRepository.findByState(LeagueState.OPEN);
        	League leagueToSubscribeTo = (openLeagues != null && !openLeagues.isEmpty() ? openLeagues.get(0) : null);
        	
        	if (leagueToSubscribeTo != null) {
        		UserSubscription subscription = 
        				subscriptionService.createSubscription(leagueToSubscribeTo, insertedUser);
        	
        		if (logger.isDebugEnabled()) {
        			logger.debug("Auto subscription enabled and one open league, so auto-subscribing (new ID: {})", subscription.getId());
        		}
        	}
        }
        
        // Login the user.
        securityService.loginUser(insertedUser);

        return insertedUser;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void saveUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Attempting to save a user that is not persistent.");
        }

        // Encrypt the plain text password.
        securityService.encryptPassword(user);

        // Save the user details.
        userRepository.save(user);

        // Update the logged in user in the security context.
        securityService.loginUser(user);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional
    public PasswordReset createPasswordReset(String username) {
        User user = userRepository.findByUsername(username);
        PasswordReset passwordReset = null;

        if (user != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating password reset for user: \"{}\"", user.getUsername());
            }
            
            passwordReset = new PasswordReset();
            
            passwordReset.setUser(user);
            passwordReset.setToken(passwordResetTokenGenerator.generateToken(user));
            
            DateTime expiryDate = systemClock.getCurrentDateTime();
            expiryDate = expiryDate.plusDays(passwordResetExpiryIntervalInDays);
            passwordReset.setExpiryDate(expiryDate);
            
            passwordResetRepository.save(passwordReset);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Created password reset with ID: {}", passwordReset.getId());
            }
        }
        
        return passwordReset;
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly=true)
    public PasswordReset getPasswordReset(String token) {
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to fetch PasswordReset with token: \"{}\"", token);
        }
        
        PasswordReset reset = passwordResetRepository.findByToken(token);
        
        if (reset != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Found matching PasswordReset, ID: {}", reset.getId());
            }
        }

        return reset;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void resetPassword(PasswordReset reset, String newPassword, String newReminder) {
        // Update the user's password.
        reset.getUser().setPassword(newPassword);
        reset.getUser().setPasswordReminder(newReminder);
        saveUser(reset.getUser());

        // Mark the reset so that it can't be used again.
        reset.setUsedDate(systemClock.getCurrentDateTime());
        passwordResetRepository.save(reset);
    }
}