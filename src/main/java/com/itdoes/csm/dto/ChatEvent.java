package com.itdoes.csm.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author Jalen Zhong
 */
public class ChatEvent {
	private static final String USER_ID_KEY = "userId";

	private final String operatorId;
	private final LocalDateTime dateTime;
	private final Map<Object, Object> data = Maps.newHashMap();

	public ChatEvent(String operatorId) {
		this(operatorId, LocalDateTime.now());
	}

	public ChatEvent(String operatorId, LocalDateTime dateTime) {
		this.operatorId = operatorId;
		this.dateTime = dateTime;
	}

	public String getOperatorId() {
		return operatorId;
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

	public String getUserId() {
		return (String) data.get(USER_ID_KEY);
	}

	public ChatEvent addUserId(String userId) {
		return addData(USER_ID_KEY, userId);
	}
}
