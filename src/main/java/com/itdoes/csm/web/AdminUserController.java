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

	@RequestMapping(value = "listForm", method = RequestMethod.GET)
	public Result listForm(@RequestParam(value = PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = PAGE_SIZE, defaultValue = "-1") int pageSize) {
		return HttpResults.success(adminUserService.listForm(pageNo, pageSize));
	}

	@RequestMapping(value = "postForm", method = RequestMethod.GET)
	public Result postForm() {
		return HttpResults.success(adminUserService.postForm());
	}

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result post(@Valid CsmUser user) {
		return HttpResults.success(adminUserService.post(user));
	}

	@RequestMapping(value = "putForm/{id}", method = RequestMethod.GET)
	public Result putForm(@PathVariable("id") String id) {
		return HttpResults.success(adminUserService.putForm(id));
	}

	@RequestMapping(value = "delete/{id}")
	public Result delete(@PathVariable("id") String id) {
		adminUserService.delete(id);
		return HttpResults.success();
	}
}
