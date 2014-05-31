package org.leastweasel.predict.web.controller;

import java.util.List;

import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.exception.NoUserSubscriptionException;
import org.leastweasel.predict.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Assist controllers by providing exception handler methods that can be used
 * by multiple instances. 
 */
@ControllerAdvice
public class ExceptionHandlers {
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

	/**
	 * Handle an exception indicating that there's no current league available. This will
	 * typically redirect the {@link User} to another page.
	 * <p>
	 * This exception can be thrown in three scenarios:
	 * <ul>
	 * <li>The user has multiple subscriptions but hasn't chosen one to play yet</li>
	 * <li>The user has no subscriptions at all</li>
	 * <li>The user had a 'current' league but no longer has a subscription for it.
	 *     Moreover, the user now has zero or many (i.e. more than one) subscriptions
	 *     so there's no definite candidate for the new current league.</li>
	 * </ul>
	 * In the first case we can redirect the user to a page that gives them the chance to
	 * select one of their subscriptions. In the second we take them to the home page.
	 * The third case is handled in the same way as the first two, depending on how
	 * many subscriptions the user now has.  
	 * 
	 * @param exception the exception that was thrown
	 */
	@ExceptionHandler(NoUserSubscriptionException.class)
	public String handleNoUserSubscriptionException(NoUserSubscriptionException exception) {
		
		logger.error("Got an exception", exception);
		
		// Check how many subscriptions the user now has.
		List<UserSubscription> subscriptions = subscriptionService.getSubscriptions();
		
		if (subscriptions.isEmpty()) {
			return "redirect:/";
		} else {
			return "redirect:/subscriptions?wobble=yes";
		}
	}
}