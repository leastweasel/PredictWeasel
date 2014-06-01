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
	$("#upcoming-fixtures input[name='prediction'").blur(function(event) {
		var predictionFieldId = $(this).attr("id").valueOf();
		var fixtureId = predictionFieldId.substr(predictionFieldId.indexOf("_") + 1,predictionFieldId.length);
		var predictionText = $(this).val();
		var originalPredictionText = $(this).next().val();
		var CSRFToken = $('#_csrf').val();
		
		if (predictionText != originalPredictionText) {
			$.post('/league/savePrediction.html',
	                {fixture: fixtureId,
	                 prediction: predictionText,
	                 _csrf: CSRFToken},
	                 function() {
	                 }, 'json');
			
			// Update the "original value" field so that we don't keep triggering events.
			$(this).next().val(predictionText); 
		}
	})
})