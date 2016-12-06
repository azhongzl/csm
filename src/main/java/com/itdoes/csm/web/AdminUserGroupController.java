package com.itdoes.csm.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseEntityController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmUserGroup;
import com.itdoes.csm.entity.CsmUserGroupRole;
import com.itdoes.csm.service.AdminUserGroupService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/userGroup", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminUserGroupController extends BaseEntityController {
	@Autowired
	private AdminUserGroupService adminUserGroupService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Result listUserGroups() {
		return HttpResults.success(adminUserGroupService.listUserGroups());
	}

	@RequestMapping(value = "listCandidateSuperUserGroups/{id}", method = RequestMethod.GET)
	public Result listCandidateSuperUserGroups(@PathVariable("id") String id) {
		return HttpResults.success(adminUserGroupService.listCandidateSuperUserGroups(id));
	}

	@RequestMapping(value = "listRoles", method = RequestMethod.GET)
	public Result listRoles() {
		return HttpResults.success(adminUserGroupService.listRoles());
	}

	@RequestMapping(value = "listUserGroupRoles/{id}", method = RequestMethod.GET)
	public Result listUserGroupRoles(@PathVariable("id") String id) {
		return HttpResults.success(adminUserGroupService.listUserGroupRoles(id));
	}

	@RequestMapping(value = "get/{id}", method = RequestMethod.GET)
	public Result getUserGroup(@PathVariable("id") String id) {
		return HttpResults.success(adminUserGroupService.getUserGroup(id));
	}

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result postUserGroup(@Valid CsmUserGroup userGroup) {
		return HttpResults.success(adminUserGroupService.postUserGroup(userGroup));
	}

	@RequestMapping(value = "delete/{id}")
	public Result deleteUserGroup(@PathVariable("id") String id) {
		adminUserGroupService.deleteUserGroup(id);
		return HttpResults.success();
	}

	@RequestMapping(value = "postUserGroupRole", method = RequestMethod.POST)
	public Result postUserGroupRole(@Valid CsmUserGroupRole userGroupRole) {
		return HttpResults.success(adminUserGroupService.postUserGroupRole(userGroupRole));
	}

	@RequestMapping(value = "deleteUserGroupRole/{id}")
	public Result deleteUserGroupRole(@PathVariable("id") String id) {
		adminUserGroupService.deleteUserGroupRole(id);
		return HttpResults.success();
	}
}
