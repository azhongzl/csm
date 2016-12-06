package com.itdoes.csm.service;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminUserService extends BaseService {
	private static final Root ROOT = Root.getInstance();

	@Autowired
	private UserCacheService userCacheService;

	@Autowired
	private EntityEnv env;

	private EntityPair<CsmUser, UUID> pair;

	@PostConstruct
	public void myInit() {
		pair = env.getPair(CsmUser.class.getSimpleName());
	}

	public CsmUser getUser(String id) {
		return pair.getInternalService().get(pair, UUID.fromString(id));
	}

	public UUID postUser(CsmUser user) {
		Validate.isTrue(!ROOT.isRootByName(user.getUsername()), "Cannot create root user");
		Validate.isTrue(!ROOT.isRootById(user.getUserGroupId()), "Cannot assign user to root UserGroup");
		Validate.isTrue(userCacheService.getUserId(user.getUsername()) == null, "User [%s] exists", user.getUsername());
		Validate.isTrue(StringUtils.isNotBlank(user.getPlainPassword()), "Password should not be null");
		user.populatePassword();

		pair.getInternalService().post(pair, user);

		userCacheService.addUser(user);

		return user.getId();
	}

	public void putUser(CsmUser user, CsmUser oldUser) {
		Validate.isTrue(!ROOT.isRootByName(user.getUsername()) && !ROOT.isRootById(user.getId()),
				"Cannot modify root user");
		Validate.isTrue(!ROOT.isRootById(user.getUserGroupId()), "Cannot assign user to root UserGroup");
		Validate.isTrue(user.getUsername().equals(oldUser.getUsername()), "Cannot change username");

		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.populatePassword();
		}

		pair.getInternalService().put(pair, user, oldUser);

		userCacheService.modifyUser(user);
	}

	public void deleteUser(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root user");

		pair.getInternalService().delete(pair, UUID.fromString(id));

		userCacheService.removeUser(id);
	}
}
