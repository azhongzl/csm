package com.itdoes.csm.service.ui;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
@Service
public class ProfileUiService extends BaseService {
	@Autowired
	private EntityEnv env;

	private EntityPair<CsmUser, UUID> userPair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		userPair = env.getPair(CsmUser.class.getSimpleName());
	}

	public Result putForm() {
		return Result.success().addData("user", userCacheService.getUser(Shiros.getShiroUser().getId()));
	}

	public CsmUser getEntity() {
		return userCacheService.getUser(Shiros.getShiroUser().getId());
	}

	public Result put(CsmUser user, CsmUser oldUser) {
		Validate.isTrue(user.getId().equals(oldUser.getId()), "Cannot modify other user");
		Validate.isTrue(user.getUsername().equals(oldUser.getUsername()), "Cannot modify username");
		Validate.isTrue(user.getActive().equals(oldUser.getActive()), "Cannot modify active");
		Validate.isTrue(user.getUserGroupId().equals(oldUser.getUserGroupId()), "Cannot modify UserGroup");

		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.populatePassword();
			userPair.db().put(userPair, user, oldUser);
			userCacheService.modifyUser(user);
		}

		return Result.success();
	}
}
