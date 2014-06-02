/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains the collection of available {@link Prize}s. Each {@link League} can
 * select between 1 and 3 prizes.
 */
public class Prizes {
	private Map<String, Prize> prizes = new HashMap<>();

	/**
	 * Add a list of prizes to the collection, replacing what was there before.
	 * 
	 * @param prizes the new collection of prizes
	 */
	public void setPrizes(Prize... prizes) {
		this.prizes.clear();
		
		if (prizes != null) {
			for (Prize prize : prizes) {
				if (this.prizes.get(prize.getCode()) != null) { 
					throw new IllegalStateException("Two prizes exist with the same code: '" + prize.getCode() + "'");
				}
				
				this.prizes.put(prize.getCode(), prize);
			}
		}
	}
	
	/**
	 * Get the prize with the given code.
	 * 
	 * @return the prize with the given code, or null if one doesn't exist
	 */
	public Prize getPrizeForCode(String code) {
		return prizes.get(code);
	}
}
