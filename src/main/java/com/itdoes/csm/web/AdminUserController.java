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

	@RequestMapping(method = RequestMethod.POST)
	public Result post(@Valid CsmUser user) {
		return HttpResults.success(adminUserService.postUser(user));
	}

	@RequestMapping(value = "put/{id}", method = RequestMethod.GET)
	public Result putForm(@PathVariable("id") String id) {
		return HttpResults.success(adminUserService.getUser(id));
	}

	@RequestMapping(value = "delete/{id}")
	public Result delete(@PathVariable("id") String id) {
		adminUserService.deleteUser(id);
		return HttpResults.success();
	}
}
