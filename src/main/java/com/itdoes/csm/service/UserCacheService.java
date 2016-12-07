package com.itdoes.csm.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.entity.CsmPermission;
import com.itdoes.csm.entity.CsmRole;
import com.itdoes.csm.entity.CsmRolePermission;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.entity.CsmUserGroup;
import com.itdoes.csm.entity.CsmUserGroupRole;

/**
 * @author Jalen Zhong
 */
@Service
public class UserCacheService extends BaseService {
	@Autowired
	private EntityEnv entityEnv;

	private final Map<String, CsmUserGroup> userGroupMap = Maps.newConcurrentMap();
	private final Map<String, CsmUserGroupRole> userGroupRoleMap = Maps.newConcurrentMap();
	private final Map<String, CsmRole> roleMap = Maps.newConcurrentMap();
	private final Map<String, CsmRolePermission> rolePermissionMap = Maps.newConcurrentMap();
	private final Map<String, CsmPermission> permissionMap = Maps.newHashMap();
	private final Map<String, Set<String>> permissionSetMap = Maps.newConcurrentMap();
	private final Map<String, CsmUser> userMap = Maps.newConcurrentMap();
	private final Map<String, String> usernameIdMap = Maps.newConcurrentMap();

	@PostConstruct
	public void myInit() {
		final EntityPair<CsmUserGroup, UUID> userGroupPair = entityEnv.getPair(CsmUserGroup.class.getSimpleName());
		final List<CsmUserGroup> userGroupList = userGroupPair.internal().findAll(userGroupPair, null, null);
		for (CsmUserGroup userGroup : userGroupList) {
			addUserGroup(userGroup);
		}

		final EntityPair<CsmUserGroupRole, UUID> userGroupRolePair = entityEnv
				.getPair(CsmUserGroupRole.class.getSimpleName());
		final List<CsmUserGroupRole> userGroupRoleList = userGroupRolePair.internal().findAll(userGroupRolePair, null,
				null);
		for (CsmUserGroupRole userGroupRole : userGroupRoleList) {
			addUserGroupRole(userGroupRole);
		}

		final EntityPair<CsmRole, UUID> rolePair = entityEnv.getPair(CsmRole.class.getSimpleName());
		final List<CsmRole> roleList = rolePair.internal().findAll(rolePair, null, null);
		for (CsmRole role : roleList) {
			addRole(role);
		}

		final EntityPair<CsmRolePermission, UUID> rolePermissionPair = entityEnv
				.getPair(CsmRolePermission.class.getSimpleName());
		final List<CsmRolePermission> rolePermissionList = rolePermissionPair.internal().findAll(rolePermissionPair,
				null, null);
		for (CsmRolePermission rolePermission : rolePermissionList) {
			addRolePermission(rolePermission);
		}

		final EntityPair<CsmPermission, UUID> permissionPair = entityEnv.getPair(CsmPermission.class.getSimpleName());
		final List<CsmPermission> permissionList = permissionPair.internal().findAll(permissionPair, null, null);
		for (CsmPermission permission : permissionList) {
			addPermission(permission);
		}

		final EntityPair<CsmUser, UUID> userPair = entityEnv.getPair(CsmUser.class.getSimpleName());
		final List<CsmUser> userList = userPair.internal().findAll(userPair, null, null);
		for (CsmUser user : userList) {
			addUser(user);
		}
	}

	public void addUserGroup(CsmUserGroup userGroup) {
		userGroupMap.put(userGroup.getId().toString(), userGroup);
	}

	public void modifyUserGroup(CsmUserGroup userGroup) {
		userGroupMap.put(userGroup.getId().toString(), userGroup);
	}

	public void removeUserGroup(String userGroupId) {
		userGroupMap.remove(userGroupId);
	}

	public Map<String, CsmUserGroup> getUserGroupMap() {
		return userGroupMap;
	}

	public CsmUserGroup getUserGroup(String userGroupId) {
		if (StringUtils.isBlank(userGroupId)) {
			return null;
		}

		return userGroupMap.get(userGroupId);
	}

	public CsmUserGroup getUserGroupByUser(String userId) {
		final CsmUser user = getUser(userId);
		if (user == null) {
			return null;
		}

		return getUserGroup(user.getUserGroupId().toString());
	}

	public Set<CsmUserGroup> getSubUserGroupSet(String userGroupId) {
		if (StringUtils.isBlank(userGroupId)) {
			return Collections.emptySet();
		}

		final Set<CsmUserGroup> userGroupSet = Sets.newHashSet();
		recurseSubUserGroup(getUserGroup(userGroupId), userGroupSet);
		return userGroupSet;
	}

	private void recurseSubUserGroup(CsmUserGroup mainUserGroup, Set<CsmUserGroup> userGroupSet) {
		if (mainUserGroup == null) {
			return;
		}

		userGroupSet.add(mainUserGroup);
		for (CsmUserGroup userGroup : userGroupMap.values()) {
			if (mainUserGroup.getId().equals(userGroup.getSuperId())) {
				recurseSubUserGroup(userGroup, userGroupSet);
			}
		}
	}

