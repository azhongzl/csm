package com.itdoes.csm.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
public class UserStore {
	private static final Sort USER_SORT = new Sort(Direction.ASC, "username");

	@Autowired
	private EntityEnv env;

	@Autowired
	private EntityDbService dbService;

	private final Map<String, CsmUser> userMap = Maps.newConcurrentMap();
	private final Set<CsmUser> customerSet = Sets.newCopyOnWriteArraySet();

	@PostConstruct
	public void init() {
		final List<CsmUser> userList = dbService.findAll(env.getPair(CsmUser.class.getSimpleName()), null, USER_SORT);
		for (CsmUser user : userList) {
			addUser(user.getId().toString(), user);
		}
	}

	public void addUser(String userId, CsmUser user) {
		userMap.put(userId, user);
		if (!user.getAdmin()) {
			customerSet.add(user);
		}
	}

	public void removeUser(String userId) {
		final CsmUser user = userMap.remove(userId);
		customerSet.remove(user);
	}

	public CsmUser getUser(String userId) {
		return userMap.get(userId);
	}

	public Map<String, CsmUser> getUserMap() {
		return userMap;
	}

	public Set<CsmUser> getCustomerSet() {
		return customerSet;
	}
}
