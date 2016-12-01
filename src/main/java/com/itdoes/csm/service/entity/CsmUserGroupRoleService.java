package com.itdoes.csm.service.entity;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmUserGroupRole;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
public class CsmUserGroupRoleService extends EntityService {
	private static final Root ROOT = Root.getInstance();

	@Autowired
	private UserCacheService userCacheService;

	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmUserGroupRole userGroupRole = (CsmUserGroupRole) entity;

		Validate.isTrue(!ROOT.isRootById(userGroupRole.getUserGroupId()) && !ROOT.isRootById(userGroupRole.getRoleId()),
				"Cannot create root UserGroupRole");

		final ID id = super.post(pair, entity);

		userCacheService.addUserGroupRole(userGroupRole);

		return id;
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmUserGroupRole userGroupRole = (CsmUserGroupRole) entity;

		Validate.isTrue(!ROOT.isRootById(userGroupRole.getUserGroupId()) && !ROOT.isRootById(userGroupRole.getRoleId())
				&& !ROOT.isRootById(userGroupRole.getId()), "Cannot modify root UserGroupRole");

		super.put(pair, entity, oldEntity);

		userCacheService.modifyUserGroupRole(userGroupRole);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root UserGroupRole");

		super.delete(pair, id, realRootPath, uploadDeleteOrphanFiles);

		userCacheService.removeUserGroupRole(id.toString());
	}
}
