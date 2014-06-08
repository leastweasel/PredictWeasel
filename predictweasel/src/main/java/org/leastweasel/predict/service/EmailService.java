/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import org.leastweasel.predict.domain.EmailDetails;

/**
 * An interface for sending emails. 
 */
public interface EmailService {
	/**
	 * Send the given email to a single recipient.
	 * 
	 * @param email the email to send
	 * @param recipient the person to send it to
	 */
	void send(EmailDetails email, String recipient);
}
