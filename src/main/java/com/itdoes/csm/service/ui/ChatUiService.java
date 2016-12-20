package com.itdoes.csm.service.ui;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.service.ChatUnhandledCustomerService;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
@Service
public class ChatUiService extends BaseService {
	private static final String CUSTOMER_SERVICE_NAME = "Customer Service";

	private static final int MESSAGE_PAGE_SIZE = 10;

	@Autowired
	private EntityEnv env;

	@Autowired
	private UserCacheService userCacheService;

	@Autowired
	private ChatUnhandledCustomerService unhandledCustomerService;

	private EntityPair<CsmChatMessage, UUID> messagePair;

	@PostConstruct
	public void myInit() {
		messagePair = env.getPair(CsmChatMessage.class.getSimpleName());
	}

	public Result listHistory(Principal principal) {
		final ShiroUser shiroUser = getShiroUser(principal);

		final List<CsmChatMessage> messageList = messagePair.db().filterEqual("roomId", shiroUser.getId())
				.sortAsc("createDateTime").exeFindList();
		for (CsmChatMessage message : messageList) {
			populateSenderName(message);
		}

		return Result.success().addData("historyList", messageList);
	}

	public Result initMessage(Principal principal) {
		final ShiroUser shiroUser = getShiroUser(principal);
		List<CsmChatMessage> messageList = getLatestMessageList(shiroUser.getId());
		if (Collections3.isEmpty(messageList)) {
			messageList = Lists.newArrayListWithCapacity(1);
		}

		final CsmChatMessage message = new CsmChatMessage();
		message.setMessage("Welcome, " + shiroUser.getUsername() + "! Our agent will contact you soon. Please wait...");
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(true);
		message.setSenderName(CUSTOMER_SERVICE_NAME);
		messageList.add(message);

		return Result.success().addData("messageList", messageList);
	}

	public void sendMessage(CsmChatMessage message, Principal principal, SimpMessagingTemplate template) {
		final ShiroUser shiroUser = getShiroUser(principal);
		final String curUserIdString = shiroUser.getId();
		final UUID curUserId = UUID.fromString(curUserIdString);
		message.setRoomId(curUserId);
		message.setSenderId(curUserId);
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(false);
		message.setSenderName(shiroUser.getUsername());
		messagePair.db().exePost(message);
		template.convertAndSend("/topic/chat/message/" + curUserIdString, message);

		addUnhandledCustomer(curUserIdString, template);
	}

	public Result upload(CsmChatMessage message, List<MultipartFile> uploadFileList, Principal principal,
			SimpMessagingTemplate template) {
		final ShiroUser shiroUser = getShiroUser(principal);
		final String curUserIdString = shiroUser.getId();
		final UUID curUserId = UUID.fromString(curUserIdString);
		message.setRoomId(curUserId);
		message.setSenderId(curUserId);
		message.setCreateDateTime(LocalDateTime.now());
		message.setFromAdmin(false);
		message.setSenderName(shiroUser.getUsername());
		messagePair.upload().exePost(message, uploadFileList);
		template.convertAndSend("/topic/chat/message/" + curUserIdString, message);

		addUnhandledCustomer(curUserIdString, template);

		return Result.success();
	}

	private void addUnhandledCustomer(String operatorUserId, SimpMessagingTemplate template) {
		final ChatEvent messageEvent = new ChatEvent(operatorUserId).addUserId(operatorUserId);
		unhandledCustomerService.addUnhandledCustomer(messageEvent);
		template.convertAndSend("/topic/chat/addUnhandledCustomer", messageEvent);
	}

	private List<CsmChatMessage> getLatestMessageList(String roomId) {
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
		if (message.getFromAdmin()) {
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
	}

	private ShiroUser getShiroUser(Principal principal) {
		return Shiros.getShiroUser(principal);
	}
}
