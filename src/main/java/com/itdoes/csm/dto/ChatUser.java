package com.itdoes.csm.dto;

import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
public class ChatUser {
	public static ChatUser valueOf(CsmUser user) {
		return new ChatUser(user.getId().toString(), user.getUsername());
	}

	private final String userId;
	private final String username;
	private boolean online;
	private boolean unhandled;

	public ChatUser(String userId, String username) {
		this.userId = userId;
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
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
}
