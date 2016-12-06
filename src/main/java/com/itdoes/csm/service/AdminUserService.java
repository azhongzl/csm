package com.itdoes.csm.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.spring.SpringDatas;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.entity.CsmUserGroup;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminUserService extends BaseService {
	private static final Root ROOT = Root.getInstance();

	@Autowired
	private EntityEnv env;

	private EntityPair<CsmUser, UUID> userPair;
	private EntityPair<CsmUserGroup, UUID> userGroupPair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		userPair = env.getPair(CsmUser.class.getSimpleName());
		userGroupPair = env.getPair(CsmUserGroup.class.getSimpleName());
	}

	public Page<CsmUser> listUsers(int pageNo, int pageSize) {

		return userPair.getExternalService().find(userPair,
				Specifications.build(CsmUser.class,
						Lists.newArrayList(new FindFilter("id", Operator.NEQ, ROOT.getIdString()))),
				SpringDatas.newPageRequest(pageNo, pageSize, DEFAULT_MAX_PAGE_SIZE,
						SpringDatas.newSort(true, "username")));
	}

	public List<CsmUserGroup> listUserGroups() {
		return userGroupPair.getExternalService().findAll(userGroupPair,
				Specifications.build(CsmUserGroup.class,
						Lists.newArrayList(new FindFilter("id", Operator.NEQ, ROOT.getIdString()))),
				SpringDatas.newSort(true, "name"));
	}

	public CsmUser getUser(String id) {
		return userPair.getExternalService().get(userPair, UUID.fromString(id));
	}

	public CsmUser getInternalUser(String id) {
		return userPair.getInternalService().get(userPair, UUID.fromString(id));
	}

	public UUID postUser(CsmUser user) {
		Validate.isTrue(StringUtils.isNotBlank(user.getUsername()), "Username should not be blank");
		Validate.notNull(user.getActive(), "Active should not be null");
		Validate.notNull(user.getUserGroupId(), "UserGroup id should not be null");
		Validate.isTrue(userCacheService.getUserId(user.getUsername()) == null, "User [%s] exists", user.getUsername());
		Validate.isTrue(StringUtils.isNotBlank(user.getPlainPassword()), "Password should not be blank");
		Validate.isTrue(!ROOT.isRootByName(user.getUsername()), "Cannot create root User");
		Validate.isTrue(!ROOT.isRootById(user.getUserGroupId()), "Cannot assign user to root UserGroup");

		user.populatePassword();
		final UUID id = userPair.getExternalService().post(userPair, user);
		userCacheService.addUser(user);
		return id;
	}

	public void putUser(CsmUser user, CsmUser oldUser) {
		Validate.isTrue(user.getUsername().equals(oldUser.getUsername()), "Cannot modify username");
		Validate.isTrue(!ROOT.isRootByName(user.getUsername()) && !ROOT.isRootById(user.getId()),
				"Cannot modify root User");
		Validate.isTrue(!ROOT.isRootById(user.getUserGroupId()), "Cannot assign user to root UserGroup");

		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.populatePassword();
		}
		userPair.getExternalService().put(userPair, user, oldUser);
		userCacheService.modifyUser(user);
	}

	public void deleteUser(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root User");

		userPair.getExternalService().delete(userPair, UUID.fromString(id));
		userCacheService.removeUser(id);
	}
}
