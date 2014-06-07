/**
 * This event handler is called to signify that the page is ready as the DOM
 * has been fully initialised.
 */
$(document).ready(function() {
	/**
	 * Register a blur event handler (triggered when the field loses input focus) for each
	 * of the edit fields where the user will enter their prediction for a given fixture.
	 * 
	 * Each input field will be given an ID in the form 'fixture_<fixtureId>' (e.g. 'fixture_21').
	 * When the user tabs out of the field a request will be made to save the prediction.
	 */
	$(":input[name='prediction']").blur(function(event) {
		
		var predictionFieldId = $(this).attr("id").valueOf();
		var fixtureId = predictionFieldId.substr(predictionFieldId.indexOf("_") + 1,predictionFieldId.length);
		var predictionText = $(this).val();
		var originalPredictionText = $(this).next().val();
		var CSRFToken = $('#_csrf').val();
		var inputField = $(this);
		var messageSpan = inputField.parent().next().next().children(); 

		// Clear any error messages.
		messageSpan.html('');
		messageSpan.show();

		// Only submit the request if the prediction has changed.
		
		if (predictionText != originalPredictionText) {
			$.post('/league/savePrediction',
	                {fixture: fixtureId,
	                 predictionText: predictionText,
	                 _csrf: CSRFToken},
	                 function(data) {
	                	 	// If there was a problem with the format of the prediction there
	                	 	// will be an "errorText" value in the response.
	                	 	var errorText = data['errorText'];
	                	 	
	                	 	if (errorText) {
	                	 		// As well as displaying the error message we reset the prediction
	                	 		// text and put back the focus in the field that caused the error.
	                	 		messageSpan.removeClass('text-info').addClass('text-danger');
	                	 		messageSpan.html(errorText);
	                	 		inputField.val(originalPredictionText);
	                	 		inputField.focus();
	                	 	} else {
		                	 	// The response contains the updated prediction, which will have been formatted properly.
		                	 	var updatedPredictionText = data['predictionText'];
		                	 	var infoText = data['infoText'];

		                	 	// Display an information message, if there is one.
		                	 	if (infoText) {
		                	 		messageSpan.removeClass('text-danger').addClass('text-info');
		                	 		messageSpan.html(infoText);
		                	 		messageSpan.fadeOut(1000);
		                	 	}

		         			// Update the "original value" field so that we don't keep triggering events, and
		                	 	// the prediction input field so that we get proper formatting of the value.
		         			inputField.val(updatedPredictionText); 
		         			inputField.next().val(updatedPredictionText);
	                	 	}

	                 }).fail(function(qXHR, textStatus, errorThrown) {
	                	 				alert("Fail! status='" + textStatus + "', error='" + errorThrown + "'");
	                 			});
		}
	});
})