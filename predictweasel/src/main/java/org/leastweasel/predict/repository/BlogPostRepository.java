/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.leastweasel.predict.domain.BlogPost;
import org.leastweasel.predict.domain.League;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@BlogPost} objects.
 */
public interface BlogPostRepository extends CrudRepository<BlogPost, Long> {
    /**
     * Find the most recent blog post for the given league.
     *
     * @param league whose blog post we're after
     * @return a list of one or no blog posts
     */
     List<BlogPost>findByLeague(League league, Pageable pageable);
}
