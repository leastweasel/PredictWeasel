/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import org.apache.commons.lang.StringUtils;
import org.leastweasel.predict.domain.EmailDetails;
import org.leastweasel.predict.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link EmailService} using Spring's
 * {@link JavaMailSender} interface. 
 */
@Service
public class JavaMailEmailService implements EmailService {
	@Autowired
	private JavaMailSender sender;

	@Value("${email.defaultSender}")
    private String defaultSender;

	private static final Logger logger = LoggerFactory.getLogger(JavaMailEmailService.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void send(EmailDetails email, String recipient) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setSubject(email.getSubject());
        message.setText(email.getMessageText());
        message.setTo(recipient);

        if (StringUtils.isNotBlank(defaultSender)) {
        	message.setFrom(defaultSender);
        }
        
        try {
        	sender.send(message);
        } catch (MailException e) {
        	logger.error("Error sending email to '{}'", recipient);
        	logger.error("Message was: {}", email.getMessageText());
        	logger.error("Exception:", e);
        }
	}
}
