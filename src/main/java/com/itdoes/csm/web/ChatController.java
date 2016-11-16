package com.itdoes.csm.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.itdoes.csm.dto.ChatUser;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.service.ChatOnlineUserStore;
import com.itdoes.csm.service.UserStore;

/**
 * @author Jalen Zhong
 */
@Controller
public class ChatController {
	public static final int MESSAGE_PAGE_SIZE = 10;

	private static final PageRequest MESSAGE_PAGE_REQUEST = new PageRequest(0, MESSAGE_PAGE_SIZE,
			new Sort(Direction.DESC, "createDateTime"));

	@Autowired
	private EntityEnv env;

	@Autowired
	private EntityDbService dbService;

	private final SimpMessagingTemplate template;

	@Autowired
	private UserStore userStore;

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

		dbService.save(env.getPair(CsmChatMessage.class.getSimpleName()), message);

		final ChatEvent messageEvent = new ChatEvent(userId);
		template.convertAndSend("/topic/chat/addUnhandledCustomer", messageEvent);
		unHandledCustomerMap.put(userId, messageEvent);
	}

	@SubscribeMapping("/chatCInitMessage")
	public List<CsmChatMessage> chatCInitMessage(Principal principal) {
		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		List<CsmChatMessage> messageList = getChatMessageList(userId);

		if (Collections3.isEmpty(messageList)) {
			messageList = Lists.newArrayListWithCapacity(1);

			final CsmChatMessage message = new CsmChatMessage();
			message.setSenderName("Customer Service");
			message.setCreateDateTime(LocalDateTime.now());
			message.setMessage(
					"Welcome, " + shiroUser.getUsername() + "! Our agent will contact you soon. Please wait...");
			message.setFromAdmin(true);
			messageList.add(message);
		}

		return messageList;
	}

	@SubscribeMapping("/chatAInit")
	public Collection<ChatUser> chatAInit() {
		final Set<CsmUser> csmUserList = userStore.getCustomerSet();

		final List<ChatUser> chatCustomerList = Lists.newArrayListWithCapacity(csmUserList.size());
		for (CsmUser csmUser : csmUserList) {
			final ChatUser chatUser = ChatUser.valueOf(csmUser);
			final String userId = chatUser.getUserId();
			if (onlineCustomerStore.isOnlineUser(userId)) {
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
	public void chatASendMessage(CsmChatMessage message, Principal principal) {
		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		final UUID userIdUuid = UUID.fromString(userId);
		message.setSenderId(userIdUuid);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		template.convertAndSend("/topic/chat/message/" + message.getRoomId(), message);

		dbService.save(env.getPair(CsmChatMessage.class.getSimpleName()), message);

		final ChatEvent messageEvent = new ChatEvent(message.getRoomId().toString());
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
		unHandledCustomerMap.remove(message.getRoomId());
	}

	@SubscribeMapping("/chatAInitMessage/{roomId}")
	public List<CsmChatMessage> chatAInitMessage(Principal principal, @DestinationVariable String roomId) {
		return getChatMessageList(roomId);
	}

	private List<CsmChatMessage> getChatMessageList(String roomId) {
		final List<CsmChatMessage> dbMessageList = getChatMessageListFromDb(roomId);
		if (Collections3.isEmpty(dbMessageList)) {
			return Collections.emptyList();
		}

		final List<CsmChatMessage> messageList = Lists.newArrayListWithCapacity(dbMessageList.size());
		for (int i = dbMessageList.size() - 1; i >= 0; i--) {
			final CsmChatMessage message = dbMessageList.get(i);

			final String senderName;
			final CsmUser user = userStore.getUser(message.getSenderId().toString());
			if (user == null) {
				senderName = "Unknown";
			} else {
				senderName = user.getUsername();
			}
			message.setSenderName(senderName);
			messageList.add(message);
		}
		return messageList;
	}

	private List<CsmChatMessage> getChatMessageListFromDb(String roomId) {
		final List<CsmChatMessage> messageList = dbService.find(env.getPair(CsmChatMessage.class.getSimpleName()),
				buildMessageSpecification(roomId), MESSAGE_PAGE_REQUEST).getContent();
		return messageList;
	}

	private Specification<CsmChatMessage> buildMessageSpecification(String roomId) {
		return Specifications.build(CsmChatMessage.class,
				Lists.newArrayList(new FindFilter("roomId", Operator.EQ, roomId)));
	}
}
