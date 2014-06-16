/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

import java.util.List;

import org.leastweasel.predict.domain.BlogPost;
import org.leastweasel.predict.domain.League;
import org.leastweasel.predict.domain.User;
import org.leastweasel.predict.domain.UserSubscription;
import org.leastweasel.predict.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that allows a user to navigate to the page showing the full blog
 * for a single {@link League}. The league in question will be the {@link User}'s 
 * 'current' league. It  will have been predetermined so is not passed in as a parameter.
 */
@Controller
public class ViewLeagueBlogController {
	
	@Autowired
	private BlogService blogService;
	
	private static final Logger logger = LoggerFactory.getLogger(ViewLeagueBlogController.class);
	
	/**
	 * Handle a GET request to navigate to the league blog.     
	 * 
	 * @param subscription the user and league for which we will be displaying the blog
	 * @return the name of the view to navigate to (the league blog page)
	 */
	@RequestMapping(value="/league/blog", method = RequestMethod.GET)
	public String navigateToPage(UserSubscription subscription) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering full blog view of league with code: {}",
						 subscription.getLeague().getCode());
		}
		
		return "leagueBlog";
	}
	
	/**
	 * Get the full list of blog posts for the current league. They'll be sorted in
	 * reverse order of post date (i.e. newest first).    
	 * 
	 * @param subscription identifies the user and the league they're currently playing
	 * @return a list of blog posts for the current league
	 */
	@ModelAttribute("blogPosts")
	public List<BlogPost> getBlogPosts(UserSubscription subscription) {
		return blogService.getAllPostsForLeague(subscription.getLeague());
	}
	
	/**
	 * Set up the current league, so that we can show details of it in the view.     
	 * 
	 * @return the league currently being played by the logged in user
	 */
	@ModelAttribute("currentLeague")
	public League getCurrentLeague(UserSubscription subscription) {
		return subscription.getLeague();
	}
}
