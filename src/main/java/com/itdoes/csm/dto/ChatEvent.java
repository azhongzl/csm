package com.itdoes.csm.dto;

import java.time.LocalDateTime;

/**
 * @author Jalen Zhong
 */
public class ChatEvent {
	private final String username;
	private final LocalDateTime dateTime;

	public ChatEvent(String username) {
		this.username = username;
		this.dateTime = LocalDateTime.now();
	}

	public String getUsername() {
		return username;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
}
