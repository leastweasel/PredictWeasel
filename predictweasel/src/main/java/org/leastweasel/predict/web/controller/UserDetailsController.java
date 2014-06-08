/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.leastweasel.predict.service.SecurityService;
import org.leastweasel.predict.service.UserService;
import org.leastweasel.predict.web.domain.EditUserRequest;
import org.leastweasel.predict.web.validation.EditUserRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that allows a user to navigate to the page where they can edit their personal details.
 */
@Controller
@RequestMapping("/userDetails")
public class UserDetailsController {
	private static final String FORM_VIEW_NAME = "userDetails";
    private static final String SUCCESS_VIEW_NAME = "redirect:/userDetails";
    
	@Autowired
	private SecurityService securityService;
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private EditUserRequestValidator editUserRequestValidator;
    
    @Autowired
    private MessageSource messageSource;
    
    /**
     * Add the object that contains the form data (which is the currently logged in user)
     * to the model so that the view can get hold of it. This resource will be protected so
     * that only logged in users can access it, so we can guarantee there will be one.
     * 
     * @return the currently logged in user
     */
    @ModelAttribute
    public EditUserRequest formBackingBean() {
    		return new EditUserRequest(securityService.getLoggedInUser());
    }
    
	/**
	 * Handle a GET request to navigate to the user details page.
	 * 
	 * @return the name of the view to navigate to
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String navigateToPage() {

		return "userDetails";
	}

	/**
     * Submit the form. The form is validated and the request handed on to the user service. If all
     * goes well a confirmation message is added which will appear at the top of the home page.
     * 
     * @param editUserRequest the submitted form backing object
     * @param errors to which validation errors are added
     * @param locale the locale used in the request
     * @param redirectAttributes any attributes we want to survive across the redirect should be added here 
     * @return the name of the page to navigate to (redirect to the home page)
     */
    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@Valid @ModelAttribute EditUserRequest editUserRequest,
                             Errors errors, Locale locale,
                             RedirectAttributes redirectAttributes) {

    		// Always validate with our own validator, too.
  		editUserRequestValidator.validate(editUserRequest, errors);
    	
  		if (errors.hasErrors()) {
  			return FORM_VIEW_NAME;
  		}

        // If the user entered a new password we can be sure it's valid so update the user object.
        // Otherwise we leave the old value untouched.

        if (StringUtils.isNotBlank(editUserRequest.getNewPassword())) {
            editUserRequest.getUser().setPassword(editUserRequest.getNewPassword());
        }

        // Save the user.
        userService.saveUser(editUserRequest.getUser());
        
        // Indicate on the screen that the update was successful. This can only be done right
        // at the end as it's important that the redirect happens.
        String message = messageSource.getMessage("flash.user.updated", 
        										  new Object [] { editUserRequest.getUser().getUsername() }, 
        										  locale);
        
        FlashMessageHelper.addSuccessMessage(redirectAttributes, message);

        return SUCCESS_VIEW_NAME;
    }
}
