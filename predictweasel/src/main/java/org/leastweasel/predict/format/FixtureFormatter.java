/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.format;

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.leastweasel.predict.domain.Fixture;
import org.leastweasel.predict.repository.FixtureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 * A {@link Formatter} to convert {@link Fixture} instances to and from Strings
 * via their unique ID.
 */
@Component
public class FixtureFormatter implements Formatter<Fixture> {

	@Autowired
	private FixtureRepository fixtureRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(FixtureFormatter.class);
	
	/**
	 * Format the given fixture as a String. This is just a String representation of the
	 * fixture's unique ID. Non-persisted fixtures are formatted as an empty string.
	 * 
	 * @param fixture the Fixture to format
	 * @param locale the locale to use (not required)
	 * @return the fixture's ID formatted as a String
	 */
	@Override
	public String print(Fixture fixture, Locale locale) {
		if (fixture == null || fixture.getId() == null) {
			return "";
	    }
		
		// Do the formatting.
	    return String.format(locale, "%d", fixture.getId());
	}
	
	/**
	 * Parse a Fixture from the given String.
	 * 
	 * @param stringToParse the string to parse
	 * @param locale the locale to use (not required)
	 * @return a fixture with an ID of the parsed value, or null if the string is blank,
	 * 			not a valid integer, or a fixture cannot be found with that ID 
	 */
	public Fixture parse(String stringToParse, Locale locale) throws ParseException {
		if (logger.isDebugEnabled()) {
			logger.debug("Trying to parse '{}' into a fixture ID", stringToParse);
		}
		
		if (StringUtils.isBlank(stringToParse)) {
			return null;
		}

		Fixture fixture = null;
		
		if (StringUtils.isNumeric(stringToParse.trim())) {
			Long fixtureId = Long.parseLong(stringToParse.trim());
			
			if (logger.isDebugEnabled()) {
				logger.debug("String '{}' is numeric and can be parsed to: {}", stringToParse, fixtureId);
			}
			
			fixture = fixtureRepository.findOne(fixtureId);
		} else {
			throw new ParseException("String '" + stringToParse + "' connot be converted into a number", 0);
		}

		if (logger.isDebugEnabled()) {
			if (fixture != null) {
				logger.debug("Got a fixture with ID: {}", fixture.getId());
			} else {
				logger.debug("Could not find fixture with ID: {}", stringToParse);
			}
		}
		
		return fixture;
	}
}
