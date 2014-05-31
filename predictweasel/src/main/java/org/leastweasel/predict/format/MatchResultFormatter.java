/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.format;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.leastweasel.predict.domain.MatchResult;
import org.springframework.format.Formatter;

/**
 * A {@link Formatter} to convert {@link MatchResult} instances to and from Strings.
 */
public class MatchResultFormatter implements Formatter<MatchResult> {
	/**
	 * The regular expression pattern for identifying valid MatchResult strings. It
	 * matches one to three digits for the home score (who's going to score 1000 goals?),
	 * an optional separator, which should be a space, a 'v' or a '-', surrounded by as
	 * many spaces as desired. Input strings should be trimmed before an attempted match is made.
	 */
	private static final Pattern parsingPattern = Pattern.compile("\\d{1,3} *[v\\- ] *\\d{1,3}");
	
	/**
	 * Format the given match result as a String. Null results are formatted as
	 * an empty string.
	 * 
	 * @param result the MatchResult to format
	 * @param locale the locale to use (not required)
	 * @return the result formatted as a String
	 */
	@Override
	public String print(MatchResult result, Locale locale) {
		if (result == null) {
			return "";
	    }
		
		// Do the formatting.
	    return String.format(locale, "%d - %d", result.getHomeScore(), result.getAwayScore());
	}
	
	/**
	 * Parse a MatchResult from the given String.
	 * 
	 * @param stringToParse the string to parse
	 * @param locale the locale to use (not required)
	 * @return a parsed MatchResult, or null if the string is blank 
	 */
	public MatchResult parse(String stringToParse, Locale locale) throws ParseException {
		if (StringUtils.isBlank(stringToParse)) {
			return null;
		}
		
		MatchResult result = new MatchResult();
		Matcher m = parsingPattern.matcher(stringToParse.trim());
		
		if (m.matches()) {
			result.setHomeScore(Integer.parseInt(m.group(1)));
			result.setAwayScore(Integer.parseInt(m.group(3)));
		} else {
			throw new ParseException("String '" + stringToParse + "' connot be converted into a match result", 0);
		}
		
		System.out.println("Got a match result: " + result);
		
		return result;
	}
}
