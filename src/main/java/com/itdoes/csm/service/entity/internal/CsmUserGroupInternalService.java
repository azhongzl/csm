package com.itdoes.csm.service.entity.internal;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.entity.internal.EntityInternalService;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmUserGroup;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
@Service
public class CsmUserGroupInternalService extends EntityInternalService {
	private static final Root ROOT = Root.getInstance();

	@Autowired
	private UserCacheService userCacheService;

	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmUserGroup userGroup = (CsmUserGroup) entity;

		Validate.isTrue(!ROOT.isRootByName(userGroup.getName()), "Cannot create root UserGroup");
		Validate.isTrue(!ROOT.isRootById(userGroup.getSuperId()), "Cannot use root as super UserGroup");

		final ID id = super.post(pair, entity);

		userCacheService.addUserGroup(userGroup);

		return id;
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmUserGroup userGroup = (CsmUserGroup) entity;

		Validate.isTrue(!ROOT.isRootByName(userGroup.getName()) && !ROOT.isRootById(userGroup.getId()),
				"Cannot modify root UserGroup");
		Validate.isTrue(!ROOT.isRootById(userGroup.getSuperId()), "Cannot use root as super UserGroup");
		final Set<CsmUserGroup> subUserGroupSet = userCacheService.getSubUserGroupSet(userGroup.getId().toString());
		if (!Collections3.isEmpty(subUserGroupSet)) {
			for (CsmUserGroup subUserGroup : subUserGroupSet) {
				Validate.isTrue(!subUserGroup.getId().equals(userGroup.getSuperId()),
						"Cannot use descendant [%s] as super UserGroup", subUserGroup.getName());
			}
		}

		super.put(pair, entity, oldEntity);

		userCacheService.modifyUserGroup(userGroup);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root UserGroup");

		super.delete(pair, id);

		userCacheService.removeUserGroup(id.toString());
	}
}
