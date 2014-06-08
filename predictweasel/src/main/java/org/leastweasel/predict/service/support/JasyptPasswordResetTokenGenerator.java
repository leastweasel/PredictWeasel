/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import org.jasypt.digest.StringDigester;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.PasswordResetTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of a {@link PasswordResetTokenGenerator} that uses Jasypt to create the token.
 */
public class JasyptPasswordResetTokenGenerator implements PasswordResetTokenGenerator {
	@Autowired
	private Clock systemClock;
	
    private StringDigester stringDigester;
    
    private final static Logger logger = LoggerFactory.getLogger(JasyptPasswordResetTokenGenerator.class);
    
    /**
     * Generate a token for the given user. The token is a digest of various parts of the user's
     * details concatenated together.
     * 
     * @param user the user for whom the token must be generated
     * @return a token string
     */
    @Override
    public String generateToken(User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating password reset token for user: \"{}\"", user.getUsername());
        }
        
        return stringDigester.digest(createPlainTextString(user));
    }
    
    /**
     * Create the plain text to be digested by concatenating various attributes of the user.
     * 
     * @param user the user to use to create the plain text
     * @return a plain text token
     */
    private String createPlainTextString(User user) {
        StringBuilder str = new StringBuilder();
        
        str.append(user.getUsername());
        str.append(user.getPassword());
        str.append(user.getPasswordReminder());
        str.append(systemClock.getCurrentDateTime());
        
        return str.toString();
    }

    /**
     * Set the string digester. The string digester takes the plain text token and creates from it
     * a string digest using a configured algorithm.
     * 
     * @param stringDigester the string digester to use
     */
    public void setStringDigester(StringDigester stringDigester) {
        this.stringDigester = stringDigester;
    }
}
