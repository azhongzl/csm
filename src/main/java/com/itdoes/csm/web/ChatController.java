package com.itdoes.csm.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
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
		final String username = principal.getName();
		message.setSender(username);
		message.setDateTime(LocalDateTime.now());
		message.setMessage(message.getMessage());
		template.convertAndSend("/topic/chat/message/" + username, message);
		addChatMessage(username, message);

		final ChatEvent messageEvent = new ChatEvent(username);
		unHandledCustomerMap.put(username, messageEvent);
		template.convertAndSend("/topic/chat/unhandledCustomer", messageEvent);
	}

	@SubscribeMapping("/chatCInitMessage")
	public List<ChatMessage> chatCInitMessage(Principal principal) {
		final String username = principal.getName();
		List<ChatMessage> messageList = messageMap.get(username);
		if (Collections3.isEmpty(messageList)) {
			final ChatMessage message = new ChatMessage();
			message.setSender("Customer Service");
			message.setDateTime(LocalDateTime.now());
			message.setMessage("Welcome, " + username + "! Our agent will contact you soon. Please wait...");
			addChatMessage(username, message);
		}

		return messageList;
	}

	private void addChatMessage(String username, ChatMessage message) {
		List<ChatMessage> messageList = messageMap.get(username);
		if (Collections3.isEmpty(messageList)) {
			messageList = Lists.newArrayList();
			messageMap.put(username, messageList);
		}
		messageList.add(message);
	}

	@SubscribeMapping("/chatAInit")
	public Collection<ChatUserDto> chatAInit() {
		final Collection<ChatUserDto> users = Arrays.asList(new ChatUserDto("admin"), new ChatUserDto("user"));
		for (ChatUserDto user : users) {
			final String username = user.getUsername();
			if (onlineUserStore.containsUser(username)) {
				user.setOnline(true);
			}
			if (unHandledCustomerMap.containsKey(username)) {
				user.setUnhandled(true);
			}
		}
		return users;
	}

	@MessageMapping("/chatASendMessage")
	public void chatASendMessage(ChatMessage message, Principal principal) {
		final String username = principal.getName();
		message.setSender(username);
		message.setDateTime(LocalDateTime.now());
		message.setMessage(message.getMessage());
		template.convertAndSend("/topic/chat/message/" + message.getRecipient(), message);
		addChatMessage(message.getRecipient(), message);

		unHandledCustomerMap.remove(message.getRecipient());
	}

	@SubscribeMapping("/chatAInitMessage/{customerName}")
	public List<ChatMessage> chatAInitMessage(Principal principal, @DestinationVariable String customerName) {
		List<ChatMessage> messageList = messageMap.get(customerName);
		return messageList;
	}
}
