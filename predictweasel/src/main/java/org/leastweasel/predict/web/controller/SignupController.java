/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.service.UserService;
import org.leastweasel.predict.web.domain.SignupRequest;
import org.leastweasel.predict.web.validation.RegisterRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that allows a user to sign up for PredictWeasel. Navigates to the form on a GET request and tries to register
 * the user on a POST (i.e. a form submission).
 * <p>
 * If all goes well the new user will be logged in and ready to go.
 */
@Controller
@RequestMapping(value="/signup")
public class SignupController {

	private static final String FORM_VIEW_NAME = "signUp";
    private static final String SUCCESS_VIEW_NAME = "redirect:/";

    @Autowired
    private RegisterRequestValidator registerRequestValidator;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageSource messageSource;
    
    /**
     * Create the object that contains the form data.
     * 
     * @return a new command object
     */
    @ModelAttribute
    public SignupRequest formBackingBean() {
    	return new SignupRequest();
    }
    
    /**
     * Navigate to the page that displays the form for initiating a user sign up request.
     * 
     * @return the name of the view to navigate to (the sign up user form)
     */
	@RequestMapping(method = RequestMethod.GET)
	public String navigateToPage() {
		return FORM_VIEW_NAME;
	}

    /**
     * Submit the form. The form is validated and the request handed on to the user service. If all
     * goes well a confirmation message is added which will appear at the top of the home page. The
     * newly registered user will also be automatically logged in, and sent a confirmation email.
     * 
     * @param signupForm the submitted form backing object
     * @param errors to which validation errors are added
     * @param locale the locale used in the request
     * @param redirectAttributes any attributes we want to survive across the redirect should be added here 
     * @return the name of the page to navigate to (redirect to the home page or back to the form if errors)
     */
	@RequestMapping(method = RequestMethod.POST)
	public String submitForm(@Valid @ModelAttribute SignupRequest signupForm, 
							 Errors errors,
							 Locale locale,
							 RedirectAttributes redirectAttributes) {
		
		// Always validate with our own validator, too.
  		registerRequestValidator.validate(signupForm, errors);
    	
		if (errors.hasErrors()) {
			return FORM_VIEW_NAME;
		}

        // Register the user.
        User registeredUser = userService.registerUser(signupForm.getUser());
        
        // Notify the newly registered user via email.
        Map<String, Object> model = new HashMap<>();
        model.put("user", registeredUser);

        /* TODO: Send the sign up confirmation email
        mailService.sendFromTemplate(REGISTRATION_CONFIRMATION_TEMPLATE, registeredUser.getEmailAddress(),
                                     messageSource.getMessage(REGISTRATION_CONFIRMATION_SUBJECT, null, locale),
                                     model); */

        // Indicate on the screen that the registration was successful. This can only be done right
        // at the end as it's important that the redirect happens.
        String message = messageSource.getMessage("flash.user.registered", 
        										  	 new Object [] { registeredUser.getUsername() }, 
        										  	 locale);
        
        FlashMessageHelper.addSuccessMessage(redirectAttributes, message);
    	
		return SUCCESS_VIEW_NAME;
	}
}
