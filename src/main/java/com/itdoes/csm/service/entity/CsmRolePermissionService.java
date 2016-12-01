package com.itdoes.csm.service.entity;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmRolePermission;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
public class CsmRolePermissionService extends EntityService {
	private static final Root ROOT = Root.getInstance();

	@Autowired
	private UserCacheService userCacheService;

	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmRolePermission rolePermission = (CsmRolePermission) entity;

		Validate.isTrue(
				!ROOT.isRootById(rolePermission.getRoleId()) && !ROOT.isRootById(rolePermission.getPermissionId()),
				"Cannot create root RolePermission");

		final ID id = super.post(pair, entity);

		userCacheService.addRolePermission(rolePermission);

		return id;
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmRolePermission rolePermission = (CsmRolePermission) entity;

		Validate.isTrue(!ROOT.isRootById(rolePermission.getRoleId())
				&& !ROOT.isRootById(rolePermission.getPermissionId()) && !ROOT.isRootById(rolePermission.getId()),
				"Cannot modify root RolePermission");

		super.put(pair, entity, oldEntity);

		userCacheService.modifyRolePermission(rolePermission);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root RolePermission");

		super.delete(pair, id, realRootPath, uploadDeleteOrphanFiles);

		userCacheService.removeRolePermission(id.toString());
	}
}
