package com.itdoes.csm.service;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.csm.dto.ChatEvent;

/**
 * @author Jalen Zhong
 */
@Service
public class ChatOnlineService extends BaseService {
	private final Map<String, ChatEvent> onlineSessionMap = Maps.newConcurrentMap();
	private final Set<String> onlineUserIdSet = Sets.newConcurrentHashSet();

	public void addOnlineSession(String sessionId, ChatEvent event) {
		onlineSessionMap.put(sessionId, event);
		onlineUserIdSet.add(event.getData().get("userId").toString());
	}

	public void removeOnlineSession(String sessionId) {
		final ChatEvent event = onlineSessionMap.remove(sessionId);
		onlineUserIdSet.remove(event.getData().get("userId").toString());
	}

	public ChatEvent getOnlineSession(String sessionId) {
		return onlineSessionMap.get(sessionId);
	}

	public Map<String, ChatEvent> getOnlineSessionMap() {
		return onlineSessionMap;
	}

	public boolean isOnlineUser(String userId) {
		return onlineUserIdSet.contains(userId);
	}
}
