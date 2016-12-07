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
import com.itdoes.csm.entity.CsmRole;
import com.itdoes.csm.entity.CsmRolePermission;
import com.itdoes.csm.service.AdminRoleService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/role", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminRoleController extends BaseController {
	@Autowired
	private AdminRoleService adminRoleService;

	@RequestMapping(value = "listForm", method = RequestMethod.GET)
	public Result listForm() {
		return HttpResults.success(adminRoleService.listForm());
	}

	@RequestMapping(value = "postForm", method = RequestMethod.GET)
	public Result postForm() {
		return HttpResults.success(adminRoleService.postForm());
	}

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result post(@Valid CsmRole role) {
		return HttpResults.success(adminRoleService.post(role));
	}

	@RequestMapping(value = "putForm/{id}", method = RequestMethod.GET)
	public Result putForm(@PathVariable("id") String id) {
		return HttpResults.success(adminRoleService.putForm(id));
	}

	@RequestMapping(value = "listRolePermissionForm/{id}", method = RequestMethod.GET)
	public Result listRolePermissionForm(@PathVariable("id") String id) {
		return HttpResults.success(adminRoleService.listRolePermissionForm(id));
	}

	@RequestMapping(value = "postRolePermission", method = RequestMethod.POST)
	public Result postRolePermission(@Valid CsmRolePermission rolePermission) {
		return HttpResults.success(adminRoleService.postRolePermission(rolePermission));
	}

	@RequestMapping(value = "deleteRolePermission/{id}")
	public Result deleteRolePermission(@PathVariable("id") String id) {
		adminRoleService.deleteRolePermission(id);
		return HttpResults.success();
	}
}
