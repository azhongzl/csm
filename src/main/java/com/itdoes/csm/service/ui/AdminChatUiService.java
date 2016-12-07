package com.itdoes.csm.service.ui;

import java.io.Serializable;
import java.security.Principal;
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
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.common.core.spring.SpringDatas;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.dto.ChatUser;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmChatCustomerUserGroup;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.entity.CsmUserGroup;
import com.itdoes.csm.service.ChatOnlineService;
import com.itdoes.csm.service.ChatUnhandledCustomerService;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminChatUiService extends BaseService {
	private static final Root ROOT = Root.getInstance();

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
	private EntityEnv env;

	@Autowired
	private UserCacheService userCacheService;

	@Autowired
	private ChatOnlineService onlineService;

	@Autowired
	private ChatUnhandledCustomerService unhandledCustomerService;

	private EntityPair<CsmChatMessage, UUID> messagePair;
	private EntityPair<CsmChatCustomerUserGroup, UUID> customerUserGroupPair;

	@PostConstruct
	public void myInit() {
		messagePair = env.getPair(CsmChatMessage.class.getSimpleName());
		customerUserGroupPair = env.getPair(CsmChatCustomerUserGroup.class.getSimpleName());
	}

	public Result hasUnhandledCustomers(Principal principal) {
		if (!unhandledCustomerService.hasUnhandledCustomers()) {
			return Result.success().addData("hasUnhandledCustomers", false);
		}

		final ShiroUser shiroUser = getShiroUser(principal);
		final CsmUser curAdminUser = userCacheService.getUser(shiroUser.getId());
		final String curAdminUserGroupIdString = curAdminUser.getUserGroupId().toString();
		final CsmUserGroup curAdminUserGroup = userCacheService.getUserGroup(curAdminUserGroupIdString);
		if (curAdminUserGroup.getChat()) {
			return Result.success().addData("hasUnhandledCustomers", true);
		} else {
			final List<CsmChatCustomerUserGroup> customerUserGroupList = customerUserGroupPair.external().findAll(
					customerUserGroupPair,
					Specifications.build(CsmChatCustomerUserGroup.class,
							Lists.newArrayList(new FindFilter("userGroupId", Operator.EQ, curAdminUserGroupIdString))),
					null);
			if (Collections3.isEmpty(customerUserGroupList)) {
				return Result.success().addData("hasUnhandledCustomers", false);
			}

			for (CsmChatCustomerUserGroup customerUserGroup : customerUserGroupList) {
				if (unhandledCustomerService.isUnhandledCustomer(customerUserGroup.getCustomerUserId().toString())) {
					return Result.success().addData("hasUnhandledCustomers", true);
				}
			}

			return Result.success().addData("hasUnhandledCustomers", false);
		}
	}

	public Result postCustomerUserGroup(CsmChatCustomerUserGroup customerUserGroup, Principal principal,
			SimpMessagingTemplate template) {
		final String customerId = customerUserGroup.getCustomerUserId().toString();
		final String userGroupId = customerUserGroup.getUserGroupId().toString();
		if (isCustomerUserGroupExist(customerId, userGroupId)) {
			return Result.fail(1, "CustomerUserGroup exists");
		}

		final ShiroUser shiroUser = getShiroUser(principal);
		customerUserGroup.setOperatorUserId(UUID.fromString(shiroUser.getId()));
		final Serializable id = customerUserGroupPair.external().post(customerUserGroupPair, customerUserGroup);

		final ChatEvent messageEvent = new ChatEvent(customerId);
		template.convertAndSend("/topic/chat/postCustomerUserGroup/" + userGroupId, messageEvent);

		return Result.success().addData("id", id);
	}

	public Result deleteCustomerUserGroup(String id, SimpMessagingTemplate template) {
		final CsmChatCustomerUserGroup customerUserGroup = customerUserGroupPair.external().get(customerUserGroupPair,
				UUID.fromString(id));
		customerUserGroupPair.external().delete(customerUserGroupPair, UUID.fromString(id));

		final ChatEvent messageEvent = new ChatEvent(customerUserGroup.getCustomerUserId().toString());
		template.convertAndSend("/topic/chat/deleteCustomerUserGroup/" + customerUserGroup.getUserGroupId().toString(),
				messageEvent);
		return Result.success();
	}

	public Result init(Principal principal) {
		final ShiroUser shiroUser = getShiroUser(principal);
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
		return Result.success().addData("customerList", customerList);
	}

	public Result initMessage(String roomId, Principal principal) {
		final List<CsmChatMessage> messageList = getLatestMessageList(roomId, principal);

		final List<CsmUserGroup> userGroupList = Lists.newArrayList();
		for (CsmUserGroup userGroup : userCacheService.getUserGroupMap().values()) {
			if (!ROOT.isRootById(userGroup.getId()) && !userGroup.getChat()) {
				userGroupList.add(userGroup);
			}
		}

		final List<CsmChatCustomerUserGroup> chatCustomerUserGroupList = customerUserGroupPair.external()
				.findAll(customerUserGroupPair, Specifications.build(CsmChatCustomerUserGroup.class,
						Lists.newArrayList(new FindFilter("customerUserId", Operator.EQ, roomId))), null);

		return Result.success().addData("messageList", messageList).addData("userGroupList", userGroupList)
				.addData("chatCustomerUserGroupList", chatCustomerUserGroupList);
	}

	public void sendMessage(CsmChatMessage message, Principal principal, SimpMessagingTemplate template) {
		final UUID roomId = message.getRoomId();
		Validate.notNull(roomId, "RoomId should not be null");
		final String roomIdString = roomId.toString();

		final ShiroUser shiroUser = getShiroUser(principal);
		final String curUserIdString = shiroUser.getId();
		final UUID curUserId = UUID.fromString(curUserIdString);
		message.setSenderId(curUserId);
		message.setSenderName(shiroUser.getUsername());
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		messagePair.external().post(messagePair, message);
		template.convertAndSend("/topic/chat/message/" + roomIdString, message);

		final ChatEvent messageEvent = new ChatEvent(roomIdString);
		unhandledCustomerService.removeUnhandledCustomer(messageEvent.getUserId());
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
	}

	private List<CsmChatMessage> getLatestMessageList(String roomId, Principal principal) {
		final List<CsmChatMessage> dbMessageList = messagePair.external().find(messagePair,
				Specifications.build(CsmChatMessage.class,
						Lists.newArrayList(new FindFilter("roomId", Operator.EQ, roomId))),
				MESSAGE_PAGE_REQUEST).getContent();
		if (Collections3.isEmpty(dbMessageList)) {
			return Collections.emptyList();
		}

		final List<CsmChatMessage> messageList = Lists.newArrayListWithCapacity(dbMessageList.size());
		for (int i = dbMessageList.size() - 1; i >= 0; i--) {
			final CsmChatMessage message = dbMessageList.get(i);

			final String senderId = message.getSenderId().toString();
			final String senderName;
			final CsmUser user = userCacheService.getUser(senderId);
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

	private boolean isCustomerUserGroupExist(String customerId, String userGroupId) {
		return customerUserGroupPair.external().count(customerUserGroupPair,
				Specifications.build(CsmChatCustomerUserGroup.class,
						Lists.newArrayList(new FindFilter("customerUserId", Operator.EQ, customerId),
								new FindFilter("userGroupId", Operator.EQ, userGroupId)))) > 0;
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

	private ShiroUser getShiroUser(Principal principal) {
		return Shiros.getShiroUser(principal);
	}
}
