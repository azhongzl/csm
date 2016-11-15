package com.itdoes.csm.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.service.ChatOnlineUserStore;

/**
 * @author Jalen Zhong
 */
@Controller
public class ChatController {
	public static final int MESSAGE_PAGE_SIZE = 10;

	@Autowired
	private EntityEnv env;

	@Autowired
	private EntityDbService dbService;

	private Specification<CsmUser> customerSpec = Specifications.build(CsmUser.class,
			Lists.newArrayList(new FindFilter("admin", Operator.EQ, false)));
	private Sort customerSort = new Sort(Direction.ASC, "username");

	private PageRequest messagePageRequest = new PageRequest(0, MESSAGE_PAGE_SIZE,
			new Sort(Direction.DESC, "createDateTime"));

	private final SimpMessagingTemplate template;

	@Autowired
	private ChatOnlineUserStore onlineCustomerStore;

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
		message.setRoomId(userId);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		template.convertAndSend("/topic/chat/message/" + userId, message);

		final CsmChatMessage csmChatMessage = message.toCsmChatMessage();
		csmChatMessage.setSenderId(UUID.fromString(userId));
		dbService.save(env.getPair(CsmChatMessage.class.getSimpleName()), csmChatMessage);

		final ChatEvent messageEvent = new ChatEvent(userId);
		unHandledCustomerMap.put(userId, messageEvent);
		template.convertAndSend("/topic/chat/addUnhandledCustomer", messageEvent);
	}

	@SubscribeMapping("/chatCInitMessage")
	public List<ChatMessage> chatCInitMessage(Principal principal) {
		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		List<ChatMessage> chatMessageList = getChatMessageList(userId);

		if (Collections3.isEmpty(chatMessageList)) {
			chatMessageList = Lists.newArrayListWithCapacity(1);

			final ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSenderName("Customer Service");
			chatMessage.setCreateDateTime(LocalDateTime.now());
			chatMessage.setMessage(
					"Welcome, " + shiroUser.getUsername() + "! Our agent will contact you soon. Please wait...");
			chatMessage.setFromAdmin(true);
			chatMessageList.add(chatMessage);
		}

		return chatMessageList;
	}

	@SubscribeMapping("/chatAInit")
	public Collection<ChatUser> chatAInit() {
		final List<CsmUser> csmUserList = dbService.findAll(env.getPair(CsmUser.class.getSimpleName()), customerSpec,
				customerSort);

		final List<ChatUser> chatCustomerList = Lists.newArrayListWithCapacity(csmUserList.size());
		for (CsmUser csmUser : csmUserList) {
			final ChatUser chatUser = ChatUser.valueOf(csmUser);
			final String userId = chatUser.getUserId();
			if (onlineCustomerStore.containsUser(userId)) {
				chatUser.setOnline(true);
			}
			if (unHandledCustomerMap.containsKey(userId)) {
				chatUser.setUnhandled(true);
			}
			chatCustomerList.add(chatUser);
		}
		return chatCustomerList;
	}

	@MessageMapping("/chatASendMessage")
	public void chatASendMessage(ChatMessage message, Principal principal) {
		final String userId = principal.getName();
		message.setSenderName("Customer Service - " + userId);
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		template.convertAndSend("/topic/chat/message/" + message.getRoomId(), message);

		final CsmChatMessage csmChatMessage = message.toCsmChatMessage();
		csmChatMessage.setSenderId(UUID.fromString(userId));
		dbService.save(env.getPair(CsmChatMessage.class.getSimpleName()), csmChatMessage);

		final ChatEvent messageEvent = new ChatEvent(message.getRoomId());
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
		unHandledCustomerMap.remove(message.getRoomId());
	}

	@SubscribeMapping("/chatAInitMessage/{roomId}")
	public List<ChatMessage> chatAInitMessage(Principal principal, @DestinationVariable String roomId) {
		final List<ChatMessage> chatMessageList = getChatMessageList(roomId);
		return chatMessageList;
	}

	private List<ChatMessage> getChatMessageList(String roomId) {
		final List<CsmChatMessage> csmChatMessageList = getCsmChatMessageList(roomId);
		if (Collections3.isEmpty(csmChatMessageList)) {
			return Collections.emptyList();
		}

		final List<ChatMessage> chatMessageList = Lists.newArrayListWithCapacity(csmChatMessageList.size());
		for (int i = csmChatMessageList.size() - 1; i >= 0; i--) {
			final CsmChatMessage csmChatMessage = csmChatMessageList.get(i);
			final ChatMessage chatMessage = ChatMessage.valueOf(csmChatMessage);
			// TODO
			chatMessage.setSenderName(csmChatMessage.getSenderId().toString());
			chatMessageList.add(chatMessage);
		}
		return chatMessageList;
	}

	private List<CsmChatMessage> getCsmChatMessageList(String roomId) {
		final List<CsmChatMessage> csmChatMessageList = dbService
				.find(env.getPair(CsmChatMessage.class.getSimpleName()), buildMessageSpecification(roomId),
						messagePageRequest)
				.getContent();
		return csmChatMessageList;
	}

	private Specification<CsmChatMessage> buildMessageSpecification(String roomId) {
		return Specifications.build(CsmChatMessage.class,
				Lists.newArrayList(new FindFilter("roomId", Operator.EQ, roomId)));
	}
}
