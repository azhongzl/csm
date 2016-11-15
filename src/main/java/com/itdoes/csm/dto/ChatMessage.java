package com.itdoes.csm.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.itdoes.csm.entity.CsmChatMessage;

/**
 * @author Jalen Zhong
 */
public class ChatMessage {
	private String roomId;
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

	public CsmChatMessage toCsmChatMessage() {
		final CsmChatMessage csmChatMessage = new CsmChatMessage();
		csmChatMessage.setRoomId(UUID.fromString(roomId));
		csmChatMessage.setCreateDateTime(createDateTime);
		csmChatMessage.setFromAdmin(fromAdmin);
		csmChatMessage.setMessage(message);
		return csmChatMessage;
	}

	public static ChatMessage valueOf(CsmChatMessage csmChatMessage) {
		final ChatMessage chatMessage = new ChatMessage();
		chatMessage.setRoomId(csmChatMessage.getRoomId().toString());
		chatMessage.setCreateDateTime(csmChatMessage.getCreateDateTime());
		chatMessage.setFromAdmin(csmChatMessage.getFromAdmin());
		chatMessage.setMessage(csmChatMessage.getMessage());
		return chatMessage;
	}
}
