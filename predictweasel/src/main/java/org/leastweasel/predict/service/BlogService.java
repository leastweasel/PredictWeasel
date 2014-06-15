/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service;

import java.util.List;

import org.leastweasel.predict.domain.BlogPost;
import org.leastweasel.predict.domain.League;

/**
 * An interface for dealing with {@link BlogPost}s.
 */
public interface BlogService {
	/**
	 * Get all posts for league.
	 * 
	 * @param league the league whose posts we're after
	 * @return a list of posts for the given league
	 */
	List<BlogPost> getAllPostsForLeague(League league);
	
	/**
	 * Get the most recent post from the given league's blog.
	 * 
	 * @param league the league whose most recent post we're after
	 * @return the most recent blog post, of null if there isn't one
	 */
	BlogPost getLatestPostForLeague(League league);
}
