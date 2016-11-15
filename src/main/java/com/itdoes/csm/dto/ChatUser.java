package com.itdoes.csm.dto;

import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
public class ChatUser {
	public static ChatUser valueOf(CsmUser user) {
		return new ChatUser(user.getId().toString(), user.getUsername());
	}

	private final String id;
	private final String username;
	private boolean online;
	private boolean unhandled;

	public ChatUser(String id, String username) {
		this.id = id;
		this.username = username;
	}

	public String getId() {
		return id;
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
