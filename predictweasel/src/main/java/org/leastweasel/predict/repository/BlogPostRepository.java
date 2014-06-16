/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.repository;

import java.util.List;

import org.leastweasel.predict.domain.BlogPost;
import org.leastweasel.predict.domain.League;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access interface for performing database operations on
 * {@BlogPost} objects.
 */
public interface BlogPostRepository extends CrudRepository<BlogPost, Long> {
    /**
     * Fetch some blog posts for the given league. The {@link Pageable} parameter
     * determines how many to return and the order in which they're sorted. 
     *
     * @param league whose blog posts we're after
     * @return a list of blog posts
     */
     List<BlogPost>findByLeague(League league, Pageable pageable);

     /**
      * Fetch some blog posts for the given league. The {@link Sort} parameter
      * determines the order in which they're sorted. 
      *
      * @param league whose blog posts we're after
      * @return a list of blog posts
      */
     List<BlogPost>findByLeague(League league, Sort pageable);
}
