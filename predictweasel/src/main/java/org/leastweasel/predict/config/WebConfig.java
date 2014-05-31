/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.config;

import java.util.List;

import org.leastweasel.predict.web.controller.LeagueCodeResolvingHandlerInterceptor;
import org.leastweasel.predict.web.controller.UserSubscriptionArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configure the web side of things for PredictWeasel. At the moment that's just view controllers
 * as Spring Boot has taken care of so much else.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private LeagueCodeResolvingHandlerInterceptor gameReqestHandlerInterceptor;

	@Autowired
	private UserSubscriptionArgumentResolver userSubscriptionArgumentResolver;
	
	/**
	 * Set up the view controllers we need. We use a view controller when
	 * there's no need to write our own controller: we just need to navigate to
	 * a view. With login, apart from rendering the form, Spring Security handles
	 * everything else.
	 * 
	 * @param registry the view controller registry to add our views to
	 */
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/resetPasswordFailed").setViewName("resetPasswordFailed");
        registry.addViewController("/subscriptions").setViewName("landing");
    }
	
	/**
	 * Interceptors, as the name suggests, intercept requests to the configured paths. We want to
	 * intercept any request that requires a league to ensure we know which one the user is
	 * playing at the moment.  
	 * 
	 * @param registry the registry of interceptors
	 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(gameReqestHandlerInterceptor).addPathPatterns("/", "/league/*");
    }
    
    /**
     * An argument resolver allows a controller method to have a non-standard argument type.
     * The resolver tries to work out what value should be passed in to the method.
     * 
     *  @param argumentResolvers the collection of argument resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    		argumentResolvers.add(userSubscriptionArgumentResolver);
	}
}
