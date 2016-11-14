package com.itdoes.csm.dto;

import java.time.LocalDateTime;

/**
 * @author Jalen Zhong
 */
public class ChatMessage {
	private String sender;
	private String roomId;
	private LocalDateTime dateTime;
	private String message;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
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
