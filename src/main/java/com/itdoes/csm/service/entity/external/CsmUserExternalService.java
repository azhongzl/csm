package com.itdoes.csm.service.entity.external;

import java.io.Serializable;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.entity.EntityPermCommand;
import com.itdoes.common.business.service.entity.external.EntityExternalPermFieldService;
import com.itdoes.common.business.service.entity.external.EntityExternalService;
import com.itdoes.common.business.service.entity.internal.EntityInternalService;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.util.Reflections;
import com.itdoes.csm.codegenerator.entity.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
public class CsmUserExternalService extends EntityExternalService {
	public CsmUserExternalService(EntityInternalService internalService,
			EntityExternalPermFieldService permFieldService) {
		super(internalService, permFieldService);
	}

	@Override
	public <T, ID extends Serializable> T get(EntityPair<T, ID> pair, ID id) {
		boolean permitted = false;
		final Subject subject = SecurityUtils.getSubject();
		if (subject.isPermitted(
				Perms.getEntityOneEntityClassPerm(CsmUser.class.getSimpleName(), EntityPermCommand.Command.GET))) {
			permitted = true;
		} else if (subject.isRemembered() || subject.isAuthenticated()) {
			final ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
			if (shiroUser.getId().equals(id.toString())) {
				permitted = true;
			}
		}

		if (permitted) {
			return super.get(pair, id);
		} else {
			return null;
		}
	}

	@Override
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		boolean permitted = false;
		final Subject subject = SecurityUtils.getSubject();
		if (subject.isPermitted(
				Perms.getEntityOneEntityClassPerm(CsmUser.class.getSimpleName(), EntityPermCommand.Command.PUT))) {
			permitted = true;
		} else if (subject.isRemembered() || subject.isAuthenticated()) {
			final ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
			if (shiroUser.getId().equals(Reflections.getFieldValue(entity, pair.getIdField()).toString())) {
				permitted = true;
			}
		}

		if (permitted) {
			super.put(pair, entity, oldEntity);
		}
	}
}
