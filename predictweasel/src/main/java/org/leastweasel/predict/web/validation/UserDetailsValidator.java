/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.validation;

import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

/**
 * Validator shared by others involved in user-based requests.
 */
public class UserDetailsValidator {
    @Autowired
    protected UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsValidator.class);
    
    /**
     * Validate that the username is a valid email address and that it hasn't already been
     * registered. Does nothing if there are already validation errors on the field.
     *
     * @param user the user whose name we have to validate
     * @param fieldName the name of the field being validated (for error messages)
     * @param errors add any errors here
     */
    protected void validateUsername(User user, String fieldName, Errors errors) {
        if (!errors.hasFieldErrors(fieldName)) {
            if (userService.userForUsernameExists(user)) {
                errors.rejectValue(fieldName, "validation.username.in.use",
                                   new Object[] { user.getUsername(), }, null);
            }
        }
    }

    /**
     * Validate the user's name.
     *
     * @param user the user whose name we have to validate
     * @param fieldName the name of the field being validated (for error messages)
     * @param errors add any errors here
     */
    protected void validateName(User user, String fieldName, Errors errors) {
        if (!errors.hasFieldErrors(fieldName)) {
	        if (userService.userForNameExists(user)) {
	            errors.rejectValue(fieldName, "validation.name.in.use",
	                               new Object[] { user.getName(), }, null);
	        }
        }
    }

    /**
     * Validate the password matches the confirmation.  Does nothing if there are already
     * validation errors on the field.
     *
     * @param password the new password
     * @param confirmPassword the confirmation of the new password
     * @param fieldName the name of the field being validated (for error messages)
     * @param errors add any errors here
     */
    protected void validatePasswordConfirmation(String password, String confirmPassword,
                                                String fieldName, Errors errors) {
        if (!errors.hasFieldErrors(fieldName)) {
            if ((StringUtils.hasText(password) != StringUtils.hasText(confirmPassword)) || 
                    (confirmPassword != null && !confirmPassword.equals(password))) {
                
            	if (logger.isDebugEnabled()) {
            		logger.debug("Password and confirmation don't match.");
            	}
            	
                errors.rejectValue(fieldName, "validation.password.mismatch");
            }
        }
    }
}
