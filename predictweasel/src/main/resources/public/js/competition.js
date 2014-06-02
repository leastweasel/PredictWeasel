/**
 * This event handler is called to signify that the page is ready as the DOM
 * has been fully initialised.
 */
$(document).ready(function() {
	/**
	 * Register a click event handler for each of the links below the drop
	 * down list that indicate they operate on an existing competition by 
	 * having a class of "requires-competition".
	 */
	$("#competition-links li.requires-competition").children("a").click(function(event) {
		// Disable normal behaviour of the link.
		event.preventDefault();

		// Tack the ID of the currently selected competition on to the end of the
		// default link so that we operate on that competition.
		var modifiedLink = $(this).attr('href') + '?competition=' + $('#competitions-list').val();
		
		alert("Click event handler: " + modifiedLink);
		window.location.href = modifiedLink;
	});
});
		
