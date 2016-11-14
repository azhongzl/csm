package com.itdoes.csm.web;

import java.time.LocalDateTime;

/**
 * @author Jalen Zhong
 */
public class ChatMessage {
	private String sender;
	private String recipient;
	private LocalDateTime dateTime;
	private String message;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
