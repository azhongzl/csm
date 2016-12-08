package com.itdoes.csm.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author Jalen Zhong
 */
public class ChatEvent {
	private final String userId;
	private final LocalDateTime dateTime;
	private final Map<Object, Object> data = Maps.newHashMap();

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

	public Map<Object, Object> getData() {
		return data;
	}

	public ChatEvent addData(Object key, Object value) {
		data.put(key, value);
		return this;
	}
}
