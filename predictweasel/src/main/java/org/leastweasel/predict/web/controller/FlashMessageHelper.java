package org.leastweasel.predict.web.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Helper class for adding a flash message to a {@link RedirectAttributes} object. Provides methods for
 * adding a message of each of the four status types. Adds them to the redirect attributes in a
 * consistent location.
 */
public class FlashMessageHelper {
	private static String ATTRIBUTE_NAME = "flashMessage";
	
	/**
	 * Add a message indicating a successful operation of some kind.
	 * 
	 * @param redirectAttributes the object to which the flash message should be added
	 * @param message the message text to add
	 */
	public static void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME, new FlashMessage(message, FlashMessage.Status.SUCCESS));
	}
	
	/**
	 * Add a message that is merely informational.
	 * 
	 * @param redirectAttributes the object to which the flash message should be added
	 * @param message the message text to add
	 */
	public static void addInfoMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME, new FlashMessage(message, FlashMessage.Status.INFO));
	}
	
	/**
	 * Add a message warning of something.
	 * 
	 * @param redirectAttributes the object to which the flash message should be added
	 * @param message the message text to add
	 */
	public static void addWarningMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME, new FlashMessage(message, FlashMessage.Status.WARNING));
	}
	
	/**
	 * Add a message indicating a failed operation of some kind.
	 * 
	 * @param redirectAttributes the object to which the flash message should be added
	 * @param message the message text to add
	 */
	public static void addFailureMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME, new FlashMessage(message, FlashMessage.Status.ERROR));
	}
}
