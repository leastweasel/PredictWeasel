package org.leastweasel.predict.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.leastweasel.predict.domain.EmailDetails;
import org.leastweasel.predict.service.EmailFactory;
import org.leastweasel.predict.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * A {@link HandlerExceptionResolver} for handling uncaught exceptions. When we get
 * an uncaught exception we want to email an administrator (me) before displaying
 * an error page. We also want to set the HTTP status to 500 so that it's clear
 * something went wrong. 
 * <p>
 * Note that this handler is only called for uncaught exceptions. There are a
 * number of exceptions that are handled by Spring before they get anywhere near
 * here. These are exceptions that Spring translates into a specific HTTP status
 * code so it's appropriate that they don't come here.
 */
@Component
public class PredictWeaselExceptionResolver extends SimpleMappingExceptionResolver {
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private EmailFactory emailFactory;
	
	@Value("${predictWeasel.adminEmailAddress}")
	private String adminEmailAddress;
	
	private static final Logger logger = LoggerFactory.getLogger(PredictWeaselExceptionResolver.class);
	
	/**
	 * Resolve an exception by sending an email to the administrator and navigating to an
	 * error page. The response status will be set to INTERNAL_SERVER_ERROR.
	 * 
	 * @param request the HTTP servlet request
	 * @param response the HTTP servlet response
	 * @param handler the handler that was handling the response when the exception was thrown
	 * @param ex the exception that was thrown
	 * @return the page to navigate to (generic error page)
	 */
	@Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object handler, Exception ex) {
	
		logger.error("Handling an unhandled exception", ex);
		
		// Set the status of the request so that it's obvious it failed.
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		
		EmailDetails email = emailFactory.createGenericExceptionEmail(handler, ex, request.getLocale());
		emailService.send(email, adminEmailAddress);
		
		// Go to the general purpose error page.
		return new ModelAndView("500");
	}
}
