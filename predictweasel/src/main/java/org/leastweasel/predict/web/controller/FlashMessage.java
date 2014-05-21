/**
 * The Least Weasel Organisation
 * Copyright (C) 2004-2014 by Andrew Gillies
 */
package org.leastweasel.predict.web.controller;

/**
 * A message to be displayed to a user, with an optional status that could affect the way in which it is displayed.
 */
public class FlashMessage {
	private String message;
	private Status status;
	
	/**
	 * Constructor.
	 * 
	 * @param message the message that will be displayed
	 * @param ststus the status of the message (error, info, etc) 
	 */
	public FlashMessage(String message, Status status) {
		this.message = message;
		this.status = status;
	}

	/**
	 * Get the text of the flash message.
	 * 
	 * @return the message text
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Get the name of the CSS class that should be used to render this message. If the
	 * status has not been set then no class will be returned, otherwise the class will
	 * be fetched from the enum value.
	 * 
	 * @return the CSS class name to use to render the message
	 */
	public String getCssClass() {
		if (status == null) {
			return "";
		} else {
			return status.getCssClass();
		}
	}
	
	/**
	 * An enum to define the various types of message status we can deal with. 
	 */
	public enum Status {
		SUCCESS("alert-success"),
		
		INFO("alert-info"),
		
		WARNING("alert-warning"),
		
		ERROR("dalert-anger");
		
		private final String cssClass;
		
		Status (String cssClass) {
			this.cssClass = cssClass;
		}
		
		public String getCssClass() {
			return cssClass;
		}
	}
}
