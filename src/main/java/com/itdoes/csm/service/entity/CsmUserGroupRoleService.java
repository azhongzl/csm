package com.itdoes.csm.service.entity;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.csm.entity.CsmUserGroupRole;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
public class CsmUserGroupRoleService extends EntityService {
	@Autowired
	private UserCacheService userCacheService;

	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmUserGroupRole userGroupRole = (CsmUserGroupRole) entity;

		final ID id = super.post(pair, entity);

		userCacheService.addUserGroupRole(userGroupRole);

		return id;
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmUserGroupRole userGroupRole = (CsmUserGroupRole) entity;

		super.put(pair, entity, oldEntity);

		userCacheService.modifyUserGroupRole(userGroupRole);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		super.delete(pair, id, realRootPath, uploadDeleteOrphanFiles);

		userCacheService.removeUserGroupRole(id.toString());
	}
}
