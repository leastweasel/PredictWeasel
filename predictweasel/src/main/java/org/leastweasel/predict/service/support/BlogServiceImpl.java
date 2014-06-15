/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.service.support;

import java.util.ArrayList;
import java.util.List;

import org.leastweasel.predict.domain.BlogPost;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.repository.BlogPostRepository;
import org.leastweasel.predict.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link BlogService}. 
 */
@Service
public class BlogServiceImpl implements BlogService {
	@Autowired
	private BlogPostRepository blogPostRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BlogPost getLatestPostForLeague(League league) {
		// Limit the query to return, at most, one entity. We should get the most recent.
		Pageable pageRequest = new PageRequest(0, 1, Direction.DESC, "postTime");
		
		List<BlogPost> posts = blogPostRepository.findByLeague(league, pageRequest);
		
		if (posts == null || posts.isEmpty()) {
			return null;
		}
		
		return posts.get(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BlogPost> getAllPostsForLeague(League league) {
		Sort sortOrder = new Sort(Direction.DESC, "postTime");
		
		List<BlogPost> posts = blogPostRepository.findByLeague(league, sortOrder);
		
		if (posts == null) {
			posts = new ArrayList<>();
		}
		
		return posts;
	}
}

