/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Spring Bean configuration for Thymeleaf. None of this was necessary
 * until we started running PW as a standalone JAR on the DO server.
 * <p>
 * The problem seems to be with the type of resource loader Spring uses
 * with its own Boot configuration. All I think I've changed is to make
 * it use a class loader template resolver.
 */
@Configuration
public class ThymeleafConfig implements EnvironmentAware {
	@Autowired
	private final ResourceLoader resourceLoader = new DefaultResourceLoader();

	private RelaxedPropertyResolver environment;

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = new RelaxedPropertyResolver(environment,
				"spring.thymeleaf.");
	}

	@PostConstruct
	public void checkTemplateLocationExists() {
		Boolean checkTemplateLocation = this.environment.getProperty(
				"checkTemplateLocation", Boolean.class, true);
		if (checkTemplateLocation) {
			Resource resource = this.resourceLoader.getResource(/*this.environment
					.getProperty("prefix", DEFAULT_PREFIX)*/DEFAULT_PREFIX);
			Assert.state(resource.exists(), "Cannot find template location: "
					+ resource + " (please add some templates "
					+ "or check your Thymeleaf configuration)");
		}
	}

	@Bean
	public ITemplateResolver defaultTemplateResolver() {
		TemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix(this.environment.getProperty("prefix", DEFAULT_PREFIX));
		resolver.setSuffix(this.environment.getProperty("suffix", DEFAULT_SUFFIX));
		resolver.setTemplateMode(this.environment.getProperty("mode", "HTML5"));
		resolver.setCharacterEncoding(this.environment.getProperty("encoding",
				"UTF-8"));
		resolver.setCacheable(this.environment.getProperty("cache", Boolean.class,
				true));
		return resolver;
	}

	public static boolean templateExists(Environment environment,
			ResourceLoader resourceLoader, String view) {
		String prefix = environment.getProperty("spring.thymeleaf.prefix",
				ThymeleafAutoConfiguration.DEFAULT_PREFIX);
		String suffix = environment.getProperty("spring.thymeleaf.suffix",
				ThymeleafAutoConfiguration.DEFAULT_SUFFIX);
		return resourceLoader.getResource(prefix + view + suffix).exists();
	}
}
