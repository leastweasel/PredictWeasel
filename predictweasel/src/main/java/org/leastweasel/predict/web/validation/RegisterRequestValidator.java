/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.validation;

import org.leastweasel.predict.web.domain.SignupRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Validate the command object used when registering a user.
 */
@Component
public class RegisterRequestValidator extends UserDetailsValidator {
    /**
     * Validate the command object. It is assumed that the command object will have already passed
     * through a JSR-303 style validator to perform basic field validation.
     *
     * @param command the command object to validate
     * @param errors add any errors here
     */
    public void validate(SignupRequest command, Errors errors) {
        validateUsername(command.getUser(), "user.username", errors);
        
        validatePasswordConfirmation(command.getUser().getPassword(),
                                     command.getConfirmPassword(),
                                     "confirmPassword", errors);
        
        validateName(command.getUser(), "user.name", errors);
    }
}
