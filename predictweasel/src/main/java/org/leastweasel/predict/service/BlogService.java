/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import org.leastweasel.predict.domain.BlogPost;
import org.leastweasel.predict.domain.League;

/**
 * An interface for dealing with {@link BlogPost}s.
 */
public interface BlogService {
	/**
	 * Get the most recent post from the given league's blog.
	 * 
	 * @return the most recent blog post, of null if there isn't one
	 */
	BlogPost getLatestPostForLeague(League league);
}
