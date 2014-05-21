/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * The PredictWeasel application entry point. Kicks off Spring Boot with this class' package
 * as the root of all component and entity scanning. That means that any suitably annotated
 * class will be picked up by the application context builder.
 * <p>
 * Running the application will also start the embedded Tomcat server so it really is starting
 * PredictWeasel in standalone mode.
 */
@EnableAutoConfiguration
@ComponentScan
public class BootPredictWeasel {

	/**
	 * Program main. Tell Spring Boot to get going.
	 * 
	 * @param args any command line arguments, as usual
	 */
	public static void main(String[] args) {
		SpringApplication.run(BootPredictWeasel.class, args);
	}
}
