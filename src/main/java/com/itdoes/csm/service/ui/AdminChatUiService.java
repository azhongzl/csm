package com.itdoes.csm.service.ui;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.apache.shiro.authz.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.shiro.Shiros;
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
	private static enum CustomerUserGroupDtoComparator implements Comparator<CustomerUserGroupDto> {
		INSTANCE;

		@Override
		public int compare(CustomerUserGroupDto o1, CustomerUserGroupDto o2) {
			return o1.getUserGroup().getName().toLowerCase().compareTo(o2.getUserGroup().getName().toLowerCase());
		}
	}

	private static enum UserGroupComparator implements Comparator<CsmUserGroup> {
		INSTANCE;

		@Override
		public int compare(CsmUserGroup o1, CsmUserGroup o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	}

	private static enum ChatUserComparator implements Comparator<ChatUser> {
		INSTANCE;

		@Override
		public int compare(ChatUser o1, ChatUser o2) {
			return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
		}
	}

	private static class CustomerUserGroupDto {
		private final CsmChatCustomerUserGroup customerUserGroup;
		private final CsmUserGroup userGroup;

		private CustomerUserGroupDto(CsmChatCustomerUserGroup customerUserGroup, CsmUserGroup userGroup) {
			this.customerUserGroup = customerUserGroup;
			this.userGroup = userGroup;
		}

		@SuppressWarnings("unused")
		public CsmChatCustomerUserGroup getCustomerUserGroup() {
			return customerUserGroup;
		}

		public CsmUserGroup getUserGroup() {
			return userGroup;
		}
	}

	private static class UserGroupDto {
		private final CsmUserGroup userGroup;
		private final boolean chatOrSuper;

		public UserGroupDto(CsmUserGroup userGroup, boolean chatOrSuper) {
			this.userGroup = userGroup;
			this.chatOrSuper = chatOrSuper;
		}

		@SuppressWarnings("unused")
		public CsmUserGroup getUserGroup() {
			return userGroup;
		}

		@SuppressWarnings("unused")
		public boolean isChatOrSuper() {
			return chatOrSuper;
		}
	}

	private static final int MESSAGE_PAGE_SIZE = 10;

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
		messagePair = env.getPair(CsmChatMessage.class);
		customerUserGroupPair = env.getPair(CsmChatCustomerUserGroup.class);
	}

	public Result getCurrentUserGroupPair(Principal principal) {
		final ShiroUser shiroUser = getShiroUser(principal);
		final CsmUserGroup curAdminUserGroup = userCacheService.getUserGroupByUser(shiroUser.getId());
		final UserGroupDto curAdminUserGroupDto = new UserGroupDto(curAdminUserGroup, isChatOrSuper(curAdminUserGroup));
		List<CsmChatCustomerUserGroup> customerUserGroupList = Collections.emptyList();
		if (!curAdminUserGroup.getChat()) {
			customerUserGroupList = getCustomerUserGroupListByUserGroup(curAdminUserGroup.getId().toString());
		}
		return Result.success().addData("currentUserGroup", curAdminUserGroupDto).addData("customerUserGroupList",
				customerUserGroupList);
	}

	public Result listHistory(String customerId, Principal principal) {
		final ShiroUser shiroUser = getShiroUser(principal);
		if (!canSendMessage(shiroUser.getId(), customerId)) {
			return Result.fail(1, "You are not allowed to view this customer's chat history");
		}

		final List<CsmChatMessage> messageList = messagePair.db().filterEqual("roomId", customerId)
				.sortAsc("createDateTime").exeFindAll();
		for (CsmChatMessage message : messageList) {
			populateSenderName(message);
		}

		return Result.success().addData("historyList", messageList);
	}

	public Result hasUnhandledCustomers(Principal principal) {
		final String key = "hasUnhandledCustomers";
		if (!unhandledCustomerService.hasUnhandledCustomers()) {
			return Result.success().addData(key, false);
		}

		final ShiroUser shiroUser = getShiroUser(principal);
		final CsmUserGroup userGroup = userCacheService.getUserGroupByUser(shiroUser.getId());
		if (userGroup.getChat()) {
			return Result.success().addData(key, true);
		} else {
			final List<CsmChatCustomerUserGroup> customerUserGroupList = getCustomerUserGroupListByUserGroup(
					userGroup.getId().toString());
			if (Collections3.isEmpty(customerUserGroupList)) {
				return Result.success().addData(key, false);
			}

			for (CsmChatCustomerUserGroup customerUserGroup : customerUserGroupList) {
				if (unhandledCustomerService.isUnhandledCustomer(customerUserGroup.getCustomerUserId().toString())) {
					return Result.success().addData(key, true);
				}
			}

			return Result.success().addData(key, false);
		}
	}

	public Result listCustomerUserGroups(String customerId, Principal principal) {
		final List<CsmChatCustomerUserGroup> customerUserGroupList = getCustomerUserGroupListByCustomer(customerId);
		final List<CustomerUserGroupDto> customerUserGroupDtoList = Lists
				.newArrayListWithCapacity(customerUserGroupList.size());
		final Set<UUID> customerUserGroupIdSet = Sets.newHashSet();
		for (CsmChatCustomerUserGroup customerUserGroup : customerUserGroupList) {
			customerUserGroupDtoList.add(new CustomerUserGroupDto(customerUserGroup,
					userCacheService.getUserGroup(customerUserGroup.getUserGroupId().toString())));
			customerUserGroupIdSet.add(customerUserGroup.getUserGroupId());
		}
		Collections.sort(customerUserGroupDtoList, CustomerUserGroupDtoComparator.INSTANCE);

		final List<CsmUserGroup> userGroupList = Lists.newArrayList();
		for (CsmUserGroup userGroup : userCacheService.getUserGroupMap().values()) {
			if (!Root.isRootById(userGroup.getId()) && !userGroup.getChat() && userGroup.getAdmin()
					&& !customerUserGroupIdSet.contains(userGroup.getId())
					&& hasChatPermission(userGroup.getId().toString())) {
				userGroupList.add(userGroup);
			}
		}
		Collections.sort(userGroupList, UserGroupComparator.INSTANCE);

		return Result.success().addData("customerUserGroupList", customerUserGroupDtoList).addData("userGroupList",
				userGroupList);
	}

	public Result postCustomerUserGroup(CsmChatCustomerUserGroup customerUserGroup, Principal principal,
			SimpMessagingTemplate template) {
		final String customerId = customerUserGroup.getCustomerUserId().toString();
		final String userGroupId = customerUserGroup.getUserGroupId().toString();
		if (isCustomerUserGroupExist(customerId, userGroupId)) {
			return Result.fail(1, "CustomerUserGroup exists");
		}
		if (!hasChatPermission(userGroupId)) {
			return Result.fail(2, "UserGroup has no chat permission");
		}

		final ShiroUser shiroUser = getShiroUser(principal);
		customerUserGroup.setOperatorUserId(UUID.fromString(shiroUser.getId()));
		customerUserGroup = customerUserGroupPair.db().exePost(customerUserGroup);

		final ChatEvent messageEvent = new ChatEvent(shiroUser.getId()).addData("customerId", customerId)
				.addData("userGroupId", userGroupId);
		template.convertAndSend("/topic/chat/addCustomerUserGroup", messageEvent);

		return Result.success().addData("id", customerUserGroup.getId());
	}

	public Result deleteCustomerUserGroup(String id, Principal principal, SimpMessagingTemplate template) {
		final CsmChatCustomerUserGroup customerUserGroup = customerUserGroupPair.db().exeGet(UUID.fromString(id));
		customerUserGroupPair.db().exeDelete(UUID.fromString(id));

		final ShiroUser shiroUser = getShiroUser(principal);
		final ChatEvent messageEvent = new ChatEvent(shiroUser.getId())
				.addData("customerId", customerUserGroup.getCustomerUserId().toString())
				.addData("userGroupId", customerUserGroup.getUserGroupId().toString());
		template.convertAndSend("/topic/chat/removeCustomerUserGroup", messageEvent);
		return Result.success();
	}

	public Result init(Principal principal) {
		final ShiroUser shiroUser = getShiroUser(principal);
		final CsmUserGroup curAdminUserGroup = userCacheService.getUserGroupByUser(shiroUser.getId());
		List<CsmChatCustomerUserGroup> customerUserGroupList = Collections.emptyList();
		if (!curAdminUserGroup.getChat()) {
			customerUserGroupList = getCustomerUserGroupListByUserGroup(curAdminUserGroup.getId().toString());
		}

		final List<ChatUser> customerList = Lists.newArrayList();
		for (CsmUser user : userCacheService.getUserMap().values()) {
			final CsmUserGroup userGroup = userCacheService.getUserGroup(user.getUserGroupId().toString());
			if (userGroup != null) {
				if (!userGroup.getAdmin()) {
					final ChatUser chatUser = new ChatUser(user.getId().toString(), user.getUsername());
					chatUser.setOnline(onlineService.isOnlineUser(chatUser.getUserId()));
					chatUser.setUnhandled(
							isUnhandledCustomer(chatUser.getUserId(), curAdminUserGroup, customerUserGroupList));
					customerList.add(chatUser);
				}
			}
		}
		Collections.sort(customerList, ChatUserComparator.INSTANCE);
		return Result.success().addData("customerList", customerList);
	}

	public Result initMessage(String roomId, Principal principal) {
		final ShiroUser shiroUser = getShiroUser(principal);

		final Result result = Result.success();
		if (canSendMessage(shiroUser.getId(), roomId)) {
			result.addData("messageList", getLatestMessageList(roomId, principal));
		}
		return result;
	}

	public void sendMessage(CsmChatMessage message, Principal principal, SimpMessagingTemplate template) {
		final UUID roomId = message.getRoomId();
		Validate.notNull(roomId, "RoomId should not be null");
		final String roomIdString = roomId.toString();

		final ShiroUser shiroUser = getShiroUser(principal);
		if (!canSendMessage(shiroUser.getId(), roomIdString)) {
			return;
		}

		final String curUserIdString = shiroUser.getId();
		final UUID curUserId = UUID.fromString(curUserIdString);
		message.setSenderId(curUserId);
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		message.setSenderName(shiroUser.getUsername());
		messagePair.db().exePost(message);
		template.convertAndSend("/topic/chat/message/" + roomIdString, message);

		removeUnhandledCustomer(curUserIdString, roomIdString, template);
	}

	public Result upload(CsmChatMessage message, List<MultipartFile> uploadFileList, Principal principal,
			SimpMessagingTemplate template) {
		final UUID roomId = message.getRoomId();
		Validate.notNull(roomId, "RoomId should not be null");
		final String roomIdString = roomId.toString();

		final ShiroUser shiroUser = getShiroUser(principal);
		if (!canSendMessage(shiroUser.getId(), roomIdString)) {
			return Result.fail(1, "You are not allowed to send message to this customer [" + roomIdString + "]");
		}

		final String curUserIdString = shiroUser.getId();
		final UUID curUserId = UUID.fromString(curUserIdString);
		message.setSenderId(curUserId);
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		message.setSenderName(shiroUser.getUsername());
		messagePair.upload().exePost(message, uploadFileList);
		template.convertAndSend("/topic/chat/message/" + roomIdString, message);

		removeUnhandledCustomer(curUserIdString, roomIdString, template);

		return Result.success();
	}

	private void removeUnhandledCustomer(String operatorUserId, String roomId, SimpMessagingTemplate template) {
		final ChatEvent messageEvent = new ChatEvent(operatorUserId).addUserId(roomId);
		unhandledCustomerService.removeUnhandledCustomer(roomId);
		template.convertAndSend("/topic/chat/removeUnhandledCustomer", messageEvent);
	}

	private List<CsmChatMessage> getLatestMessageList(String roomId, Principal principal) {
		final List<CsmChatMessage> dbMessageList = messagePair.db().filterEqual("roomId", roomId)
				.page(1, MESSAGE_PAGE_SIZE, MESSAGE_PAGE_SIZE).sortDesc("createDateTime").exeFindPage().getContent();

		if (Collections3.isEmpty(dbMessageList)) {
			return Collections.emptyList();
		}

		final List<CsmChatMessage> messageList = Lists.newArrayListWithCapacity(dbMessageList.size());
		for (int i = dbMessageList.size() - 1; i >= 0; i--) {
			final CsmChatMessage message = dbMessageList.get(i);
			populateSenderName(message);
			messageList.add(message);
		}
		return messageList;
	}

	private void populateSenderName(CsmChatMessage message) {
		final String senderId = message.getSenderId().toString();
		final String senderName;
		final CsmUser user = userCacheService.getUser(senderId);
		if (user == null) {
			senderName = "Unknown";
		} else {
			senderName = user.getUsername();
		}
		message.setSenderName(senderName);
	}

	private boolean isCustomerUserGroupExist(String customerId, String userGroupId) {
		return customerUserGroupPair.db().filterEqual("customerUserId", customerId)
				.filterEqual("userGroupId", userGroupId).exeCount() > 0;
	}

	private boolean isUnhandledCustomer(String customerId, CsmUserGroup adminUserGroup,
			List<CsmChatCustomerUserGroup> customerUserGroupList) {
		if (!unhandledCustomerService.isUnhandledCustomer(customerId)) {
			return false;
		}

		if (adminUserGroup.getChat()) {
			return true;
		} else {
			if (Collections3.isEmpty(customerUserGroupList)) {
				return false;
			}

			for (CsmChatCustomerUserGroup customerUserGroup : customerUserGroupList) {
				if (customerUserGroup.getCustomerUserId().toString().equals(customerId)) {
					return true;
				}
			}
			return false;
		}
	}

	private List<CsmChatCustomerUserGroup> getCustomerUserGroupListByUserGroup(String userGroupId) {
		return customerUserGroupPair.db().filterEqual("userGroupId", userGroupId).exeFindAll();
	}

	private List<CsmChatCustomerUserGroup> getCustomerUserGroupListByCustomer(String customerUserId) {
		return customerUserGroupPair.db().filterEqual("customerUserId", customerUserId).exeFindAll();
	}

	private boolean canSendMessage(String adminId, String customerId) {
		final CsmUserGroup userGroup = userCacheService.getUserGroupByUser(adminId);
		if (userGroup == null) {
			return false;
		}

		if (isChatOrSuper(userGroup)) {
			return true;
		}

		return isCustomerUserGroupExist(customerId, userGroup.getId().toString());
	}

	private boolean isChatOrSuper(CsmUserGroup userGroup) {
		if (userGroup == null) {
			return false;
		}

		if (userGroup.getChat()) {
			return true;
		}

		final Set<CsmUserGroup> subUserGroupList = userCacheService.getSubUserGroupSet(userGroup.getId().toString());
		if (!Collections3.isEmpty(subUserGroupList)) {
			for (CsmUserGroup subUserGroup : subUserGroupList) {
				if (subUserGroup.getChat()) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean hasChatPermission(String userGroupId) {
		final Set<Permission> permissionSet = userCacheService.getPermissionSetByUserGroup(userGroupId);
		return Shiros.isPermitted(permissionSet, Shiros.toPermission(Perms.getFullPerm("chat")));
	}

	private ShiroUser getShiroUser(Principal principal) {
		return Shiros.getShiroUser(principal);
	}
}
