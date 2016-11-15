package com.itdoes.csm.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.dto.ChatMessage;
import com.itdoes.csm.dto.ChatUser;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.service.ChatOnlineUserStore;

/**
 * @author Jalen Zhong
 */
@Controller
public class ChatController {
	@Autowired
	private EntityEnv env;

	@Autowired
	private EntityDbService dbService;

	private Specification<CsmUser> customerSpec = Specifications.build(CsmUser.class,
			Lists.newArrayList(new FindFilter("admin", Operator.EQ, false)));

	private Sort customerSort = new Sort(Direction.ASC, "username");

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
		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		message.setSenderId(userId);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setMessage(message.getMessage());
		template.convertAndSend("/topic/chat/message/" + userId, message);
		addChatMessage(userId, message);

		final ChatEvent messageEvent = new ChatEvent(userId);
		unHandledCustomerMap.put(userId, messageEvent);
		template.convertAndSend("/topic/chat/addUnhandledCustomer", messageEvent);
	}

	@SubscribeMapping("/chatCInitMessage")
	public List<ChatMessage> chatCInitMessage(Principal principal) {
		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		List<ChatMessage> messageList = messageMap.get(userId);
		if (Collections3.isEmpty(messageList)) {
			final ChatMessage message = new ChatMessage();
			message.setSenderId("Customer Service Id");
			message.setSenderName("Customer Service");
			message.setCreateDateTime(LocalDateTime.now());
			message.setMessage(
					"Welcome, " + shiroUser.getUsername() + "! Our agent will contact you soon. Please wait...");
			message.setFromAdmin(true);
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
		final List<CsmUser> csmUserList = dbService.findAll(env.getPair(CsmUser.class.getSimpleName()), customerSpec,
				customerSort);

		final List<ChatUser> chatCustomerList = Lists.newArrayListWithCapacity(csmUserList.size());
		for (CsmUser csmUser : csmUserList) {
			final ChatUser chatUser = ChatUser.valueOf(csmUser);
			if (onlineUserStore.containsUser(chatUser.getUserId())) {
				chatUser.setOnline(true);
			}
			if (unHandledCustomerMap.containsKey(chatUser.getUserId())) {
				chatUser.setUnhandled(true);
			}
			chatCustomerList.add(chatUser);
		}
		return chatCustomerList;
	}

	@MessageMapping("/chatASendMessage")
	public void chatASendMessage(ChatMessage message, Principal principal) {
		final String userId = principal.getName();
		message.setSenderId(userId);
		message.setSenderName("Customer Service - " + userId);
		message.setCreateDateTime(LocalDateTime.now());
		message.setMessage(message.getMessage());
		message.setFromAdmin(true);
		template.convertAndSend("/topic/chat/message/" + message.getRoomId(), message);
		addChatMessage(message.getRoomId(), message);

		final ChatEvent messageEvent = new ChatEvent(message.getRoomId());
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
		unHandledCustomerMap.remove(message.getRoomId());
	}

	@SubscribeMapping("/chatAInitMessage/{customerName}")
	public List<ChatMessage> chatAInitMessage(Principal principal, @DestinationVariable String customerName) {
		List<ChatMessage> messageList = messageMap.get(customerName);
		return messageList;
	}
}
