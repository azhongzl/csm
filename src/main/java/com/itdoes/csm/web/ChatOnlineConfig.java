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
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.service.ChatOnlineService;

/**
 * @author Jalen Zhong
 */
@Configuration
public class ChatOnlineConfig {
	private static final String KEY_ADMIN = "admin";

	private static final String TOPIC_LOGIN = "/topic/chat/login";
	private static final String TOPIC_LOGOUT = "/topic/chat/logout";

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private ChatOnlineService onlineService;

	@EventListener
	private void handleSessionConnected(SessionConnectedEvent event) {
		final Message<?> message = event.getMessage();
		// Only monitor customers
		if (!SpringMessagings.getNativeHeaders(message).containsKey(KEY_ADMIN)) {
			final StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
			final String userId = sha.getUser().getName();
			final ChatEvent loginEvent = new ChatEvent(userId);
			template.convertAndSend(TOPIC_LOGIN, loginEvent);
			onlineService.addOnlineSession(sha.getSessionId(), loginEvent);
		}
	}

	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		Optional.ofNullable(onlineService.getOnlineSession(event.getSessionId())).ifPresent(login -> {
			template.convertAndSend(TOPIC_LOGOUT, new ChatEvent(login.getUserId()));
			onlineService.removeOnlineSession(event.getSessionId());
		});
	}
}
