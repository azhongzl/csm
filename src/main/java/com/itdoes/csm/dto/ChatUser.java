package com.itdoes.csm.dto;

/**
 * @author Jalen Zhong
 */
public class ChatUser {
	private final String username;
	private boolean online;
	private boolean unhandled;

	public ChatUser(String username) {
		this.username = username;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isUnhandled() {
		return unhandled;
	}

	public void setUnhandled(boolean unhandled) {
		this.unhandled = unhandled;
	}

	public String getUsername() {
		return username;
	}
}
