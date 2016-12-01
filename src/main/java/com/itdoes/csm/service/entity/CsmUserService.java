package com.itdoes.csm.service.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
@Service
public class CsmUserService extends EntityService {
	private static final Root ROOT = Root.getInstance();

	@Autowired
	private UserCacheService userCacheService;

	@Override
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		final CsmUser user = (CsmUser) entity;

		Validate.isTrue(!ROOT.isRootByName(user.getUsername()), "Cannot create root user");

		Validate.isTrue(StringUtils.isNotBlank(user.getPlainPassword()), "Password should not be null");
		user.populatePassword();

		final ID id = super.post(pair, entity);

		userCacheService.addUser(user);

		return id;
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		final CsmUser user = (CsmUser) entity;
		final CsmUser oldUser = (CsmUser) oldEntity;

		Validate.isTrue(!ROOT.isRootByName(user.getUsername()) && !ROOT.isRootById(user.getId()),
				"Cannot modify root user");
		Validate.isTrue(user.getUsername().equals(oldUser.getUsername()), "Cannot change username");

		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.populatePassword();
		}

		super.put(pair, entity, oldEntity);

		userCacheService.modifyUser(user);
	}

	@Override
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root user");

		super.delete(pair, id, realRootPath, uploadDeleteOrphanFiles);

		userCacheService.removeUser(id.toString());
	}
}
