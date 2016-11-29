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
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.entity.CsmUserGroup;

/**
 * @author Jalen Zhong
 */
@Service
public class UserCacheService extends BaseService {
	@Autowired
	private EntityEnv entityEnv;

	@Autowired
	private EntityDbService entityDbService;

	private final Map<String, CsmUserGroup> userGroupMap = Maps.newConcurrentMap();
	private final Map<String, CsmUser> userMap = Maps.newConcurrentMap();
	private final Set<String> customerIdSet = Sets.newConcurrentHashSet();

	@PostConstruct
	public void myInit() {
		final List<CsmUserGroup> userGroupList = entityDbService
				.findAll(entityEnv.getPair(CsmUserGroup.class.getSimpleName()), null, null);
		for (CsmUserGroup userGroup : userGroupList) {
			addUserGroup(userGroup);
		}

		final List<CsmUser> userList = entityDbService.findAll(entityEnv.getPair(CsmUser.class.getSimpleName()), null,
				null);
		for (CsmUser user : userList) {
			addUser(user);
		}
	}

	public void addUserGroup(CsmUserGroup userGroup) {
		final String userGroupId = userGroup.getId().toString();
		userGroupMap.put(userGroupId, userGroup);
	}

	public void updateUserGroup(CsmUserGroup userGroup) {
		final String userGroupId = userGroup.getId().toString();
		userGroupMap.put(userGroupId, userGroup);
	}

	public void removeUserGroup(String userGroupId) {
		userGroupMap.remove(userGroupId);
	}

	public CsmUserGroup getUserGroup(String userGroupId) {
		return userGroupMap.get(userGroupId);
	}

	public void addUser(CsmUser user) {
		final String userId = user.getId().toString();

		userMap.put(userId, user);

		if (!userGroupMap.get(user.getUserGroupId().toString()).isAdmin()) {
			customerIdSet.add(userId);
		}
	}

	public void updateUser(CsmUser user) {
		final String userId = user.getId().toString();

		userMap.put(userId, user);

		if (!userGroupMap.get(user.getUserGroupId().toString()).isAdmin()) {
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
