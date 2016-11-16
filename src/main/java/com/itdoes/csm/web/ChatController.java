package com.itdoes.csm.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.dto.ChatUser;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.service.ChatService;

/**
 * @author Jalen Zhong
 */
@Controller
public class ChatController {
	@Autowired
	private ChatService chatService;

	private final SimpMessagingTemplate template;

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
	public void chatCSendMessage(CsmChatMessage message, Principal principal) {
		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		final UUID userIdUuid = UUID.fromString(userId);
		message.setRoomId(userIdUuid);
		message.setSenderId(userIdUuid);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(false);
		template.convertAndSend("/topic/chat/message/" + userId, message);

		final ChatEvent messageEvent = new ChatEvent(userId);
		template.convertAndSend("/topic/chat/addUnhandledCustomer", messageEvent);
		chatService.addUnhandledCustomer(messageEvent);

		chatService.saveChatMessage(message);
	}

	@SubscribeMapping("/chatCInitMessage")
	public List<CsmChatMessage> chatCInitMessage(Principal principal) {
		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		List<CsmChatMessage> messageList = chatService.getChatMessageList(userId);
		if (Collections3.isEmpty(messageList)) {
			messageList = Lists.newArrayListWithCapacity(1);
		}

		final CsmChatMessage message = new CsmChatMessage();
		message.setSenderName("Customer Service");
		message.setCreateDateTime(LocalDateTime.now());
		message.setMessage("Welcome, " + shiroUser.getUsername() + "! Our agent will contact you soon. Please wait...");
		message.setFromAdmin(true);
		messageList.add(message);

		return messageList;
	}

	@SubscribeMapping("/chatAInit")
	public List<ChatUser> chatAInit() {
		return chatService.getCustomerList();
	}

	@MessageMapping("/chatASendMessage")
	public void chatASendMessage(CsmChatMessage message, Principal principal) {
		final UUID roomIdUuid = message.getRoomId();
		Validate.notNull(roomIdUuid, "RoomId should not be null");

		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		final UUID userIdUuid = UUID.fromString(userId);
		message.setSenderId(userIdUuid);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		template.convertAndSend("/topic/chat/message/" + roomIdUuid, message);

		final ChatEvent messageEvent = new ChatEvent(roomIdUuid.toString());
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
		chatService.removeUnhandledCustomer(messageEvent.getUserId());

		chatService.saveChatMessage(message);
	}

	@SubscribeMapping("/chatAInitMessage/{roomId}")
	public List<CsmChatMessage> chatAInitMessage(@DestinationVariable String roomId) {
		return chatService.getChatMessageList(roomId);
	}
}
