package com.itdoes.csm.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.spring.SpringDatas;
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

	private static final int MESSAGE_PAGE_SIZE = 10;
	private static final PageRequest MESSAGE_PAGE_REQUEST = SpringDatas.newPageRequest(1, MESSAGE_PAGE_SIZE,
			SpringDatas.newSort("createDateTime", false));

	private static class ChatUserComparator implements Comparator<ChatUser> {
		private static final ChatUserComparator INSTANCE = new ChatUserComparator();

		@Override
		public int compare(ChatUser o1, ChatUser o2) {
			return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
		}
	}

	@Autowired
	private EntityEnv entityEnv;

	@Autowired
	private UserCacheService userCacheService;

	@Autowired
	private ChatOnlineService onlineService;

	@Autowired
	private ChatUnhandledCustomerService unhandledCustomerService;

	private EntityPair<CsmChatMessage, UUID> chatMessagePair;
	private EntityPair<CsmChatCustomerUserGroup, UUID> chatCustomerUserGroupPair;

	@PostConstruct
	public void myInit() {
		chatMessagePair = entityEnv.getPair(CsmChatMessage.class.getSimpleName());
		chatCustomerUserGroupPair = entityEnv.getPair(CsmChatCustomerUserGroup.class.getSimpleName());
	}

	public CsmUser getUser(ShiroUser shiroUser) {
		return userCacheService.getUser(shiroUser.getId());
	}

	public CsmUserGroup getUserGroup(ShiroUser shiroUser) {
		return userCacheService.getUserGroupByUser(shiroUser.getId());
	}

	public List<CsmChatMessage> customerInitMessage(ShiroUser shiroUser) {
		List<CsmChatMessage> messageList = initMessage(shiroUser.getId(), shiroUser, false);
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
		final String curUserIdString = shiroUser.getId();
		final UUID curUserId = UUID.fromString(curUserIdString);
		message.setRoomId(curUserId);
		message.setSenderId(curUserId);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(false);
		saveChatMessage(message);
		template.convertAndSend("/topic/chat/message/" + curUserIdString, message);

		final ChatEvent messageEvent = new ChatEvent(curUserIdString);
		unhandledCustomerService.addUnhandledCustomer(messageEvent);
		template.convertAndSend("/topic/chat/addUnhandledCustomer", messageEvent);
	}

	public List<ChatUser> adminInit(ShiroUser shiroUser) {
		final CsmUser curAdminUser = userCacheService.getUser(shiroUser.getId());
		final String curAdminUserGroupIdString = curAdminUser.getUserGroupId().toString();
		final CsmUserGroup curAdminUserGroup = userCacheService.getUserGroup(curAdminUserGroupIdString);

		final List<ChatUser> customerList = Lists.newArrayList();
		for (CsmUser user : userCacheService.getUserMap().values()) {
			final CsmUserGroup userGroup = userCacheService.getUserGroup(user.getUserGroupId().toString());
			if (userGroup != null) {
				if (!userGroup.getAdmin()) {
					final ChatUser chatUser = new ChatUser(user.getId().toString(), user.getUsername());
					chatUser.setOnline(onlineService.isOnlineUser(chatUser.getUserId()));
					chatUser.setUnhandled(
							isUnhandledCustomer(chatUser.getUserId(), curAdminUserGroup, curAdminUserGroupIdString));
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
		final UUID roomId = message.getRoomId();
		Validate.notNull(roomId, "RoomId should not be null");
		final String roomIdString = roomId.toString();

		final String curUserIdString = shiroUser.getId();
		final UUID curUserId = UUID.fromString(curUserIdString);
		message.setSenderId(curUserId);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		saveChatMessage(message);
		template.convertAndSend("/topic/chat/message/" + roomIdString, message);

		final ChatEvent messageEvent = new ChatEvent(roomIdString);
		unhandledCustomerService.removeUnhandledCustomer(messageEvent.getUserId());
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
	}

	public boolean hasUnhandledCustomers(ShiroUser shiroUser) {
		if (!unhandledCustomerService.hasUnhandledCustomers()) {
			return false;
		}

		final CsmUser curAdminUser = userCacheService.getUser(shiroUser.getId());
		final String curAdminUserGroupIdString = curAdminUser.getUserGroupId().toString();
		final CsmUserGroup curAdminUserGroup = userCacheService.getUserGroup(curAdminUserGroupIdString);
		if (curAdminUserGroup.getChat()) {
			return true;
		} else {
			final List<CsmChatCustomerUserGroup> chatCustomerUserGroupList = chatCustomerUserGroupPair.external()
					.findAll(chatCustomerUserGroupPair,
							Specifications.build(CsmChatCustomerUserGroup.class,
									Lists.newArrayList(
											new FindFilter("userGroupId", Operator.EQ, curAdminUserGroupIdString))),
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

		if (adminUserGroup.getChat()) {
			return true;
		} else {
			return isCustomerUserGroupExist(customerId, adminUserGroupIdString);
		}
	}

	private boolean isCustomerUserGroupExist(String customerId, String userGroupId) {
		return chatCustomerUserGroupPair.external().count(chatCustomerUserGroupPair,
				Specifications.build(CsmChatCustomerUserGroup.class,
						Lists.newArrayList(new FindFilter("customerUserId", Operator.EQ, customerId),
								new FindFilter("userGroupId", Operator.EQ, userGroupId)))) > 0;
	}

	public Result postCustomerUserGroup(CsmChatCustomerUserGroup chatCustomerUserGroup, ShiroUser shiroUser,
			SimpMessagingTemplate template) {
		final String customerId = chatCustomerUserGroup.getCustomerUserId().toString();
		final String userGroupId = chatCustomerUserGroup.getUserGroupId().toString();
		if (isCustomerUserGroupExist(customerId, userGroupId)) {
			return Result.fail(1, "CustomerUserGroup exists");
		}

		chatCustomerUserGroup.setOperatorUserId(UUID.fromString(shiroUser.getId()));
		final Serializable id = chatCustomerUserGroupPair.external().post(chatCustomerUserGroupPair,
				chatCustomerUserGroup);

		final ChatEvent messageEvent = new ChatEvent(customerId);
		template.convertAndSend("/topic/chat/postCustomerUserGroup/" + userGroupId, messageEvent);

		return Result.success().addData("id", id);
	}

	public Result deleteCustomerUserGroup(String id, SimpMessagingTemplate template) {
		final CsmChatCustomerUserGroup chatCustomerUserGroup = chatCustomerUserGroupPair.external()
				.get(chatCustomerUserGroupPair, UUID.fromString(id));
		chatCustomerUserGroupPair.external().delete(chatCustomerUserGroupPair, UUID.fromString(id));

		final ChatEvent messageEvent = new ChatEvent(chatCustomerUserGroup.getCustomerUserId().toString());
		template.convertAndSend(
				"/topic/chat/deleteCustomerUserGroup/" + chatCustomerUserGroup.getUserGroupId().toString(),
				messageEvent);
		return Result.success();
	}

	private void saveChatMessage(CsmChatMessage message) {
		chatMessagePair.external().post(chatMessagePair, message);
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
		return chatMessagePair.external().find(chatMessagePair,
				Specifications.build(CsmChatMessage.class,
						Lists.newArrayList(new FindFilter("roomId", Operator.EQ, roomId))),
				MESSAGE_PAGE_REQUEST).getContent();
	}
}
