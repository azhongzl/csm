package com.itdoes.csm.service.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmRole;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.entity.CsmUserGroup;
import com.itdoes.csm.entity.CsmUserGroupRole;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminUserGroupUiService extends BaseService {
	private static class UserGroupComparator implements Comparator<CsmUserGroup> {
		private static final UserGroupComparator INSTANCE = new UserGroupComparator();

		@Override
		public int compare(CsmUserGroup o1, CsmUserGroup o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	}

	private static class RoleComparator implements Comparator<CsmRole> {
		private static final RoleComparator INSTANCE = new RoleComparator();

		@Override
		public int compare(CsmRole o1, CsmRole o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	}

	private static class UserGroupRoleDtoComparator implements Comparator<UserGroupRoleDto> {
		private static final UserGroupRoleDtoComparator INSTANCE = new UserGroupRoleDtoComparator();

		@Override
		public int compare(UserGroupRoleDto o1, UserGroupRoleDto o2) {
			return o1.getRole().getName().toLowerCase().compareTo(o2.getRole().getName().toLowerCase());
		}
	}

	private static class UserGroupRoleDto {
		private final CsmUserGroupRole userGroupRole;
		private final CsmRole role;

		private UserGroupRoleDto(CsmUserGroupRole userGroupRole, CsmRole role) {
			this.userGroupRole = userGroupRole;
			this.role = role;
		}

		@SuppressWarnings("unused")
		public CsmUserGroupRole getUserGroupRole() {
			return userGroupRole;
		}

		public CsmRole getRole() {
			return role;
		}
	}

	private static class UserGroupDto {
		private final CsmUserGroup userGroup;
		private final CsmUserGroup superUserGroup;

		public UserGroupDto(CsmUserGroup userGroup, CsmUserGroup superUserGroup) {
			this.userGroup = userGroup;
			this.superUserGroup = superUserGroup;
		}

		@SuppressWarnings("unused")
		public CsmUserGroup getUserGroup() {
			return userGroup;
		}

		@SuppressWarnings("unused")
		public CsmUserGroup getSuperUserGroup() {
			return superUserGroup;
		}
	}

	@Autowired
	private EntityEnv env;

	private EntityPair<CsmUserGroup, UUID> userGroupPair;
	private EntityPair<CsmUserGroupRole, UUID> userGroupRolePair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		userGroupPair = env.getPair(CsmUserGroup.class);
		userGroupRolePair = env.getPair(CsmUserGroupRole.class);
	}

	public Result listForm() {
		final List<CsmUserGroup> csmUserGroupList = getUserGroupList();
		final List<UserGroupDto> userGroupList = Lists.newArrayListWithCapacity(csmUserGroupList.size());
		for (CsmUserGroup csmUserGroup : csmUserGroupList) {
			final UUID superUserGroupId = csmUserGroup.getSuperId();
			final CsmUserGroup superUserGroup = superUserGroupId == null ? null
					: userCacheService.getUserGroup(superUserGroupId.toString());
			userGroupList.add(new UserGroupDto(csmUserGroup, superUserGroup));
		}
		return Result.success().addData("userGroupList", userGroupList);
	}

	public Result postForm() {
		return Result.success().addData("superUserGroupList", getUserGroupList());
	}

	public Result post(CsmUserGroup userGroup) {
		Validate.isTrue(StringUtils.isNotBlank(userGroup.getName()), "Name should not be blank");
		Validate.isTrue(!Root.isRootByName(userGroup.getName()), "Cannot create root UserGroup");
		if (userGroup.getSuperId() != null) {
			Validate.isTrue(!Root.isRootById(userGroup.getSuperId()), "Cannot use root as super UserGroup");
		}

		userGroup = userGroupPair.db().exePost(userGroup);
		userCacheService.addUserGroup(userGroup);
		return Result.success().addData("id", userGroup.getId());
	}

	public Result putForm(String id) {
		return Result.success().addData("userGroup", userCacheService.getUserGroup(id)).addData("superUserGroupList",
				getCandidateSuperUserGroupList(id));
	}

	public CsmUserGroup getEntity(String id) {
		return userCacheService.getUserGroup(id);
	}

	public Result put(CsmUserGroup userGroup, CsmUserGroup oldUserGroup) {
		Validate.isTrue(!Root.isRootByName(userGroup.getName()) && !Root.isRootById(userGroup.getId()),
				"Cannot modify root UserGroup");
		if (userGroup.getSuperId() != null) {
			Validate.isTrue(!Root.isRootById(userGroup.getSuperId()), "Cannot use root as super UserGroup");
			final Set<CsmUserGroup> subUserGroupSet = userCacheService.getSubUserGroupSet(userGroup.getId().toString());
			if (!Collections3.isEmpty(subUserGroupSet)) {
				for (CsmUserGroup subUserGroup : subUserGroupSet) {
					Validate.isTrue(!subUserGroup.getId().equals(userGroup.getSuperId()),
							"Cannot use descendant [%s] as super UserGroup", subUserGroup.getName());
				}
			}
		}

		userGroupPair.db().exePut(userGroup, oldUserGroup);
		userCacheService.modifyUserGroup(userGroup);
		return Result.success();
	}

	public Result delete(String id) {
		Validate.isTrue(!Root.isRootById(id), "Cannot remove root UserGroup");

		final Set<CsmUserGroup> subUserGroupSet = userCacheService.getSubUserGroupSet(id);
		if (subUserGroupSet.size() > 1) {
			return Result.fail(1, "UserGroup has children");
		}

		for (CsmUser user : userCacheService.getUserMap().values()) {
			if (user.getUserGroupId().toString().equals(id)) {
				return Result.fail(2, "UserGroup has user");
			}
		}

		userGroupPair.db().exeDelete(UUID.fromString(id));
		userCacheService.removeUserGroup(id);
		return Result.success();
	}

	public Result listUserGroupRoleForm(String id) {
		final List<CsmRole> roleList = Lists.newArrayListWithCapacity(userCacheService.getRoleMap().size() - 1);
		for (CsmRole role : userCacheService.getRoleMap().values()) {
			if (!Root.isRootById(role.getId())) {
				roleList.add(role);
			}
		}
		Collections.sort(roleList, RoleComparator.INSTANCE);

		final List<UserGroupRoleDto> userGroupRoleDtoList = Lists.newArrayList();
		for (CsmUserGroupRole userGroupRole : userCacheService.getUserGroupRoleMap().values()) {
			final String userGroupIdString = userGroupRole.getUserGroupId().toString();
			if (userGroupIdString.equals(id)) {
				userGroupRoleDtoList.add(new UserGroupRoleDto(userGroupRole,
						userCacheService.getRole(userGroupRole.getRoleId().toString())));
			}
		}
		Collections.sort(userGroupRoleDtoList, UserGroupRoleDtoComparator.INSTANCE);

		return Result.success().addData("roleList", roleList).addData("userGroupRoleList", userGroupRoleDtoList);
	}

	public Result postUserGroupRole(CsmUserGroupRole userGroupRole) {
		Validate.notNull(userGroupRole.getUserGroupId(), "UserGroup should not be null");
		Validate.notNull(userGroupRole.getRoleId(), "Role should not be null");
		Validate.isTrue(!Root.isRootById(userGroupRole.getUserGroupId()) && !Root.isRootById(userGroupRole.getRoleId()),
				"Cannot create root UserGroupRole");

		userGroupRole = userGroupRolePair.db().exePost(userGroupRole);
		userCacheService.addUserGroupRole(userGroupRole);
		return Result.success().addData("id", userGroupRole.getId());
	}

	public Result deleteUserGroupRole(String id) {
		Validate.isTrue(!Root.isRootById(id), "Cannot remove root UserGroupRole");

		userGroupRolePair.db().exeDelete(UUID.fromString(id));
		userCacheService.removeUserGroupRole(id);
		return Result.success();
	}

	private List<CsmUserGroup> getUserGroupList() {
		final List<CsmUserGroup> userGroupList = Lists
				.newArrayListWithCapacity(userCacheService.getUserGroupMap().size() - 1);
		for (CsmUserGroup userGroup : userCacheService.getUserGroupMap().values()) {
			if (!Root.isRootById(userGroup.getId())) {
				userGroupList.add(userGroup);
			}
		}
		Collections.sort(userGroupList, UserGroupComparator.INSTANCE);
		return userGroupList;
	}

	private List<CsmUserGroup> getCandidateSuperUserGroupList(String id) {
		final List<CsmUserGroup> userGroupList = Lists.newArrayList();
		final Set<CsmUserGroup> subUserGroupSet = userCacheService.getSubUserGroupSet(id);
		for (CsmUserGroup userGroup : userCacheService.getUserGroupMap().values()) {
			if (!Root.isRootById(userGroup.getId()) && !subUserGroupSet.contains(userGroup)) {
				userGroupList.add(userGroup);
			}
		}
		Collections.sort(userGroupList, UserGroupComparator.INSTANCE);
		return userGroupList;
	}
}
