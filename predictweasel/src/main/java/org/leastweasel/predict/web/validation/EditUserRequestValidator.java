/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2009 by Andrew Gillies
 */
package org.leastweasel.predict.web.validation;

import org.leastweasel.predict.service.SecurityService;
import org.leastweasel.predict.web.domain.EditUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Validate the command object used when registering a user.
 */
@Component
public class EditUserRequestValidator extends UserDetailsValidator {
    @Autowired
    private SecurityService securityService;

    private static final Logger logger = LoggerFactory.getLogger(EditUserRequestValidator.class);

    /**
     * Validate the command object. It is assumed that the command object will have already passed
     * through a JSR-303 style validator to perform basic field validation.
     * 
     * @param command the command object to validate
     * @param errors add any errors here
     */
    public void validate(EditUserRequest command, Errors errors) {
        validateUsername(command.getUser(), "user.username", errors);
        validateOldPassword(command, errors);
        validatePasswordConfirmation(command, errors);
        validateName(command.getUser(), "user.name", errors);
    }

    /**
     * Validate the password length and pattern.
     * 
     * @param command the command object to validate
     * @param errors add any errors here
     */
    protected void validateOldPassword(EditUserRequest command, Errors errors) {
        // Only validate the password if the user has entered a new one and there isn't already an
        // error for the password.
        if (!errors.hasFieldErrors("oldPassword") && command.getNewPassword() != null) {
            if (command.getOldPassword() == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Need to supply old password as new one has been entered.");
                }
                
                errors.rejectValue("oldPassword", "validation.old.password.required");
            } else if (!securityService.verifyPasswordsMatch(command.getOldPassword(),
                                                             command.getUser().getPassword())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Entered password does not match user's.");
                }
                
                errors.rejectValue("oldPassword", "validation.invalid.password");
            }

        }
    }

    /**
     * Validate the password matches the confirmation.
     * 
     * @param command the command object to validate
     * @param errors add any errors here
     */
    private void validatePasswordConfirmation(EditUserRequest command, Errors errors) {
        // Only validate the confirmation if the user has entered a new password.
        if (command.getNewPassword() != null) {
            validatePasswordConfirmation(command.getNewPassword(), command.getConfirmPassword(),
                                         "confirmPassword", errors);
        }
    }
}
