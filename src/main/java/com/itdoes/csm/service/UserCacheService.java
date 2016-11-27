package com.itdoes.csm.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
@Service
public class UserCacheService extends BaseService {
	@Autowired
	private EntityEnv entityEnv;

	@Autowired
	private EntityDbService entityDbService;

	private final Map<String, CsmUser> userMap = Maps.newConcurrentMap();
	private final Set<String> customerIdSet = Sets.newConcurrentHashSet();

	public void init() {
		final List<CsmUser> userList = entityDbService.findAll(entityEnv.getPair(CsmUser.class.getSimpleName()), null,
				null);
		for (CsmUser user : userList) {
			addUser(user);
		}
	}

	public void addUser(CsmUser user) {
		final String userId = user.getId().toString();

		userMap.put(userId, user);

		if (!user.isAdmin()) {
			customerIdSet.add(userId);
		}
	}

	public void updateUser(CsmUser user) {
		final String userId = user.getId().toString();

		userMap.put(userId, user);

		if (!user.isAdmin()) {
			customerIdSet.add(userId);
		} else {
			customerIdSet.remove(userId);
		}
	}

	public void removeUser(String userId) {
		userMap.remove(userId);

		customerIdSet.remove(userId);
	}

	public CsmUser getUser(String userId) {
		return userMap.get(userId);
	}

	public Set<String> getCustomerIdSet() {
		return customerIdSet;
	}
}
