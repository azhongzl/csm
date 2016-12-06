package com.itdoes.csm.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseEntityController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.service.AdminUserService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/user", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminUserController extends BaseEntityController {
	@Autowired
	private AdminUserService adminUserService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Result listUsers(@RequestParam(value = PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = PAGE_SIZE, defaultValue = "-1") int pageSize) {
		return HttpResults.success(adminUserService.listUsers(pageNo, pageSize));
	}

	@RequestMapping(value = "listUserGroup", method = RequestMethod.GET)
	public Result listUserGroups() {
		return HttpResults.success(adminUserService.listUserGroups());
	}

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result postUser(@Valid CsmUser user) {
		return HttpResults.success(adminUserService.postUser(user));
	}

	@RequestMapping(value = "put/{id}", method = RequestMethod.GET)
	public Result putUserForm(@PathVariable("id") String id) {
		return HttpResults.success(adminUserService.getUser(id));
	}

	@RequestMapping(value = "delete/{id}")
	public Result deleteUser(@PathVariable("id") String id) {
		adminUserService.deleteUser(id);
		return HttpResults.success();
	}
}
