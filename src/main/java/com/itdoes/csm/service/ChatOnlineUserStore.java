package com.itdoes.csm.service;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.csm.web.ChatEvent;

/**
 * @author Jalen Zhong
 */
@Service
public class ChatOnlineUserStore {
	private final Map<String, ChatEvent> onlineUserMap = Maps.newConcurrentMap();
	private final Set<String> onlineUserSet = Sets.newConcurrentHashSet();

	public void add(String sessionId, ChatEvent event) {
		onlineUserMap.put(sessionId, event);
		onlineUserSet.add(event.getUsername());
	}

	public void remove(String sessionId) {
		final ChatEvent event = onlineUserMap.remove(sessionId);
		onlineUserSet.remove(event.getUsername());
	}

	public ChatEvent get(String sessionId) {
		return onlineUserMap.get(sessionId);
	}

	public Map<String, ChatEvent> getMap() {
		return onlineUserMap;
	}

	public boolean containsUser(String username) {
		return onlineUserSet.contains(username);
	}
}
