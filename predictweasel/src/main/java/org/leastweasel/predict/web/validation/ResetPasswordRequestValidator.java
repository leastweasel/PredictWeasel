/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.validation;

import org.leastweasel.predict.web.domain.ResetPasswordRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Validate the command object used when a user attempts to reset their password.
 */
@Component
public class ResetPasswordRequestValidator extends UserDetailsValidator {
    /**
     * Validate the command object. It is assumed that the command object will have already passed
     * through a JSR-303 style validator to perform basic field validation.
     *
     * @param command the command object to validate
     * @param errors add any errors here
     */
    public void validate(ResetPasswordRequest command, Errors errors) {
        validatePasswordConfirmation(command.getPassword(),
                                     command.getConfirmPassword(),
                                     "confirmPassword", errors);
    }
}
