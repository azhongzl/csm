package com.itdoes.csm.web;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseEntityPutController;
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
public class AdminUserPutController extends BaseEntityPutController {
	@Autowired
	private AdminUserService adminUserService;

	@RequestMapping(value = "put", method = RequestMethod.POST)
	public Result putUser(@Valid @ModelAttribute(ENTITY_KEY) CsmUser user, ServletRequest request) {
		adminUserService.putUser(user, getOldEntity(request));
		return HttpResults.success();
	}

	@ModelAttribute
	public void getEntity(@RequestParam("id") String id, ServletRequest request) {
		cacheEntity(request, adminUserService.getInternalUser(id));
	}
}
