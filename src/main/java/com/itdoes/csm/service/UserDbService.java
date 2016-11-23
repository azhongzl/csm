package com.itdoes.csm.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.service.BaseTransactionalService;
import com.itdoes.csm.dao.CsmUserGroupDao;

/**
 * @author Jalen Zhong
 */
@Service
public class UserDbService extends BaseTransactionalService {
	@Autowired
	private CsmUserGroupDao userGroupDao;

	public Set<String> findPermissionSetByUserGroup(UUID userGroupId) {
		return userGroupDao.findPermission(userGroupId);
	}
}
