package com.itdoes.csm.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.itdoes.common.core.spring.SpringMessagings;
import com.itdoes.csm.service.ChatOnlineUserStore;

/**
 * @author Jalen Zhong
 */
@Configuration
public class ChatConfig {
	private static final String KEY_ADMIN = "admin";

	private static final String TOPIC_LOGIN = "/topic/chat/login";
	private static final String TOPIC_LOGOUT = "/topic/chat/logout";

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private ChatOnlineUserStore onlineUserStore;

	@EventListener
	private void handleSessionConnected(SessionConnectedEvent event) {
		final Message<?> message = event.getMessage();
		final StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
		final String username = sha.getUser().getName();

		if (!SpringMessagings.getNativeHeaders(message).containsKey(KEY_ADMIN)) {
			final ChatEvent loginEvent = new ChatEvent(username);
			template.convertAndSend(TOPIC_LOGIN, loginEvent);
			onlineUserStore.add(sha.getSessionId(), loginEvent);
		}
	}

	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		Optional.ofNullable(onlineUserStore.get(event.getSessionId())).ifPresent(login -> {
			template.convertAndSend(TOPIC_LOGOUT, new ChatEvent(login.getUsername()));
			onlineUserStore.remove(event.getSessionId());
		});
	}
}
