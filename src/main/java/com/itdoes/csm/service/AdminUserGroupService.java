package com.itdoes.csm.service;

import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmUserGroup;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminUserGroupService extends BaseService {
	private static final Root ROOT = Root.getInstance();

	@Autowired
	private EntityEnv env;

	private EntityPair<CsmUserGroup, UUID> pair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		pair = env.getPair(CsmUserGroup.class.getSimpleName());
	}

	public CsmUserGroup getUserGroup(String id) {
		return pair.getExternalService().get(pair, UUID.fromString(id));
	}

	public CsmUserGroup getInternalUserGroup(String id) {
		return pair.getInternalService().get(pair, UUID.fromString(id));
	}

	public UUID postUserGroup(CsmUserGroup userGroup) {
		Validate.isTrue(StringUtils.isNotBlank(userGroup.getName()), "Name should not be blank");
		Validate.notNull(userGroup.isAdmin(), "Admin should not be null");
		Validate.notNull(userGroup.isChat(), "Chat should not be null");
		Validate.isTrue(!ROOT.isRootByName(userGroup.getName()), "Cannot create root UserGroup");
		if (userGroup.getSuperId() != null) {
			Validate.isTrue(!ROOT.isRootById(userGroup.getSuperId()), "Cannot use root as super UserGroup");
		}

		final UUID id = pair.getExternalService().post(pair, userGroup);
		userCacheService.addUserGroup(userGroup);
		return id;
	}

	public void putUserGroup(CsmUserGroup userGroup, CsmUserGroup oldUserGroup) {
		Validate.isTrue(!ROOT.isRootByName(userGroup.getName()) && !ROOT.isRootById(userGroup.getId()),
				"Cannot modify root UserGroup");
		if (userGroup.getSuperId() != null) {
			Validate.isTrue(!ROOT.isRootById(userGroup.getSuperId()), "Cannot use root as super UserGroup");
			final Set<CsmUserGroup> subUserGroupSet = userCacheService.getSubUserGroupSet(userGroup.getId().toString());
			if (!Collections3.isEmpty(subUserGroupSet)) {
				for (CsmUserGroup subUserGroup : subUserGroupSet) {
					Validate.isTrue(!subUserGroup.getId().equals(userGroup.getSuperId()),
							"Cannot use descendant [%s] as super UserGroup", subUserGroup.getName());
				}
			}
		}

		pair.getExternalService().put(pair, userGroup, oldUserGroup);
		userCacheService.modifyUserGroup(userGroup);
	}

	public void deleteUserGroup(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root UserGroup");

		pair.getExternalService().delete(pair, UUID.fromString(id));
		userCacheService.removeUserGroup(id);
	}
}
