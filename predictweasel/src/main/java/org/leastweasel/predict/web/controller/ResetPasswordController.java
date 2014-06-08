/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.leastweasel.predict.domain.PasswordReset;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.UserService;
import org.leastweasel.predict.web.domain.ResetPasswordRequest;
import org.leastweasel.predict.web.validation.ResetPasswordRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that allows a user to reset their password because they've forgotten it.
 */
@Controller
@RequestMapping(value="/resetPassword")
public class ResetPasswordController {

	private static final String FORM_VIEW_NAME = "resetPassword";
    private static final String SUCCESS_VIEW_NAME = "redirect:/";
    private static final String FAILURE_VIEW_NAME = "resetPasswordFailed";

    @Autowired
    private ResetPasswordRequestValidator resetPaswordRequestValidator;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private Clock systemClock;
    
    /**
     * Create the object that contains the form data.
     * 
     * @return a new command object
     */
    @ModelAttribute
    public ResetPasswordRequest formBackingBean() {
    	return new ResetPasswordRequest();
    }
    
    /**
     * Navigate to the page that displays the form where the user enters their new password.
     * 
     * @param token the token representing the password to reset
     * @return the name of the view to navigate to (the password reset form)
     */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView navigateToPage(@RequestParam("token") String token) {
		
        PasswordReset reset = userService.getPasswordReset(token);
        ModelAndView errorMav = validatePasswordReset(reset);
        
        if (errorMav != null) {
            return errorMav;
        }
        
        return new ModelAndView(FORM_VIEW_NAME).addObject("resetToken", token);
	}
	
    /**
     * Submit the form. The form is validated and the request handed on to the user service. If all
     * goes well a message is added confirming that the password has been changed.
     * 
     * @param resetPasswordRequest the submitted form backing object
     * @param errors to which validation errors are added
     * @param token the token representing the password to reset
     * @param locale the locale used in the request
     * @param redirectAttributes any attributes we want to survive across the redirect should be added here 
     * @return the name of the page to navigate to (redirect to a confirmation page)
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitForm(@Valid @ModelAttribute ResetPasswordRequest resetPasswordRequest,
                                       Errors errors, @RequestParam("token") String token,
                                       Locale locale,
                                       RedirectAttributes redirectAttributes) {

        PasswordReset reset = userService.getPasswordReset(token);
        ModelAndView errorMav = validatePasswordReset(reset);

        if (errorMav != null) {
            return errorMav;
        }
        
        // Always validate with our own validator, too.
        resetPaswordRequestValidator.validate(resetPasswordRequest, errors);
        
        if (errors.hasErrors()) {
            return new ModelAndView(FORM_VIEW_NAME);
        }

        // Reset the password.
        userService.resetPassword(reset, resetPasswordRequest.getPassword(),
                                  resetPasswordRequest.getPasswordReminder());
        
        // Indicate on the screen that the password reminder has been successful.
        String message = messageSource.getMessage("flash.password.reset", 
                                                  new Object [] {}, locale);
        
        FlashMessageHelper.addSuccessMessage(redirectAttributes, message);

        return new ModelAndView(SUCCESS_VIEW_NAME);
    }
    
    /**
     * Validate that the password reset is valid. A valid reset must:
     * <ul>
     * <li>be known to the system</li>
     * <li>represent an active user</li>
     * <li>not have been used before</li>
     * <li>not have expired</li>
     * </ul>
     * If the reset is invalid for some reason the user will be taken to an error page where an
     * appropriate message will be displayed. This isn't part of the normal validation process as
     * this out-ranks anything the user may enter in the form and is also required when navigating
     * to the form in the first place.
     * 
     * @param reset the reset requested by the user
     * @return a ModelAndView if anything is wrong with the reset, null if not
     */
    private ModelAndView validatePasswordReset(PasswordReset reset) {
        String messageCode = null;
        
        if (reset == null) {
            messageCode = "password.reset.unknown.token";
        } else if (!reset.getUser().isEnabled()) {
            messageCode = "password.reset.inactive.user";
        } else if (reset.getUsedDate() != null) {
            messageCode = "password.reset.already.used";
        } else if (reset.getExpiryDate().isBefore(systemClock.getCurrentDateTime())) {
            messageCode = "password.reset.expired";
        }
        
        if (messageCode != null) {
            return new ModelAndView(FAILURE_VIEW_NAME).addObject("messageCode", messageCode);
        }
        
        return null;
    }
}