	public void addUserGroupRole(CsmUserGroupRole userGroupRole) {
		userGroupRoleMap.put(userGroupRole.getId().toString(), userGroupRole);
	}

	public void modifyUserGroupRole(CsmUserGroupRole userGroupRole) {
		userGroupRoleMap.put(userGroupRole.getId().toString(), userGroupRole);
	}

	public void removeUserGroupRole(String id) {
		userGroupRoleMap.remove(id);
	}

	public Map<String, CsmUserGroupRole> getUserGroupRoleMap() {
		return userGroupRoleMap;
	}

	public void addRole(CsmRole role) {
		roleMap.put(role.getId().toString(), role);
	}

	public void modifyRole(CsmRole role) {
		roleMap.put(role.getId().toString(), role);
	}

	public void removeRole(String id) {
		roleMap.remove(id);
	}

	public Map<String, CsmRole> getRoleMap() {
		return roleMap;
	}

	public CsmRole getRole(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}

		return roleMap.get(id);
	}

	public void addRolePermission(CsmRolePermission rolePermission) {
		rolePermissionMap.put(rolePermission.getId().toString(), rolePermission);
	}

	public void modifyRolePermission(CsmRolePermission rolePermission) {
		rolePermissionMap.put(rolePermission.getId().toString(), rolePermission);
	}

	public void removeRolePermission(String id) {
		rolePermissionMap.remove(id);
	}

	public Map<String, CsmRolePermission> getRolePermissionMap() {
		return rolePermissionMap;
	}

	public void addPermission(CsmPermission permission) {
		final String permissionIdString = permission.getId().toString();
		permissionMap.put(permissionIdString, permission);
		permissionSetMap.put(permissionIdString, getPermissionSet(permission));
	}

	public void modifyPermission(CsmPermission permission) {
		final String permissionIdString = permission.getId().toString();
		permissionMap.put(permissionIdString, permission);
		permissionSetMap.put(permissionIdString, getPermissionSet(permission));
	}

	public void removePermission(String id) {
		permissionMap.remove(id);
		permissionSetMap.remove(id);
	}

	public Map<String, CsmPermission> getPermissionMap() {
		return permissionMap;
	}

	public CsmPermission getPermission(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}

		return permissionMap.get(id);
	}

	public Set<String> getPermissionSetByUser(String userId) {
		final CsmUser user = getUser(userId);
		if (user == null) {
			return Collections.emptySet();
		}

		return getPermissionSetByUserGroup(user.getUserGroupId().toString());
	}

	private Set<String> getPermissionSetByUserGroup(String userGroupId) {
		final Set<String> result = Sets.newHashSet();
		final Set<CsmUserGroup> userGroupSet = getSubUserGroupSet(userGroupId);
		for (CsmUserGroup userGroup : userGroupSet) {
			for (CsmUserGroupRole userGroupRole : userGroupRoleMap.values()) {
				if (userGroupRole.getUserGroupId().equals(userGroup.getId())) {
					for (CsmRolePermission rolePermission : rolePermissionMap.values()) {
						if (rolePermission.getRoleId().equals(userGroupRole.getRoleId())) {
							final Set<String> permissionSet = permissionSetMap
									.get(rolePermission.getPermissionId().toString());
							if (!Collections3.isEmpty(permissionSet)) {
								result.addAll(permissionSet);
							}
						}
					}
				}
			}
		}
		return result;
	}

	private Set<String> getPermissionSet(CsmPermission permission) {
		if (permission == null) {
			return Collections.emptySet();
		}

		final Set<String> result = Sets.newHashSet();
		final String permRow = permission.getPermission();
		if (StringUtils.isNotBlank(permRow)) {
			final String[] permRowItems = StringUtils.split(permRow, ",");
			if (!Collections3.isEmpty(permRowItems)) {
				for (String permRowItem : permRowItems) {
					if (StringUtils.isNotBlank(permRowItem)) {
						result.add(Perms.getFullPerm(permRowItem));
					}
				}
			}
		}
		return result;
	}

	public void addUser(CsmUser user) {
		final String userIdString = user.getId().toString();
		userMap.put(userIdString, user);
		usernameIdMap.put(user.getUsername(), userIdString);
	}

	public void modifyUser(CsmUser user) {
		final String userIdString = user.getId().toString();
		userMap.put(userIdString, user);
		usernameIdMap.put(user.getUsername(), userIdString);
	}

	public void removeUser(String userId) {
		final CsmUser user = userMap.remove(userId);
		if (user != null) {
			usernameIdMap.remove(user.getUsername());
		}
	}

	public Map<String, CsmUser> getUserMap() {
		return userMap;
	}

	public CsmUser getUser(String userId) {
		if (StringUtils.isBlank(userId)) {
			return null;
		}

		return userMap.get(userId);
	}

	public String getUserId(String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}

		return usernameIdMap.get(username);
	}
}
