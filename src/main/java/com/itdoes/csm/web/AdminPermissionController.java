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
import com.itdoes.csm.entity.CsmPermission;
import com.itdoes.csm.service.AdminPermissionService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/permission", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminPermissionController extends BaseController {
	@Autowired
	private AdminPermissionService adminPermissionService;

	@RequestMapping(value = "listForm", method = RequestMethod.GET)
	public Result listForm() {
		return HttpResults.success(adminPermissionService.listForm());
	}

	@RequestMapping(value = "postForm", method = RequestMethod.GET)
	public Result postForm() {
		return HttpResults.success(adminPermissionService.postForm());
	}

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result post(@Valid CsmPermission permission) {
		return HttpResults.success(adminPermissionService.post(permission));
	}

	@RequestMapping(value = "putForm/{id}", method = RequestMethod.GET)
	public Result putForm(@PathVariable("id") String id) {
		return HttpResults.success(adminPermissionService.putForm(id));
	}

	@RequestMapping(value = "delete/{id}")
	public Result delete(@PathVariable("id") String id) {
		adminPermissionService.delete(id);
		return HttpResults.success();
	}
}
