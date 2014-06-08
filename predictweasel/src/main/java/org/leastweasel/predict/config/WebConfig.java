/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.config;

import java.util.List;

import org.leastweasel.predict.web.controller.LeagueCodeResolvingHandlerInterceptor;
import org.leastweasel.predict.web.controller.UserSubscriptionArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
        registry.addViewController("/error/403").setViewName("403");
        registry.addViewController("/error/404").setViewName("404");
        registry.addViewController("/error/500").setViewName("500");
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

    /**
     * A bean that customises the servlet container (embeded Tomcat in our case).
     *   
     * @return a servlet container customiser
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
    		return new MyServletContainerCustomizer();
    }

    /**
     * A class that can customise our embedded Tomcat environment.
     */
    private static class MyServletContainerCustomizer implements EmbeddedServletContainerCustomizer {
    		/**
    		 * Customise Tomcat. At the moment just create a few error pages to display for specific HTTP
    		 * status values.
    		 * 
    		 * @param factory for customising Tomcat
    		 */
    		@Override
    		public void customize(ConfigurableEmbeddedServletContainer factory) {
    			factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"),
    								  new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"),
    								  new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));

    			factory.addErrorPages(new ErrorPage(Exception.class, "/error/500"));
    		}
    }
}
