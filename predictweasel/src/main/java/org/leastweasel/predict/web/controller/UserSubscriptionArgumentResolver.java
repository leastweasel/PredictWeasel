/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.exception.NoUserSubscriptionException;
import org.leastweasel.predict.service.SubscriptionService;
import org.leastweasel.predict.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * An instance of a {@link HandlerMethodArgumentResolver} that tries to resolve the
 * value to be assigned to a {@link UserSubscription} argument of a controller method.
 * This subscription will always be to the {@link User}'s 'current' league. That's the
 * one that the logged in user is playing at the time.
 * <p>
 * Relies on a configured {@link HandlerInterceptor} to have set up the current
 * league's code in the user's session. If this code can't be used to fetch a League
 * entity from the database then an exception will be thrown. Some special exception
 * types can be thrown if the league code is one of the special values that aren't
 * really league codes (i.e. representing user having no subscription, or more than
 * one and not having chosen one yet).
 * 
 * @see LeagueCodeResolvingHandlerInterceptor
 */
@Component
public class UserSubscriptionArgumentResolver implements HandlerMethodArgumentResolver {
	@Autowired
	private SubscriptionService subscriptionService;
	
	/**
	 * Indicates whether this resolver supports the given parameter to a controller method.
	 * It does if the parameter is a UserSubscription.
	 * 
	 *  @param parameter the controller method parameter we need to check
	 *  @return true if the parameter is a UserSubscription
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return UserSubscription.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * Resolve the parameter, which will be a UserSubsciption.
	 * 
	 * @param parameter the controller method parameter whose value we're trying to supply
	 * @param mavContainer the ModelAndViewContainer for the current request
	 * @param webRequest the current request
	 * @param binderFactory a factory for creating {@link WebDataBinder} instances
	 * @return the resolved argument value, or {@code null}.
	 * @throws Exception in case of errors with the preparation of argument values
	 * @throws NoUserSubscriptionException if there's no 'current' subscription to use
	 */
	@Override
	public Object resolveArgument (MethodParameter parameter,
								  ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest,
								  WebDataBinderFactory binderFactory) throws Exception {
		
		if (UserSubscription.class.isAssignableFrom(parameter.getParameterType())) {
			String leagueCode = WebUtil.Session.getCurrentLeagueCode(webRequest);

			UserSubscription subscription = subscriptionService.getSubscriptionFromLeagueCode(leagueCode);

			if (subscription == null) {
				throw new NoUserSubscriptionException();
			}
			
			return subscription;
		}
		else {
			// should never happen...
			throw new UnsupportedOperationException(
					"Unknown parameter type: " + parameter.getParameterType() + " in method: " + parameter.getMethod());
		}
	}
}
