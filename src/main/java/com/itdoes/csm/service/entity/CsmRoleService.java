package com.itdoes.csm.service.entity;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmRole;

/**
 * @author Jalen Zhong
 */
@Service
public class CsmRoleService extends EntityService {
	private static final Root ROOT = Root.getInstance();

	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmRole role = (CsmRole) entity;

		Validate.isTrue(!ROOT.isRootByName(role.getName()), "Cannot create root Role");

		final ID id = super.post(pair, entity);
		return id;
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmRole role = (CsmRole) entity;

		Validate.isTrue(!ROOT.isRootByName(role.getName()) && !ROOT.isRootById(role.getId()),
				"Cannot modify root Role");

		super.put(pair, entity, oldEntity);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root Role");

		super.delete(pair, id, realRootPath, uploadDeleteOrphanFiles);
	}
}
