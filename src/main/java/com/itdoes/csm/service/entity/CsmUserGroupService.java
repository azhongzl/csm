package com.itdoes.csm.service.entity;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmUserGroup;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
public class CsmUserGroupService extends EntityService {
	private static final Root ROOT = Root.getInstance();

	@Autowired
	private UserCacheService userCacheService;

	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmUserGroup userGroup = (CsmUserGroup) entity;

		Validate.isTrue(!ROOT.isRootByName(userGroup.getName()), "Cannot create root UserGroup");
		Validate.isTrue(!ROOT.isRootById(userGroup.getSuperId()), "Cannot use root UserGroup as super");

		final ID id = super.post(pair, entity);

		userCacheService.addUserGroup(userGroup);

		return id;
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmUserGroup userGroup = (CsmUserGroup) entity;

		Validate.isTrue(!ROOT.isRootByName(userGroup.getName()) && !ROOT.isRootById(userGroup.getId()),
				"Cannot modify root UserGroup");
		Validate.isTrue(!ROOT.isRootById(userGroup.getSuperId()), "Cannot use root UserGroup as super");

		super.put(pair, entity, oldEntity);

		userCacheService.modifyUserGroup(userGroup);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root UserGroup");

		super.delete(pair, id, realRootPath, uploadDeleteOrphanFiles);

		userCacheService.removeUserGroup(id.toString());
	}
}
