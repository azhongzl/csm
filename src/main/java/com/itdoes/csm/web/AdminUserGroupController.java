package com.itdoes.csm.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseController;
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
public class AdminUserGroupController extends BaseController {
	@Autowired
	private AdminUserGroupService adminUserGroupService;

	@RequestMapping(value = "listForm", method = RequestMethod.GET)
	public Result listForm() {
		return HttpResults.success(adminUserGroupService.listForm());
	}

	@RequestMapping(value = "postForm", method = RequestMethod.GET)
	public Result postForm() {
		return HttpResults.success(adminUserGroupService.postForm());
	}

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result post(@Valid CsmUserGroup userGroup) {
		return HttpResults.success(adminUserGroupService.post(userGroup));
	}

	@RequestMapping(value = "putForm/{id}", method = RequestMethod.GET)
	public Result putForm(@PathVariable("id") String id) {
		return HttpResults.success(adminUserGroupService.putForm(id));
	}

	@RequestMapping(value = "delete/{id}")
	public Result delete(@PathVariable("id") String id) {
		return HttpResults.success(adminUserGroupService.delete(id));
	}

	@RequestMapping(value = "listUserGroupRoleForm/{id}", method = RequestMethod.GET)
	public Result listUserGroupRoleForm(@PathVariable("id") String id) {
		return HttpResults.success(adminUserGroupService.listUserGroupRoleForm(id));
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
