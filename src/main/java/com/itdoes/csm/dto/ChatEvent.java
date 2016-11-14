package com.itdoes.csm.dto;

import java.time.LocalDateTime;

/**
 * @author Jalen Zhong
 */
public class ChatEvent {
	private final String userId;
	private final LocalDateTime dateTime;

	public ChatEvent(String userId) {
		this.userId = userId;
		this.dateTime = LocalDateTime.now();
	}

	public String getUserId() {
		return userId;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
}
