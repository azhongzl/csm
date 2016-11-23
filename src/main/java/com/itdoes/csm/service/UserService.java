package com.itdoes.csm.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dao.CsmUserGroupDao;
import com.itdoes.csm.entity.CsmPermission;
import com.itdoes.csm.entity.CsmRolePermission;
import com.itdoes.csm.entity.CsmUserGroup;
import com.itdoes.csm.entity.CsmUserGroupRole;

/**
 * @author Jalen Zhong
 */
@Service
public class UserService {
	@Autowired
	private EntityEnv env;

	@Autowired
	private EntityDbService dbService;
	
	@Autowired
	private CsmUserGroupDao ugDao;
	
	@PostConstruct
	public void init(){
		getPermissionSet1(UUID.fromString("1c93b13d-d6ea-1034-a268-c6b53a0158b7"));
	}
	
	public Set<String> getPermissionSet1(UUID userGroupId){
		Set<String> permissionSet = ugDao.findPermissionById(userGroupId);
		System.out.println(permissionSet);
		return null;
	}

	public Set<String> getPermissionSet(UUID userGroupId) {
		final Set<UUID> subUserGroupIdSet = getSubUserGroupIdSet(userGroupId);
		final Set<UUID> roleIdSet = Sets.newHashSet();
		for (UUID subUserGroupId : subUserGroupIdSet) {
			final List<CsmUserGroupRole> userGroupRoleList = dbService
					.findAll(env.getPair(CsmUserGroupRole.class.getSimpleName()),
							Specifications.build(CsmUserGroupRole.class,
									Lists.newArrayList(new FindFilter("userGroupId", Operator.EQ, subUserGroupId))),
							null);
			for (CsmUserGroupRole userGroupRole : userGroupRoleList) {
				roleIdSet.add(userGroupRole.getRoleId());
			}
		}
		final Set<UUID> permissionIdSet = Sets.newHashSet();
		for (UUID roleId : roleIdSet) {
			final List<CsmRolePermission> rolePermissionList = dbService.findAll(
					env.getPair(CsmRolePermission.class.getSimpleName()), Specifications.build(CsmRolePermission.class,
							Lists.newArrayList(new FindFilter("roldId", Operator.EQ, roleId))),
					null);
			for (CsmRolePermission rolePermission : rolePermissionList) {
				permissionIdSet.add(rolePermission.getPermissionId());
			}
		}
		final Set<String> permissionSet = Sets.newHashSet();
		for (UUID permissionId : permissionIdSet) {
			final CsmPermission permission = dbService.get(env.getPair(CsmPermission.class.getSimpleName()),
					permissionId);
			permissionSet.add(permission.getPermission());
		}

		return permissionSet;
	}

	private Set<UUID> getSubUserGroupIdSet(UUID userGroupId) {
		final Set<UUID> userGroupSet = Sets.newHashSet();
		populateSubUserGroupIdSet(userGroupId, userGroupSet);
		return userGroupSet;
	}

	private void populateSubUserGroupIdSet(UUID userGroupId, Set<UUID> userGroupIdSet) {
		userGroupIdSet.add(userGroupId);
		final List<CsmUserGroup> subUserGroupList = dbService.findAll(env.getPair(CsmUserGroup.class.getSimpleName()),
				Specifications.build(CsmUserGroup.class,
						Lists.newArrayList(new FindFilter("superId", Operator.EQ, userGroupId))),
				null);
		if (Collections3.isEmpty(subUserGroupList)) {
			return;
		}
		for (CsmUserGroup subUserGroup : subUserGroupList) {
			populateSubUserGroupIdSet(subUserGroup.getId(), userGroupIdSet);
		}
	}
}
