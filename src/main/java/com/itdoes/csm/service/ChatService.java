package com.itdoes.csm.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.dto.ChatUser;
import com.itdoes.csm.entity.CsmChatCustomerUserGroup;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.entity.CsmUserGroup;

/**
 * @author Jalen Zhong
 */
@Service
public class ChatService extends BaseService {
	public static final String CUSTOMER_SERVICE_NAME = "Customer Service";

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
	private UserCacheService userCacheService;

	@Autowired
	private ChatOnlineService onlineService;

	@Autowired
	private ChatUnhandledCustomerService unhandledCustomerService;

	@Autowired
	private EntityDbService entityDbService;

	public CsmUser getUser(ShiroUser shiroUser) {
		return userCacheService.getUser(shiroUser.getId());
	}

	public CsmUserGroup getUserGroup(ShiroUser shiroUser) {
		return userCacheService.getUserGroup(getUser(shiroUser).getUserGroupId().toString());
	}

	public List<CsmChatMessage> customerInitMessage(ShiroUser shiroUser) {
		final String userIdString = shiroUser.getId();
		List<CsmChatMessage> messageList = initMessage(userIdString, shiroUser, false);
		if (Collections3.isEmpty(messageList)) {
			messageList = Lists.newArrayListWithCapacity(1);
		}

		final CsmChatMessage message = new CsmChatMessage();
		message.setSenderName(CUSTOMER_SERVICE_NAME);
		message.setCreateDateTime(LocalDateTime.now());
		message.setMessage("Welcome, " + shiroUser.getUsername() + "! Our agent will contact you soon. Please wait...");
		message.setFromAdmin(true);
		messageList.add(message);

		return messageList;
	}

	public void customerSendMessage(CsmChatMessage message, ShiroUser shiroUser, SimpMessagingTemplate template) {
		final String userIdString = shiroUser.getId();
		final UUID userId = UUID.fromString(userIdString);
		message.setRoomId(userId);
		message.setSenderId(userId);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(false);
		saveChatMessage(message);
		template.convertAndSend("/topic/chat/message/" + userIdString, message);

		final ChatEvent messageEvent = new ChatEvent(userIdString);
		unhandledCustomerService.addUnhandledCustomer(messageEvent);
		template.convertAndSend("/topic/chat/addUnhandledCustomer", messageEvent);
	}

	public List<ChatUser> adminInit(ShiroUser shiroUser) {
		final CsmUser adminUser = userCacheService.getUser(shiroUser.getId());
		final String adminUserGroupIdString = adminUser.getUserGroupId().toString();
		final CsmUserGroup adminUserGroup = userCacheService.getUserGroup(adminUserGroupIdString);

		final List<ChatUser> customerList = Lists.newArrayList();
		for (CsmUser user : userCacheService.getUserMap().values()) {
			final CsmUserGroup userGroup = userCacheService.getUserGroup(user.getUserGroupId().toString());
			if (userGroup != null) {
				if (!userGroup.isAdmin()) {
					final ChatUser chatUser = new ChatUser(user.getId().toString(), user.getUsername());
					chatUser.setOnline(onlineService.isOnlineUser(chatUser.getUserId()));
					chatUser.setUnhandled(
							isUnhandledCustomer(chatUser.getUserId(), adminUserGroup, adminUserGroupIdString));
					customerList.add(chatUser);
				}
			}
		}
		Collections.sort(customerList, ChatUserComparator.INSTANCE);
		return customerList;
	}

	public List<CsmChatMessage> adminInitMessage(String roomId, ShiroUser shiroUser) {
		return initMessage(roomId, shiroUser, true);
	}

	public void adminSendMessage(CsmChatMessage message, ShiroUser shiroUser, SimpMessagingTemplate template) {
		final UUID roomIdUuid = message.getRoomId();
		Validate.notNull(roomIdUuid, "RoomId should not be null");

		final String userIdString = shiroUser.getId();
		final UUID userId = UUID.fromString(userIdString);
		message.setSenderId(userId);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		saveChatMessage(message);
		template.convertAndSend("/topic/chat/message/" + roomIdUuid, message);

		final ChatEvent messageEvent = new ChatEvent(roomIdUuid.toString());
		unhandledCustomerService.removeUnhandledCustomer(messageEvent.getUserId());
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
	}

