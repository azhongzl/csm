package com.itdoes.csm.dto;

import java.time.LocalDateTime;

/**
 * @author Jalen Zhong
 */
public class ChatEvent {
	private final String userId;
	private final LocalDateTime dateTime;

	public ChatEvent(String userId) {
		this(userId, LocalDateTime.now());
	}

	public ChatEvent(String userId, LocalDateTime dateTime) {
		this.userId = userId;
		this.dateTime = dateTime;
	}

	public String getUserId() {
		return userId;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
}
