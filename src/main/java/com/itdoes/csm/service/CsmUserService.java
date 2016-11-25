package com.itdoes.csm.service;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
@Service
public class CsmUserService extends EntityService {
	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmUser user = (CsmUser) entity;

		Validate.isTrue(StringUtils.isNotBlank(user.getPlainPassword()), "Password should not be null");
		user.populatePassword();

		return super.post(pair, entity);
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmUser user = (CsmUser) entity;

		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.populatePassword();
		}

		super.put(pair, entity, oldEntity);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		super.delete(pair, id, realRootPath, uploadDeleteOrphanFiles);
	}
}
