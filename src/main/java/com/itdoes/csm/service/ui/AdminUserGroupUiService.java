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

	private static final Root ROOT = Root.getInstance();

	@Autowired
	private EntityEnv env;

	private EntityPair<CsmUserGroup, UUID> userGroupPair;
	private EntityPair<CsmUserGroupRole, UUID> userGroupRolePair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		userGroupPair = env.getPair(CsmUserGroup.class.getSimpleName());
		userGroupRolePair = env.getPair(CsmUserGroupRole.class.getSimpleName());
	}

	public Result listForm() {
		return Result.success().addData("userGroupList", getUserGroupList());
	}

	public Result postForm() {
		return Result.success().addData("superUserGroupList", getUserGroupList());
	}

	public Result post(CsmUserGroup userGroup) {
		Validate.isTrue(StringUtils.isNotBlank(userGroup.getName()), "Name should not be blank");
		Validate.isTrue(!ROOT.isRootByName(userGroup.getName()), "Cannot create root UserGroup");
		if (userGroup.getSuperId() != null) {
			Validate.isTrue(!ROOT.isRootById(userGroup.getSuperId()), "Cannot use root as super UserGroup");
		}

		final UUID id = userGroupPair.external().post(userGroupPair, userGroup);
		userCacheService.addUserGroup(userGroup);
		return Result.success().addData("id", id);
	}

	public Result putForm(String id) {
		return Result.success().addData("userGroup", userCacheService.getUserGroup(id)).addData("superUserGroupList",
				getCandidateSuperUserGroupList(id));
	}

	public CsmUserGroup getEntity(String id) {
		return userCacheService.getUserGroup(id);
	}

	public Result put(CsmUserGroup userGroup, CsmUserGroup oldUserGroup) {
		Validate.isTrue(!ROOT.isRootByName(userGroup.getName()) && !ROOT.isRootById(userGroup.getId()),
				"Cannot modify root UserGroup");
		if (userGroup.getSuperId() != null) {
			Validate.isTrue(!ROOT.isRootById(userGroup.getSuperId()), "Cannot use root as super UserGroup");
			final Set<CsmUserGroup> subUserGroupSet = userCacheService.getSubUserGroupSet(userGroup.getId().toString());
			if (!Collections3.isEmpty(subUserGroupSet)) {
				for (CsmUserGroup subUserGroup : subUserGroupSet) {
					Validate.isTrue(!subUserGroup.getId().equals(userGroup.getSuperId()),
							"Cannot use descendant [%s] as super UserGroup", subUserGroup.getName());
				}
			}
		}

		userGroupPair.external().put(userGroupPair, userGroup, oldUserGroup);
		userCacheService.modifyUserGroup(userGroup);
		return Result.success();
	}

	public Result delete(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root UserGroup");

		final Set<CsmUserGroup> subUserGroupSet = userCacheService.getSubUserGroupSet(id);
		if (subUserGroupSet.size() > 1) {
			return Result.fail(1, "UserGroup has children");
		}

		for (CsmUser user : userCacheService.getUserMap().values()) {
			if (user.getUserGroupId().toString().equals(id)) {
				return Result.fail(2, "UserGroup has user");
			}
		}

		userGroupPair.external().delete(userGroupPair, UUID.fromString(id));
		userCacheService.removeUserGroup(id);
		return Result.success();
	}

	public Result listUserGroupRoleForm(String id) {
		final List<CsmRole> roleList = Lists.newArrayListWithCapacity(userCacheService.getRoleMap().size() - 1);
		for (CsmRole role : userCacheService.getRoleMap().values()) {
			if (!ROOT.isRootById(role.getId())) {
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

		return Result.success().addData("roleList", roleList).addData("userGroupRoleDtoList", userGroupRoleDtoList);
	}

	public Result postUserGroupRole(CsmUserGroupRole userGroupRole) {
		Validate.notNull(userGroupRole.getUserGroupId(), "UserGroup should not be null");
		Validate.notNull(userGroupRole.getRoleId(), "Role should not be null");
		Validate.isTrue(!ROOT.isRootById(userGroupRole.getUserGroupId()) && !ROOT.isRootById(userGroupRole.getRoleId()),
				"Cannot create root UserGroupRole");

		final UUID id = userGroupRolePair.external().post(userGroupRolePair, userGroupRole);
		userCacheService.addUserGroupRole(userGroupRole);
		return Result.success().addData("id", id);
	}

	public Result deleteUserGroupRole(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root UserGroupRole");

		userGroupRolePair.external().delete(userGroupRolePair, UUID.fromString(id));
		userCacheService.removeUserGroupRole(id);
		return Result.success();
	}

	private List<CsmUserGroup> getUserGroupList() {
		final List<CsmUserGroup> userGroupList = Lists
				.newArrayListWithCapacity(userCacheService.getUserGroupMap().size() - 1);
		for (CsmUserGroup userGroup : userCacheService.getUserGroupMap().values()) {
			if (!ROOT.isRootById(userGroup.getId())) {
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
			if (!ROOT.isRootById(userGroup.getId()) && !subUserGroupSet.contains(userGroup)) {
				userGroupList.add(userGroup);
			}
		}
		Collections.sort(userGroupList, UserGroupComparator.INSTANCE);
		return userGroupList;
	}
}
