package com.itdoes.csm.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.dto.ChatUser;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
@Service
public class ChatFacadeService {
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
	private UserStoreService userStoreService;

	@Autowired
	private ChatOnlineService onlineService;

	private Map<String, ChatEvent> unhandledCustomerMap = Maps.newConcurrentMap();

	private EntityPair<CsmChatMessage, UUID> messagePair;

	@PostConstruct
	public void init() {
		messagePair = env.getPair(CsmChatMessage.class.getSimpleName());
	}

	public List<ChatUser> getCustomerList() {
		final Set<String> customerIdSet = userStoreService.getCustomerIdSet();
		final List<ChatUser> customerList = Lists.newArrayListWithCapacity(customerIdSet.size());
		for (String customerId : customerIdSet) {
			final ChatUser chatUser = ChatUser.valueOf(userStoreService.getUser(customerId));
			chatUser.setOnline(onlineService.isOnlineUser(customerId));
			chatUser.setUnhandled(unhandledCustomerMap.containsKey(customerId));
			customerList.add(chatUser);
		}
		Collections.sort(customerList, ChatUserComparator.INSTANCE);
		return customerList;
	}

	public void addUnhandledCustomer(ChatEvent event) {
		unhandledCustomerMap.put(event.getUserId(), event);
	}

	public void removeUnhandledCustomer(String userId) {
		unhandledCustomerMap.remove(userId);
	}

	public void saveChatMessage(CsmChatMessage message) {
		dbService.save(messagePair, message);
	}

	public List<CsmChatMessage> getChatMessageList(String roomId) {
		final List<CsmChatMessage> dbMessageList = getChatMessageListFromDb(roomId);
		if (Collections3.isEmpty(dbMessageList)) {
			return Collections.emptyList();
		}

		final List<CsmChatMessage> messageList = Lists.newArrayListWithCapacity(dbMessageList.size());
		for (int i = dbMessageList.size() - 1; i >= 0; i--) {
			final CsmChatMessage message = dbMessageList.get(i);

			final String senderName;
			final CsmUser user = userStoreService.getUser(message.getSenderId().toString());
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
		final List<CsmChatMessage> messageList = dbService
				.find(messagePair, buildMessageSpecification(roomId), MESSAGE_PAGE_REQUEST).getContent();
		return messageList;
	}

	private Specification<CsmChatMessage> buildMessageSpecification(String roomId) {
		return Specifications.build(CsmChatMessage.class,
				Lists.newArrayList(new FindFilter("roomId", Operator.EQ, roomId)));
	}
}
