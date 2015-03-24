package com.app.nyumbakumi.entity;

public class NotiMessage {
	private String sender, message, timeDuration;
	
	/**
	 * 
	 * @param sender
	 * @param message
	 * @param timeDuration
	 */
	public NotiMessage(String sender, String message, String timeDuration) {
		this.setSender(sender);
		this.setMessage(message);
		this.setTimeDuration(timeDuration);
	}

	public NotiMessage() {
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(String timeDuration) {
		this.timeDuration = timeDuration;
	}
}
