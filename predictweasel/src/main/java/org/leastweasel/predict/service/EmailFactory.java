/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.Locale;

import org.leastweasel.predict.domain.EmailDetails;
import org.leastweasel.predict.domain.PasswordReset;

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
}
