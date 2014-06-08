/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.leastweasel.predict.domain.EmailDetails;
import org.leastweasel.predict.domain.PasswordReset;
import org.leastweasel.predict.service.EmailFactory;
import org.leastweasel.predict.service.EmailService;
import org.leastweasel.predict.service.UserService;
import org.leastweasel.predict.web.domain.ForgottenPasswordRequest;
import org.leastweasel.predict.web.validation.ForgottenPasswordRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that allows a user to notify the system that they've forgotten their password and that
 * they'd like to reset it.
 */
@Controller
@RequestMapping(value="/forgottenPassword")
public class ForgottenPasswordController {

	private static final String FORM_VIEW_NAME = "forgottenPassword";
    private static final String SUCCESS_VIEW_NAME = "redirect:/forgottenPassword";

    private static Logger logger = LoggerFactory.getLogger(ForgottenPasswordController.class);

    @Autowired
    private ForgottenPasswordRequestValidator forgottenPasswordRequestValidator;
    
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private EmailFactory emailFactory;
    
    @Autowired
    private MessageSource messageSource;
    
    /**
     * Create the object that contains the form data.
     * 
     * @return a new command object
     */
    @ModelAttribute
    public ForgottenPasswordRequest formBackingBean() {
    	return new ForgottenPasswordRequest();
    }
    
    /**
     * Navigate to the page that displays the form for initiating a password reminder request.
     * 
     * @return the name of the view to navigate to (the password reminder form)
     */
	@RequestMapping(method = RequestMethod.GET)
	public String navigateToPage() {
		return FORM_VIEW_NAME;
	}

    /**
     * Submit the form. The form is validated and the request handed on to the user service. If all
     * goes well a message is added which will tell the user what they should do next.
     * 
     * @param forgottenPasswordRequest the submitted form backing object
     * @param errors to which validation errors are added
     * @param locale the locale used in the request
     * @param redirectAttributes any attributes we want to survive across the redirect should be added here 
     * @return the name of the page to navigate to (redirect to a confirmation page)
      */
	@RequestMapping(method = RequestMethod.POST)
	public String submitForm(@Valid @ModelAttribute ForgottenPasswordRequest forgottenPasswordRequest, 
							 Errors errors,
							 Locale locale,
							 RedirectAttributes redirectAttributes) {
		
		// Always validate with our own validator, too.
		forgottenPasswordRequestValidator.validate(forgottenPasswordRequest, errors);
    	
		if (errors.hasErrors()) {
			return FORM_VIEW_NAME;
		}

        PasswordReset passwordReset = 
                userService.createPasswordReset(forgottenPasswordRequest.getUsername());

        if (passwordReset != null) {
	        // The model just contains the current user.
	        Map<String, Object> model = new HashMap<String, Object>();
	        model.put("passwordReset", passwordReset);
	        
	        try {
	            model.put("passwordResetToken", URLEncoder.encode(passwordReset.getToken(), "UTF-8"));
	        } catch(UnsupportedEncodingException e) {
	            throw new IllegalArgumentException("Exception URL encoding password reset token", e);
	        }

	        EmailDetails emailDetails = emailFactory.createPasswordReminderEmail(passwordReset, locale);

	        if (logger.isDebugEnabled()) {
				logger.debug("Password reminder email: {}", emailDetails.getSubject());
				logger.debug("Password reminder email: {}", emailDetails.getMessageText());
			}
	        
	        // Send the password reminder email.
	        emailService.send(emailDetails, forgottenPasswordRequest.getUsername());
        }
        
        // Indicate on the screen that the password reminder has been sent, even if it hasn't because the email address
        // didn't match a known user.
        
        String message = messageSource.getMessage("flash.password.reminder.sent", 
                                                  new Object [] { forgottenPasswordRequest.getUsername() }, 
                                                  locale);

        FlashMessageHelper.addSuccessMessage(redirectAttributes, message);
        
		return SUCCESS_VIEW_NAME;
	}
}
