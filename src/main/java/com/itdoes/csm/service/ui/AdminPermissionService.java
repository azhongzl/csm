package com.itdoes.csm.service.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmPermission;
import com.itdoes.csm.entity.CsmRolePermission;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminPermissionService extends BaseService {
	private static class PermissionComparator implements Comparator<CsmPermission> {
		private static final PermissionComparator INSTANCE = new PermissionComparator();

		@Override
		public int compare(CsmPermission o1, CsmPermission o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	}

	private static final Root ROOT = Root.getInstance();

	@Autowired
	private EntityEnv env;

	private EntityPair<CsmPermission, UUID> permissionPair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		permissionPair = env.getPair(CsmPermission.class.getSimpleName());
	}

	public Result listForm() {
		final List<CsmPermission> permissionList = Lists
				.newArrayListWithCapacity(userCacheService.getPermissionMap().size() - 1);
		for (CsmPermission permission : userCacheService.getPermissionMap().values()) {
			if (!ROOT.isRootById(permission.getId())) {
				permissionList.add(permission);
			}
		}
		Collections.sort(permissionList, PermissionComparator.INSTANCE);
		return Result.success().addData("permissionList", permissionList);
	}

	public Result postForm() {
		return Result.success();
	}

	public Result post(CsmPermission permission) {
		Validate.isTrue(StringUtils.isNotBlank(permission.getName()), "Permission name should not be blank");
		Validate.isTrue(StringUtils.isNotBlank(permission.getPermission()), "Permission value should not be blank");
		Validate.isTrue(!ROOT.isRootByName(permission.getName()), "Cannot create root Permission");

		final UUID id = permissionPair.external().post(permissionPair, permission);
		userCacheService.addPermission(permission);
		return Result.success().addData("id", id);
	}

	public Result putForm(String id) {
		return Result.success().addData("permission", userCacheService.getPermission(id));
	}

	public CsmPermission getEntity(String id) {
		return userCacheService.getPermission(id);
	}

	public Result put(CsmPermission permission, CsmPermission oldPermission) {
		Validate.isTrue(!ROOT.isRootByName(permission.getName()) && !ROOT.isRootById(permission.getId()),
				"Cannot modify root Permission");

		permissionPair.external().put(permissionPair, permission, oldPermission);
		userCacheService.modifyPermission(permission);
		return Result.success();
	}

	public Result delete(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root Permission");

		for (CsmRolePermission rolePermission : userCacheService.getRolePermissionMap().values()) {
			if (rolePermission.getPermissionId().toString().equals(id)) {
				return Result.fail(2, "Permission is used by Role");
			}
		}

		permissionPair.external().delete(permissionPair, UUID.fromString(id));
		userCacheService.removePermission(id);
		return Result.success();
	}
}
