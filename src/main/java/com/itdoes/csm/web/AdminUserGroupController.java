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
import com.itdoes.csm.service.AdminUserGroupService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/userGroup", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminUserGroupController extends BaseEntityController {
	@Autowired
	private AdminUserGroupService adminUserGroupService;

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result postUserGroup(@Valid CsmUserGroup userGroup) {
		return HttpResults.success(adminUserGroupService.postUserGroup(userGroup));
	}

	@RequestMapping(value = "put/{id}", method = RequestMethod.GET)
	public Result putUserGroupForm(@PathVariable("id") String id) {
		return HttpResults.success(adminUserGroupService.getUserGroup(id));
	}

	@RequestMapping(value = "delete/{id}")
	public Result deleteUserGroup(@PathVariable("id") String id) {
		adminUserGroupService.deleteUserGroup(id);
		return HttpResults.success();
	}
}
