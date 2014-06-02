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
		
		window.location.href = modifiedLink;
	});
	
	/**
	 * Register a blur event handler (triggered when the field loses input focus) for each
	 * of the edit fields where the user will enter the result for a given fixture.
	 * 
	 * Each input field will be given an ID in the form 'fixture_<fixtureId>' (e.g. 'fixture_21').
	 * When the user tabs out of the field a request will be made to save the result.
	 */
	$(":input[name='fixture']").blur(function(event) {
		
		var resultFieldId = $(this).attr("id").valueOf();
		var fixtureId = resultFieldId.substr(resultFieldId.indexOf("_") + 1,resultFieldId.length);
		var resultText = $(this).val();
		var originalResultText = $(this).next().val();
		var CSRFToken = $('#_csrf').val();
		var inputField = $(this);
		var errorMessageSpan = inputField.parent().next().children(); 

		// Clear any error messages.
		errorMessageSpan.html('');

		// Only submit the request if it has changed.
		
		if (resultText != originalResultText) {
			$.post('/competition/saveResult',
	                {fixture: fixtureId,
	                 resultText: resultText,
	                 _csrf: CSRFToken},
	                 function(data) {
	                	 	// If there was a problem with the format of the prediction there
	                	 	// will be an "errorText" value in the response.
	                	 	var errorText = data['errorText'];
	                	 	
	                	 	if (errorText) {
	                	 		// As well as displaying the error message we reset the prediction
	                	 		// text and put back the focus in the field that caused the error.
	                	 		errorMessageSpan.html(errorText);
	                	 		inputField.val(originalResultText);
	                	 		inputField.focus();
	                	 	} else {
		                	 	// The response contains the updated result, which will have been formatted properly.
		                	 	var updatedResultText = data['resultText'];
		                	 	
		         			// Update the "original value" field so that we don't keep triggering events, and
		                	 	// the result input field so that we get proper formatting of the value.
		         			inputField.val(updatedResultText); 
		         			inputField.next().val(updatedResultText);
	                	 	}

	                 }).fail(function(qXHR, textStatus, errorThrown) {
	                	 				alert("Fail! status='" + textStatus + "', error='" + errorThrown + "'");
	                 			});
		}
	});
});
		
