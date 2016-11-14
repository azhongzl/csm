package com.itdoes.csm.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.dto.ChatMessage;
import com.itdoes.csm.dto.ChatUser;
import com.itdoes.csm.service.ChatOnlineUserStore;

/**
 * @author Jalen Zhong
 */
@Controller
public class ChatController {
	private final SimpMessagingTemplate template;

	@Autowired
	private ChatOnlineUserStore onlineUserStore;

	private Map<String, List<ChatMessage>> messageMap = Maps.newHashMap();

	private Map<String, ChatUser> userMap = Maps.newHashMap();
	{
		userMap.put("1c93b13d-d6ea-1034-a268-c6b53a0158b7",
				new ChatUser("1c93b13d-d6ea-1034-a268-c6b53a0158b7", "admin"));
		userMap.put("490aa897-d6ea-1034-a268-c6b53a0158b7",
				new ChatUser("490aa897-d6ea-1034-a268-c6b53a0158b7", "user"));
	}

	private Map<String, ChatEvent> unHandledCustomerMap = Maps.newHashMap();

	@Autowired
	public ChatController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@RequestMapping("/chat")
	public String chat() {
		return "chat";
	}

	@RequestMapping("/admin/chat")
	public String adminChat() {
		return "admin/chat";
	}

	@RequestMapping("/jalenChat")
	public String jalenChat() {
		return "jalenChat";
	}

	@RequestMapping("/admin/jalenChat")
	public String jalenAdminChat() {
		return "admin/jalenChat";
	}

	@MessageMapping("/chatCSendMessage")
	public void chatCSendMessage(ChatMessage message, Principal principal) {
		final String userId = principal.getName();
		message.setSenderId(userId);
		message.setSenderName(userMap.get(userId).getUsername());
		message.setDateTime(LocalDateTime.now());
		message.setMessage(message.getMessage());
		template.convertAndSend("/topic/chat/message/" + userId, message);
		addChatMessage(userId, message);

		final ChatEvent messageEvent = new ChatEvent(userId);
		unHandledCustomerMap.put(userId, messageEvent);
		template.convertAndSend("/topic/chat/unhandledCustomer", messageEvent);
	}

	@SubscribeMapping("/chatCInitMessage")
	public List<ChatMessage> chatCInitMessage(Principal principal) {
		final String userId = principal.getName();
		List<ChatMessage> messageList = messageMap.get(userId);
		if (Collections3.isEmpty(messageList)) {
			final ChatMessage message = new ChatMessage();
			message.setSenderId("Customer Service Id");
			message.setSenderName("Customer Service");
			message.setDateTime(LocalDateTime.now());
			message.setMessage("Welcome, " + userMap.get(userId).getUsername()
					+ "! Our agent will contact you soon. Please wait...");
			addChatMessage(userId, message);
		}

		return messageList;
	}

	private void addChatMessage(String userId, ChatMessage message) {
		List<ChatMessage> messageList = messageMap.get(userId);
		if (Collections3.isEmpty(messageList)) {
			messageList = Lists.newArrayList();
			messageMap.put(userId, messageList);
		}
		messageList.add(message);
	}

	@SubscribeMapping("/chatAInit")
	public Collection<ChatUser> chatAInit() {
		final Collection<ChatUser> users = userMap.values();
		for (ChatUser user : users) {
			final String userId = user.getUserId();
			if (onlineUserStore.containsUser(userId)) {
				user.setOnline(true);
			}
			if (unHandledCustomerMap.containsKey(userId)) {
				user.setUnhandled(true);
			}
		}
		return users;
	}

	@MessageMapping("/chatASendMessage")
	public void chatASendMessage(ChatMessage message, Principal principal) {
		final String userId = principal.getName();
		message.setSenderId(userId);
		message.setSenderName(userMap.get(userId).getUsername());
		message.setDateTime(LocalDateTime.now());
		message.setMessage(message.getMessage());
		template.convertAndSend("/topic/chat/message/" + message.getRoomId(), message);
		addChatMessage(message.getRoomId(), message);

		unHandledCustomerMap.remove(message.getRoomId());
	}

	@SubscribeMapping("/chatAInitMessage/{customerName}")
	public List<ChatMessage> chatAInitMessage(Principal principal, @DestinationVariable String customerName) {
		List<ChatMessage> messageList = messageMap.get(customerName);
		return messageList;
	}
}
