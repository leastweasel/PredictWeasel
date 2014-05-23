/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.config;

import org.leastweasel.predict.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configure the security side of PredictWeasel.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Configure the authentication process. We want form login, with a
	 * remember-me check box in the form to be supported. We navigate to
	 * the home page if the login is successful, and back to the form if not.
	 * 
	 * @param http web based security configuration
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/login", "/signup", "/forgottenPassword", "/resetPassword").permitAll()
			.anyRequest().fullyAuthenticated()
			.and()
			.formLogin().loginPage("/login")
						.failureUrl("/login?error")
						.defaultSuccessUrl("/")
						.permitAll()
			.and()
			.rememberMe();
	}

	/**
	 * Configure the authentication manager with our {@link SecurityService} instance so that
	 * the authentication process can look up the username entered by the user. Also set up
	 * a password encoder so that the password entered in the form is encoded first, before
	 * being checked against the one the database.
	 * 
	 * @param auth configuration for the authentication manager
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(securityService).passwordEncoder(passwordEncoder);
    }
}
