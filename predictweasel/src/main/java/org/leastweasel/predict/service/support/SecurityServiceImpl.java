/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.repository.UserRepository;
import org.leastweasel.predict.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link SecurityService}.
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);
    
    /**
     * {@inheritDoc}
     */
	@Override
	public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if ((auth != null) && (auth.getPrincipal() instanceof User)) {
        	User loggedInUser = (User) auth.getPrincipal();
        	
        	if (logger.isDebugEnabled()) {
        		logger.debug("Logged in user is: \"{}\"", loggedInUser.getUsername());
        	}
        	
            return loggedInUser;
        }

        return null;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void loginUser(User user) {
    	if (logger.isDebugEnabled()) {
    		logger.debug("Manually logging in user: \"{}\"", user.getUsername());
    	}
    	
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(user, user.getPassword(),
                                                    user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void encryptPassword(User user) {
    	String encryptedPassword = user.getPassword();
    	
    	if (passwordEncoder != null) {
    		encryptedPassword = passwordEncoder.encode(user.getPassword());
    		
    		logger.info("Encrypted password (length = {}) is [{}]", encryptedPassword.length(), encryptedPassword);
    	}
    	
    	user.setPassword(encryptedPassword);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean verifyPasswordsMatch(String plainTextPassword, String encryptedPassword) {
    	if (plainTextPassword == null || encryptedPassword == null) {
    		return false;
    	}
    	
    	if (passwordEncoder == null) {
    		return plainTextPassword.equals(encryptedPassword);
    	} else {
    		return passwordEncoder.matches(encryptedPassword, plainTextPassword);
    	}
	}
    
    /**
     * Load the user with the given username from the database. This is called by the Spring
     * Security authentication manager as this bean has been configured as an Authentication
     * Provider user service {@link SecurityService} for more details.
     * <p>
     * Although involved in the authentication process, this method is merely responsible for
     * returning any user with the given username. Validating the user's credentials is done
     * elsewhere.
     * 
     * @param username the username to authenticate
     * @return the user identified by the username
     * @throws UsernameNotFoundException if no such user exists
     * @throws DataAccessException if there are problems with the database
     */
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException, DataAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to find username: {}", username);
        }

    	User user = userRepository.findByUsername(username);
    	
        if (user == null) {
            throw new UsernameNotFoundException("Username not found: [" + username + "]");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Located user: {}", user);
        }

        return user;
    }
}
