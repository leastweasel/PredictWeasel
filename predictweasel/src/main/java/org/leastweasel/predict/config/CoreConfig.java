/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.config;

import org.jasypt.digest.StandardStringDigester;
import org.leastweasel.predict.domain.DefaultMissingPredictionScoringModel;
import org.leastweasel.predict.domain.DefaultSpotOnScoringModel;
import org.leastweasel.predict.domain.KnockoutStageFixtureFilter;
import org.leastweasel.predict.domain.LivermoreScoringModel;
import org.leastweasel.predict.domain.Prize;
import org.leastweasel.predict.domain.Prizes;
import org.leastweasel.predict.domain.Scorer;
import org.leastweasel.predict.domain.StandingsCache;
import org.leastweasel.predict.service.Clock;
import org.leastweasel.predict.service.PasswordResetTokenGenerator;
import org.leastweasel.predict.service.support.ElapsedTimeClock;
import org.leastweasel.predict.service.support.JasyptPasswordResetTokenGenerator;
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
	
	/**
	 * Create a bean with which we can generate password reset tokens using Jasypt.
	 */
	@Bean
	public PasswordResetTokenGenerator passwordResetTokenGenerator() {
		JasyptPasswordResetTokenGenerator generator = new JasyptPasswordResetTokenGenerator();
		
		StandardStringDigester digester = new StandardStringDigester();
		digester.setAlgorithm("SHA-256");
		digester.setIterations(100);
		
		generator.setStringDigester(digester);
		
		return generator;
	}
	
	/**
	 * Create  a bean that will allow us to query the current date and time,
	 * but not necessarily the real world value, depending on environment.
	 * 
	 * @return the current date and time
	 */
	@Bean
	public Clock systemClock() {
		return new ElapsedTimeClock("2014-06-02T12:00:00+01:00");
	}
	
	/**
	 * Create the bean that holds the various prizes from which a league
	 * creator can choose.
	 *  
	 * @return the collection of configured prizes
	 */
	@Bean
	public Prizes prizes() {
		Prizes prizes = new Prizes();
		
		Scorer mainScorer = new LivermoreScoringModel();
		Scorer missingPredictionScorer = new DefaultMissingPredictionScoringModel(mainScorer);
		
		Prize prize1 = new Prize("O", "Overall", mainScorer);
		prize1.setMissingPredictionScorer(missingPredictionScorer);
		
		Prize prize2 = new Prize("KO", "Knockout", mainScorer);
		prize2.setMissingPredictionScorer(missingPredictionScorer);
		prize2.setFixtureFilters(new KnockoutStageFixtureFilter());

		Prize prize3 = new Prize("SO", "Spot-on", new DefaultSpotOnScoringModel());

		prizes.setPrizes(prize1, prize2, prize3);
		
		return prizes;
	}

	/**
	 * Create the bean that holds the standings for the various leagues.
	 *  
	 * @return the standings cache
	 */
	@Bean
	public StandingsCache standingsCache() {
		return new StandingsCache();
	}
}
