package com.itdoes.csm.dto;

import java.time.LocalDateTime;

/**
 * @author Jalen Zhong
 */
public class ChatMessage {
	private String roomId;
	private String senderId;
	private String senderName;
	private LocalDateTime createDateTime;
	private boolean fromAdmin;
	private String message;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public boolean isFromAdmin() {
		return fromAdmin;
	}

	public void setFromAdmin(boolean fromAdmin) {
		this.fromAdmin = fromAdmin;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
