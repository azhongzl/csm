package com.itdoes.csm.web;

/**
 * @author Jalen Zhong
 */
public class ChatUserDto {
	private final String username;
	private boolean online;
	private boolean unhandled;

	public ChatUserDto(String username) {
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
