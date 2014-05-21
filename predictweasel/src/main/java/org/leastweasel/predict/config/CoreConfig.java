/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Bean configuration for any core components,
 */
@Configuration
public class CoreConfig {
	/**
	 * Create a bean for encoding passwords. This is used by both the sign up process (to
	 * encrypt the password chosen by the user), and Spring Security during authentication
	 * to ensure the password is entered correctly.
	 * <p>
	 * I might have to change the type of encoder used as this takes 5 seconds to encrypt
	 * a password! 
	 * 
	 * @return a BCrypt password encoder bean
	 */
	@Bean
    public PasswordEncoder configurePasswordEncoder() {
        return new BCryptPasswordEncoder(16);
    }
}
