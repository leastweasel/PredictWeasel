package org.leastweasel.predict.service.support;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.leastweasel.predict.domain.EmailDetails;
import org.leastweasel.predict.domain.PasswordReset;
import org.leastweasel.predict.service.EmailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

/**
 * An implementation of {@link EmailFactory} that hand crafts the emails, rather than
 * using a templating library like Velocity, for example.
 */
public class HandCraftedEmailFactory implements EmailFactory {
	@Autowired
	private MessageSource messageSource;

	@Value("${webapp.protocol}")
	private String webApplicationProtocol;
	
	@Value("${webapp.hostname}")
	private String webApplicationHostname;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EmailDetails createPasswordReminderEmail(PasswordReset passwordReset, Locale locale) {
		StringBuilder str = new StringBuilder();
		
		str.append("Hello ");
		str.append(passwordReset.getUser().getName());
		str.append(",\n\n");

		str.append("Someone, hopefully you, has asked PredictWeasel to send them a password reminder as they've forgotten it.\n\n");
		str.append("PredictWeasel stores passwords in an encrypted form so it is not possible to send it to you directly.\n\n");

		if (StringUtils.isNotBlank(passwordReset.getUser().getPasswordReminder())) {
			str.append("However, you have supplied a password reminder, which might help you remember it.\n\n");

			str.append("Your password reminder is: ");
			str.append(passwordReset.getUser().getPasswordReminder());
			str.append("\n\n");
			
			str.append("If you still can't remember your password then you can ask PredictWeasel to allow you to change it.\n\n"); 
		} else {
			str.append("As you haven't specified a password reminder then your only option is to ask PredictWeasel to allow you to change it.\n\n");
		}

		str.append("If you click on the link below (or copy it into your browser address bar) you will be taken to a page where you can enter a new password.\n\n");

		DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yy HH:mm");
		
		str.append("Please note that this link will only work until ");
		str.append(format.print(passwordReset.getExpiryDate()));
		str.append(" and it can only be used once.\n\n");

		str.append("The link is: ");
		str.append(webApplicationProtocol);
		str.append("://");
		str.append(webApplicationHostname);
		str.append("/resetPassword?token=");

		try {
			str.append(URLEncoder.encode(passwordReset.getToken(), "UTF-8"));
		} catch(UnsupportedEncodingException e) {
			// Won't happen.
		}
		
		str.append("\n\n");

		str.append("Thank you for playing PredictWeasel.\n");
		
        String emailSubject = messageSource.getMessage("password.reminder.subject", null, locale);

		EmailDetails emailDetails = new EmailDetails();
		emailDetails.setMessageText(str.toString());
        emailDetails.setSubject(emailSubject);
        
		return emailDetails;
	}
}
