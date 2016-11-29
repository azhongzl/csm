package com.itdoes.csm.service.entity;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.csm.entity.CsmPermission;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
public class CsmPermissionService extends EntityService {
	@Autowired
	private UserCacheService userCacheService;

	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmPermission permission = (CsmPermission) entity;

		final ID id = super.post(pair, entity);

		userCacheService.addPermission(permission);

		return id;
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmPermission permission = (CsmPermission) entity;

		super.put(pair, entity, oldEntity);

		userCacheService.modifyPermission(permission);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		super.delete(pair, id, realRootPath, uploadDeleteOrphanFiles);

		userCacheService.removePermission(id.toString());
	}
}
