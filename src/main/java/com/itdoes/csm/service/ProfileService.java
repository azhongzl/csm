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
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
@Service
public class ProfileService extends BaseService {
	@Autowired
	private EntityEnv env;

	private EntityPair<CsmUser, UUID> userPair;

	@PostConstruct
	public void myInit() {
		userPair = env.getPair(CsmUser.class.getSimpleName());
	}

	public CsmUser getInternalUser() {
		return userPair.getInternalService().get(userPair, UUID.fromString(Shiros.getShiroUser().getId()));
	}

	public void putUser(CsmUser user, CsmUser oldUser) {
		Validate.isTrue(user.getId().equals(oldUser.getId()), "Cannot modify other user");
		Validate.isTrue(user.getUsername().equals(oldUser.getUsername()), "Cannot modify username");
		Validate.isTrue(user.isActive().equals(oldUser.isActive()), "Cannot modify active");
		Validate.isTrue(user.getUserGroupId().equals(oldUser.getUserGroupId()), "Cannot modify UserGroup");

		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.populatePassword();
			userPair.getExternalService().put(userPair, user, oldUser);
		}
	}
}
