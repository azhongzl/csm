package com.itdoes.csm.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.MapModel;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmPermission;
import com.itdoes.csm.entity.CsmRole;
import com.itdoes.csm.entity.CsmRolePermission;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminRoleService extends BaseService {
	private static class RoleComparator implements Comparator<CsmRole> {
		private static final RoleComparator INSTANCE = new RoleComparator();

		@Override
		public int compare(CsmRole o1, CsmRole o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	}

	private static class PermissionComparator implements Comparator<CsmPermission> {
		private static final PermissionComparator INSTANCE = new PermissionComparator();

		@Override
		public int compare(CsmPermission o1, CsmPermission o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	}

	private static class RolePermissionDtoComparator implements Comparator<RolePermissionDto> {
		private static final RolePermissionDtoComparator INSTANCE = new RolePermissionDtoComparator();

		@Override
		public int compare(RolePermissionDto o1, RolePermissionDto o2) {
			return o1.getPermission().getName().toLowerCase().compareTo(o2.getPermission().getName().toLowerCase());
		}
	}

	private static class RolePermissionDto {
		private final CsmRolePermission rolePermission;
		private final CsmPermission permission;

		private RolePermissionDto(CsmRolePermission rolePermission, CsmPermission permission) {
			this.rolePermission = rolePermission;
			this.permission = permission;
		}

		@SuppressWarnings("unused")
		public CsmRolePermission getRolePermission() {
			return rolePermission;
		}

		public CsmPermission getPermission() {
			return permission;
		}
	}

	private static final Root ROOT = Root.getInstance();

	@Autowired
	private EntityEnv env;

	private EntityPair<CsmRole, UUID> rolePair;
	private EntityPair<CsmRolePermission, UUID> rolePermissionPair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		rolePair = env.getPair(CsmRole.class.getSimpleName());
		rolePermissionPair = env.getPair(CsmRolePermission.class.getSimpleName());
	}

	public MapModel listForm() {
		final MapModel model = new MapModel();
		final List<CsmRole> roleList = Lists.newArrayListWithCapacity(userCacheService.getRoleMap().size() - 1);
		for (CsmRole role : userCacheService.getRoleMap().values()) {
			if (!ROOT.isRootById(role.getId())) {
				roleList.add(role);
			}
		}
		Collections.sort(roleList, RoleComparator.INSTANCE);
		model.put("roleList", roleList);
		return model;
	}

	public MapModel postForm() {
		return MapModel.emptyMapModel();
	}

	public UUID post(CsmRole role) {
		Validate.isTrue(!ROOT.isRootByName(role.getName()), "Cannot create root Role");

		final UUID id = rolePair.getExternalService().post(rolePair, role);
		userCacheService.addRole(role);
		return id;
	}

	public MapModel putForm(String id) {
		final MapModel model = new MapModel();
		model.put("role", userCacheService.getRole(id));
		return model;
	}

	public CsmRole getEntity(String id) {
		return userCacheService.getRole(id);
	}

	public void put(CsmRole role, CsmRole oldRole) {
		Validate.isTrue(!ROOT.isRootByName(role.getName()) && !ROOT.isRootById(role.getId()),
				"Cannot modify root Role");

		rolePair.getExternalService().put(rolePair, role, oldRole);
		userCacheService.modifyRole(role);
	}

	public void delete(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root Role");

		rolePair.getExternalService().delete(rolePair, UUID.fromString(id));
		userCacheService.removeRole(id);
	}

	public MapModel listRolePermissionForm(String id) {
		final MapModel model = new MapModel();

		final List<CsmPermission> permissionList = Lists
				.newArrayListWithCapacity(userCacheService.getPermissionMap().size() - 1);
		for (CsmPermission permission : userCacheService.getPermissionMap().values()) {
			if (!ROOT.isRootById(permission.getId())) {
				permissionList.add(permission);
			}
		}
		Collections.sort(permissionList, PermissionComparator.INSTANCE);
		model.put("roleList", permissionList);

		final List<RolePermissionDto> rolePermissionDtoList = Lists.newArrayList();
		for (CsmRolePermission rolePermission : userCacheService.getRolePermissionMap().values()) {
			final String roleIdString = rolePermission.getRoleId().toString();
			if (roleIdString.equals(id)) {
				rolePermissionDtoList.add(new RolePermissionDto(rolePermission,
						userCacheService.getPermission(rolePermission.getPermissionId().toString())));
			}
		}
		Collections.sort(rolePermissionDtoList, RolePermissionDtoComparator.INSTANCE);
		model.put("rolePermissionDtoList", rolePermissionDtoList);

		return model;
	}

	public UUID postRolePermission(CsmRolePermission rolePermission) {
		Validate.notNull(rolePermission.getRoleId(), "Role should not be null");
		Validate.notNull(rolePermission.getPermissionId(), "Permission should not be null");
		Validate.isTrue(
				!ROOT.isRootById(rolePermission.getRoleId()) && !ROOT.isRootById(rolePermission.getPermissionId()),
				"Cannot create root RolePermission");

		final UUID id = rolePermissionPair.getExternalService().post(rolePermissionPair, rolePermission);
		userCacheService.addRolePermission(rolePermission);
		return id;
	}

	public void deleteRolePermission(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root RolePermission");

		rolePermissionPair.getExternalService().delete(rolePermissionPair, UUID.fromString(id));
		userCacheService.removeRolePermission(id);
	}
}
