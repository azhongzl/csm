package com.itdoes.csm.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.entity.CsmChatUnhandledCustomer;

/**
 * @author Jalen Zhong
 */
@Service
public class ChatUnhandledCustomerService extends BaseService {
	@Autowired
	private EntityEnv entityEnv;

	private EntityPair<CsmChatUnhandledCustomer, UUID> chatUnhandledCustomerPair;

	private final Map<String, ChatEvent> unhandledCustomerMap = Maps.newConcurrentMap();

	@PostConstruct
	public void myInit() {
		chatUnhandledCustomerPair = entityEnv.getPair(CsmChatUnhandledCustomer.class.getSimpleName());

		final List<CsmChatUnhandledCustomer> unhandledCustomerList = chatUnhandledCustomerPair.internal()
				.findAll(chatUnhandledCustomerPair, null, null);
		if (Collections3.isEmpty(unhandledCustomerList)) {
			return;
		}

		for (CsmChatUnhandledCustomer unhandledCustomer : unhandledCustomerList) {
			final ChatEvent event = new ChatEvent(unhandledCustomer.getUserId().toString(),
					unhandledCustomer.getCreateDateTime());
			addUnhandledCustomer(event);
		}

		chatUnhandledCustomerPair.internal().deleteIterable(chatUnhandledCustomerPair, unhandledCustomerList);
	}

	@PreDestroy
	public void myDestory() {
		if (Collections3.isEmpty(unhandledCustomerMap)) {
			return;
		}

		final List<CsmChatUnhandledCustomer> unhandledCustomerList = Lists
				.newArrayListWithCapacity(unhandledCustomerMap.size());
		for (ChatEvent event : unhandledCustomerMap.values()) {
			final CsmChatUnhandledCustomer unhandledCustomer = new CsmChatUnhandledCustomer();
			unhandledCustomer.setUserId(UUID.fromString(event.getUserId()));
			unhandledCustomer.setCreateDateTime(event.getDateTime());
			unhandledCustomerList.add(unhandledCustomer);
		}

		chatUnhandledCustomerPair.internal().postIterable(chatUnhandledCustomerPair, unhandledCustomerList);
	}

	public boolean hasUnhandledCustomers() {
		return !Collections3.isEmpty(unhandledCustomerMap);
	}

	public boolean isUnhandledCustomer(String userId) {
		return unhandledCustomerMap.containsKey(userId);
	}

	public void addUnhandledCustomer(ChatEvent event) {
		unhandledCustomerMap.put(event.getUserId(), event);
	}

	public void removeUnhandledCustomer(String userId) {
		unhandledCustomerMap.remove(userId);
	}
}
