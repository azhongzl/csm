package com.itdoes.csm.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
import com.google.common.collect.Sets;
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
		template.convertAndSend("/topic/chat/message/" + userIdString, message);

		final ChatEvent messageEvent = new ChatEvent(userIdString);
		template.convertAndSend("/topic/chat/addUnhandledCustomer", messageEvent);
		unhandledCustomerService.addUnhandledCustomer(messageEvent);

		saveChatMessage(message);
	}

	public List<ChatUser> adminInit(ShiroUser shiroUser) {
		final Set<String> customerIdSet;
		if (isChatUserGroup(shiroUser)) {
			customerIdSet = userCacheService.getCustomerIdSet();
		} else {
			final CsmUser user = userCacheService.getUser(shiroUser.getId());
			final List<CsmChatCustomerUserGroup> chatCustomerUserGroupList = entityDbService
					.findAll(env.getPair(CsmChatCustomerUserGroup.class.getSimpleName()),
							Specifications.build(CsmChatCustomerUserGroup.class, Lists.newArrayList(
									new FindFilter("user_group_id", Operator.EQ, user.getUserGroupId().toString()))),
							null);
			if (Collections3.isEmpty(chatCustomerUserGroupList)) {
				return Collections.emptyList();
			}

			customerIdSet = Sets.newHashSetWithExpectedSize(chatCustomerUserGroupList.size());
			for (CsmChatCustomerUserGroup chatCustomerUserGroup : chatCustomerUserGroupList) {
				customerIdSet.add(chatCustomerUserGroup.getCustomerUserId().toString());
			}
		}

		final List<ChatUser> customerList = Lists.newArrayListWithCapacity(customerIdSet.size());
		for (String customerId : customerIdSet) {
			final ChatUser chatUser = ChatUser.valueOf(userCacheService.getUser(customerId));
			chatUser.setOnline(onlineService.isOnlineUser(customerId));
			chatUser.setUnhandled(unhandledCustomerService.hasUnhandledCustomer(customerId));
			customerList.add(chatUser);
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
		template.convertAndSend("/topic/chat/message/" + roomIdUuid, message);

		final ChatEvent messageEvent = new ChatEvent(roomIdUuid.toString());
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
		unhandledCustomerService.removeUnhandledCustomer(messageEvent.getUserId());

		saveChatMessage(message);
	}

	public boolean isChatUserGroup(ShiroUser shiroUser) {
		final CsmUser user = userCacheService.getUser(shiroUser.getId());
		final String userGroupIdString = user.getUserGroupId().toString();
		final CsmUserGroup userGroup = userCacheService.getUserGroup(userGroupIdString);
		return userGroup.isChat();
	}

	public boolean hasUnhandledCustomers(ShiroUser shiroUser) {
		if (!unhandledCustomerService.hasUnhandledCustomers()) {
			return false;
		}

		if (isChatUserGroup(shiroUser)) {
			return true;
		} else {
			final CsmUser user = userCacheService.getUser(shiroUser.getId());
			final List<CsmChatCustomerUserGroup> chatCustomerUserGroupList = entityDbService
					.findAll(env.getPair(CsmChatCustomerUserGroup.class.getSimpleName()),
							Specifications.build(CsmChatCustomerUserGroup.class, Lists.newArrayList(
									new FindFilter("user_group_id", Operator.EQ, user.getUserGroupId().toString()))),
							null);
			if (Collections3.isEmpty(chatCustomerUserGroupList)) {
				return false;
			}

			for (CsmChatCustomerUserGroup chatCustomerUserGroup : chatCustomerUserGroupList) {
				if (unhandledCustomerService
						.hasUnhandledCustomer(chatCustomerUserGroup.getCustomerUserId().toString())) {
					return true;
				}
			}

			return false;
		}
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
