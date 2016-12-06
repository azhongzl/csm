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
	private EntityEnv env;

	private EntityPair<CsmUser, UUID> pair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		pair = env.getPair(CsmUser.class.getSimpleName());
	}

	public CsmUser getUser(String id) {
		return pair.getExternalService().get(pair, UUID.fromString(id));
	}

	public CsmUser getInternalUser(String id) {
		return pair.getInternalService().get(pair, UUID.fromString(id));
	}

	public UUID postUser(CsmUser user) {
		Validate.isTrue(StringUtils.isNotBlank(user.getUsername()), "Username should not be blank");
		Validate.notNull(user.isActive(), "Active should not be null");
		Validate.notNull(user.getUserGroupId(), "UserGroup id should not be null");
		Validate.isTrue(userCacheService.getUserId(user.getUsername()) == null, "User [%s] exists", user.getUsername());
		Validate.isTrue(StringUtils.isNotBlank(user.getPlainPassword()), "Password should not be blank");
		Validate.isTrue(!ROOT.isRootByName(user.getUsername()), "Cannot create root User");
		Validate.isTrue(!ROOT.isRootById(user.getUserGroupId()), "Cannot assign user to root UserGroup");

		user.populatePassword();
		final UUID id = pair.getExternalService().post(pair, user);
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
		pair.getExternalService().put(pair, user, oldUser);
		userCacheService.modifyUser(user);
	}

	public void deleteUser(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root User");

		pair.getExternalService().delete(pair, UUID.fromString(id));
		userCacheService.removeUser(id);
	}
}