	public boolean hasUnhandledCustomers(ShiroUser shiroUser) {
		if (!unhandledCustomerService.hasUnhandledCustomers()) {
			return false;
		}

		final CsmUser adminUser = userCacheService.getUser(shiroUser.getId());
		final String adminUserGroupIdString = adminUser.getUserGroupId().toString();
		final CsmUserGroup adminUserGroup = userCacheService.getUserGroup(adminUserGroupIdString);
		if (adminUserGroup.isChat()) {
			return true;
		} else {
			final List<CsmChatCustomerUserGroup> chatCustomerUserGroupList = entityDbService.findAll(
					env.getPair(CsmChatCustomerUserGroup.class.getSimpleName()),
					Specifications.build(CsmChatCustomerUserGroup.class,
							Lists.newArrayList(new FindFilter("userGroupId", Operator.EQ, adminUserGroupIdString))),
					null);
			if (Collections3.isEmpty(chatCustomerUserGroupList)) {
				return false;
			}

			for (CsmChatCustomerUserGroup chatCustomerUserGroup : chatCustomerUserGroupList) {
				if (unhandledCustomerService
						.isUnhandledCustomer(chatCustomerUserGroup.getCustomerUserId().toString())) {
					return true;
				}
			}

			return false;
		}
	}

	private boolean isUnhandledCustomer(String customerId, CsmUserGroup adminUserGroup, String adminUserGroupIdString) {
		if (!unhandledCustomerService.isUnhandledCustomer(customerId)) {
			return false;
		}

		if (adminUserGroup.isChat()) {
			return true;
		} else {
			return isCustomerUserGroupExist(customerId, adminUserGroupIdString);
		}
	}

	private boolean isCustomerUserGroupExist(String customerId, String userGroupId) {
		return entityDbService.count(env.getPair(CsmChatCustomerUserGroup.class.getSimpleName()),
				Specifications.build(CsmChatCustomerUserGroup.class,
						Lists.newArrayList(new FindFilter("customerUserId", Operator.EQ, customerId),
								new FindFilter("userGroupId", Operator.EQ, userGroupId)))) > 0;
	}

	public Serializable addCustomerUserGroup(String customerId, String userGroupId, ShiroUser shiroUser,
			SimpMessagingTemplate template) {
		if (isCustomerUserGroupExist(customerId, userGroupId)) {
			return false;
		}

		final CsmChatCustomerUserGroup chatCustomerUserGroup = new CsmChatCustomerUserGroup();
		chatCustomerUserGroup.setCustomerUserId(UUID.fromString(customerId));
		chatCustomerUserGroup.setUserGroupId(UUID.fromString(userGroupId));
		chatCustomerUserGroup.setOperatorUserId(UUID.fromString(shiroUser.getId()));
		final Serializable id = entityDbService.post(env.getPair(CsmChatCustomerUserGroup.class.getSimpleName()),
				chatCustomerUserGroup);

		final ChatEvent messageEvent = new ChatEvent(customerId);
		template.convertAndSend("/topic/chat/addCustomerUserGroup/" + userGroupId, messageEvent);

		return id;
	}

	public void removeCustomerUserGroup(String id, String customerId, String userGroupId,
			SimpMessagingTemplate template) {
		entityDbService.delete(env.getPair(CsmChatCustomerUserGroup.class.getSimpleName()), UUID.fromString(id));

		final ChatEvent messageEvent = new ChatEvent(customerId);
		template.convertAndSend("/topic/chat/removeCustomerUserGroup/" + userGroupId, messageEvent);
	}

	private void saveChatMessage(CsmChatMessage message) {
		final EntityPair<CsmChatMessage, UUID> messagePair = getMessagePair();
		messagePair.getService().post(messagePair, message);
	}

	private List<CsmChatMessage> initMessage(String roomId, ShiroUser shiroUser, boolean forAdmin) {
		final List<CsmChatMessage> dbMessageList = getChatMessageListFromDb(roomId);
		if (Collections3.isEmpty(dbMessageList)) {
			return Collections.emptyList();
		}

		final List<CsmChatMessage> messageList = Lists.newArrayListWithCapacity(dbMessageList.size());
		for (int i = dbMessageList.size() - 1; i >= 0; i--) {
			final CsmChatMessage message = dbMessageList.get(i);

			final String senderId = message.getSenderId().toString();
			final String senderName;
			if (!forAdmin && !shiroUser.getId().equals(senderId)) {
				senderName = CUSTOMER_SERVICE_NAME;
			} else {
				final CsmUser user = userCacheService.getUser(senderId);
				if (user == null) {
					senderName = "Unknown";
				} else {
					senderName = user.getUsername();
				}
			}
			message.setSenderName(senderName);
			messageList.add(message);
		}
		return messageList;
	}

	private List<CsmChatMessage> getChatMessageListFromDb(String roomId) {
		final EntityPair<CsmChatMessage, UUID> messagePair = getMessagePair();
		final List<CsmChatMessage> messageList = messagePair.getService()
				.find(messagePair, buildMessageSpecification(roomId), MESSAGE_PAGE_REQUEST).getContent();
		return messageList;
	}

	private Specification<CsmChatMessage> buildMessageSpecification(String roomId) {
		return Specifications.build(CsmChatMessage.class,
				Lists.newArrayList(new FindFilter("roomId", Operator.EQ, roomId)));
	}

	private EntityPair<CsmChatMessage, UUID> getMessagePair() {
		return env.getPair(CsmChatMessage.class.getSimpleName());
	}
}
