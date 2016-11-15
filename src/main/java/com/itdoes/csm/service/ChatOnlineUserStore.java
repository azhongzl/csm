package com.itdoes.csm.service;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.csm.dto.ChatEvent;

/**
 * @author Jalen Zhong
 */
@Service
public class ChatOnlineUserStore {
	private final Map<String, ChatEvent> onlineSessionMap = Maps.newConcurrentMap();
	private final Set<String> onlineUserSet = Sets.newConcurrentHashSet();

	public void add(String sessionId, ChatEvent event) {
		onlineSessionMap.put(sessionId, event);
		onlineUserSet.add(event.getUserId());
	}

	public void remove(String sessionId) {
		final ChatEvent event = onlineSessionMap.remove(sessionId);
		onlineUserSet.remove(event.getUserId());
	}

	public ChatEvent get(String sessionId) {
		return onlineSessionMap.get(sessionId);
	}

	public Map<String, ChatEvent> getMap() {
		return onlineSessionMap;
	}

	public boolean containsUser(String userId) {
		return onlineUserSet.contains(userId);
	}
}
