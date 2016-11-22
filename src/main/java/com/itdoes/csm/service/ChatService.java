package com.itdoes.csm.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
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

/**
 * @author Jalen Zhong
 */
@Service
public class ChatService extends BaseService {
	public static final int MESSAGE_PAGE_SIZE = 10;

	private static final PageRequest MESSAGE_PAGE_REQUEST = new PageRequest(0, MESSAGE_PAGE_SIZE,
			new Sort(Direction.DESC, "createDateTime"));

	private static class ChatUserComparator implements Comparator<ChatUser> {
		private static final ChatUserComparator INSTANCE = new ChatUserComparator();

		@Override
		public int compare(ChatUser o1, ChatUser o2) {
			return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
		}
	}

	@Autowired
	private EntityEnv env;

	@Autowired
	private EntityDbService dbService;

	@Autowired
	private UserCacheService userCacheService;

	@Autowired
	private ChatOnlineService onlineService;

	private final Map<String, ChatEvent> unhandledCustomerMap = Maps.newConcurrentMap();

	private EntityPair<CsmChatMessage, UUID> messagePair;

	@PostConstruct
	public void init() {
		messagePair = env.getPair(CsmChatMessage.class.getSimpleName());
	}

	public List<CsmChatMessage> customerInitMessage(Principal principal) {
		final ShiroUser shiroUser = Shiros.getShiroUser(principal);
		final String userId = shiroUser.getId();
		List<CsmChatMessage> messageList = adminInitMessage(userId);
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

	public void customerSendMessage(CsmChatMessage message, Principal principal, SimpMessagingTemplate template) {
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
		addUnhandledCustomer(messageEvent);

		saveChatMessage(message);
	}

	public List<ChatUser> adminInit() {
		final Set<String> customerIdSet = userCacheService.getCustomerIdSet();
		final List<ChatUser> customerList = Lists.newArrayListWithCapacity(customerIdSet.size());
		for (String customerId : customerIdSet) {
			final ChatUser chatUser = ChatUser.valueOf(userCacheService.getUser(customerId));
			chatUser.setOnline(onlineService.isOnlineUser(customerId));
			chatUser.setUnhandled(unhandledCustomerMap.containsKey(customerId));
			customerList.add(chatUser);
		}
		Collections.sort(customerList, ChatUserComparator.INSTANCE);
		return customerList;
	}

	public List<CsmChatMessage> adminInitMessage(String roomId) {
		final List<CsmChatMessage> dbMessageList = getChatMessageListFromDb(roomId);
		if (Collections3.isEmpty(dbMessageList)) {
			return Collections.emptyList();
		}

		final List<CsmChatMessage> messageList = Lists.newArrayListWithCapacity(dbMessageList.size());
		for (int i = dbMessageList.size() - 1; i >= 0; i--) {
			final CsmChatMessage message = dbMessageList.get(i);

			final String senderName;
			final CsmUser user = userCacheService.getUser(message.getSenderId().toString());
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

	public void adminSendMessage(CsmChatMessage message, Principal principal, SimpMessagingTemplate template) {
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
		removeUnhandledCustomer(messageEvent.getUserId());

		saveChatMessage(message);
	}

	public boolean hasUnhandledCustomer() {
		return !Collections3.isEmpty(unhandledCustomerMap);
	}

	private void addUnhandledCustomer(ChatEvent event) {
		unhandledCustomerMap.put(event.getUserId(), event);
	}

	private void removeUnhandledCustomer(String userId) {
		unhandledCustomerMap.remove(userId);
	}

	private void saveChatMessage(CsmChatMessage message) {
		dbService.save(messagePair, message);
	}

	private List<CsmChatMessage> getChatMessageListFromDb(String roomId) {
		final List<CsmChatMessage> messageList = dbService
				.find(messagePair, buildMessageSpecification(roomId), MESSAGE_PAGE_REQUEST).getContent();
		return messageList;
	}

	private Specification<CsmChatMessage> buildMessageSpecification(String roomId) {
		return Specifications.build(CsmChatMessage.class,
				Lists.newArrayList(new FindFilter("roomId", Operator.EQ, roomId)));
	}
}
