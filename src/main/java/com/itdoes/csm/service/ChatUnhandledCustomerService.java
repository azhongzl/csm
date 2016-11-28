package com.itdoes.csm.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.core.shutdownhook.ShutdownHookThread;
import com.itdoes.common.core.shutdownhook.ShutdownHookThread.ShutdownHookCallback;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.ChatEvent;
import com.itdoes.csm.entity.CsmChatUnhandledCustomer;

/**
 * @author Jalen Zhong
 */
@Service
public class ChatUnhandledCustomerService extends BaseService {
	@Autowired
	private EntityEnv env;

	@Autowired
	private EntityDbService entityDbService;

	private EntityPair<CsmChatUnhandledCustomer, UUID> pair;

	private final Map<String, ChatEvent> unhandledCustomerMap = Maps.newConcurrentMap();
	{
		ShutdownHookThread.getInstance().register(this, new ShutdownHookCallback() {
			@Override
			public void shutdown() {
				if (Collections3.isEmpty(unhandledCustomerMap)) {
					return;
				}

				for (ChatEvent event : unhandledCustomerMap.values()) {
					final CsmChatUnhandledCustomer unhandledCustomer = new CsmChatUnhandledCustomer();
					unhandledCustomer.setUserId(UUID.fromString(event.getUserId()));
					unhandledCustomer.setCreateDateTime(LocalDateTime.now());
					entityDbService.save(pair, unhandledCustomer);
				}
			}
		});
	}

	@PostConstruct
	public void myInit() {
		pair = env.getPair(CsmChatUnhandledCustomer.class.getSimpleName());
		final List<CsmChatUnhandledCustomer> unhandledCustomerList = entityDbService.findAll(pair, null, null);
		if (Collections3.isEmpty(unhandledCustomerList)) {
			return;
		}

		for (CsmChatUnhandledCustomer unhandledCustomer : unhandledCustomerList) {
			final String userIdString = unhandledCustomer.getId().toString();
			unhandledCustomerMap.put(userIdString, new ChatEvent(userIdString, unhandledCustomer.getCreateDateTime()));
		}

		entityDbService.deleteAll(pair);
	}

	public boolean hasUnhandledCustomers() {
		return !Collections3.isEmpty(unhandledCustomerMap);
	}

	public boolean hasUnhandledCustomer(String userId) {
		return unhandledCustomerMap.containsKey(userId);
	}

	public void addUnhandledCustomer(ChatEvent event) {
		unhandledCustomerMap.put(event.getUserId(), event);
	}

	public void removeUnhandledCustomer(String userId) {
		unhandledCustomerMap.remove(userId);
	}
}
