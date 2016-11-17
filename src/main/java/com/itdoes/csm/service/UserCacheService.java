package com.itdoes.csm.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
@Service
public class UserCacheService {
	@Autowired
	private EntityEnv env;

	@Autowired
	private EntityDbService dbService;

	private final Map<String, CsmUser> userMap = Maps.newConcurrentMap();
	private final Set<String> customerIdSet = Sets.newConcurrentHashSet();

	@PostConstruct
	public void init() {
		final List<CsmUser> userList = dbService.findAll(env.getPair(CsmUser.class.getSimpleName()), null, null);
		for (CsmUser user : userList) {
			addUser(user);
		}
	}

	public void addUser(CsmUser user) {
		final String userId = user.getId().toString();

		userMap.put(userId, user);

		if (!user.getAdmin()) {
			customerIdSet.add(userId);
		}
	}

	public void updateUser(CsmUser user) {
		userMap.put(user.getId().toString(), user);
	}

	public void removeUser(String userId) {
		final CsmUser user = userMap.remove(userId);
		if (user == null) {
			return;
		}

		if (!user.getAdmin()) {
			customerIdSet.remove(userId);
		}
	}

	public CsmUser getUser(String userId) {
		return userMap.get(userId);
	}

	public Set<String> getCustomerIdSet() {
		return customerIdSet;
	}
}
