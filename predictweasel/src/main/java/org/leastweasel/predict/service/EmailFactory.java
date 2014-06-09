/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.Locale;

import org.leastweasel.predict.domain.EmailDetails;
import org.leastweasel.predict.domain.PasswordReset;
import org.leastweasel.predict.domain.User;

/**
 * An interface for creating system emails.
 */
public interface EmailFactory {
	/**
	 * Create the email sent as a result of a password reminder request.
	 * 
	 * @param passwordReset identifies who it is that has asked for the reminder
     * @param locale the locale to use for any internationalisation
	 * @return details of the email to send
	 */
	EmailDetails createPasswordReminderEmail(PasswordReset passwordReset, Locale locale);
	
	/**
	 * Create the email sent as a result of a user signing up to PredictWeasel.
	 * 
	 * @param user identifies who it is that has just signed up
     * @param locale the locale to use for any internationalisation
	 * @return details of the email to send
	 */
	EmailDetails createSignUpConfirmationEmail(User user, Locale locale);

	/**
	 * Create the email sent as a result of an unhandled exception being thrown.
	 * 
	 * @param handler the handler that was processing the request at the time
	 * @param ex the exception that was thrown
     * @param locale the locale to use for any internationalisation
	 * @return details of the email to send
	 */
	EmailDetails createGenericExceptionEmail(Object handler, Exception e, Locale locale);
}
