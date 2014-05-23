/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.validation;

import org.leastweasel.predict.service.UserService;
import org.leastweasel.predict.web.domain.ForgottenPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Validate the command object used when a user indicates that they've forgotten their password.
 */
@Component
public class ForgottenPasswordRequestValidator {
    @Autowired
    private UserService userService;

    /**
     * Validate the command object. It is assumed that the command object will have already passed
     * through a JSR-303 style validator to perform basic field validation.
     * 
     * @param command the command object to validate
     * @param errors add any errors here
     */
    public void validate(ForgottenPasswordRequest command, Errors errors) {
    }
}
