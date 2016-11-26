package com.itdoes.csm.service;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.service.BaseTransactionalService;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dao.CsmUserGroupDao;

/**
 * @author Jalen Zhong
 */
@Service
public class UserDbService extends BaseTransactionalService {
	@Autowired
	private CsmUserGroupDao userGroupDao;

	public Set<String> findPermissionSetByUserGroup(UUID userGroupId) {
		final Set<String> dbPermRowSet = userGroupDao.findPermission(userGroupId);
		if (Collections3.isEmpty(dbPermRowSet)) {
			return Collections.emptySet();
		}

		final Set<String> permissionSet = Sets.newHashSet();
		for (String dbPermRow : dbPermRowSet) {
			if (StringUtils.isBlank(dbPermRow)) {
				continue;
			}

			final String[] dbPermRowItems = StringUtils.split(dbPermRow);
			if (Collections3.isEmpty(dbPermRowItems)) {
				continue;
			}

			for (String dbPermRowItem : dbPermRowItems) {
				if (StringUtils.isBlank(dbPermRowItem)) {
					continue;
				}

				permissionSet.add(Perms.getFullPerm(dbPermRowItem));
			}
		}
		return permissionSet;
	}
}
