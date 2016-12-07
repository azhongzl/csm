package com.itdoes.csm.web;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseEntityPutController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmRole;
import com.itdoes.csm.service.AdminRoleService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/role", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminRolePutController extends BaseEntityPutController {
	@Autowired
	private AdminRoleService adminRoleService;

	@RequestMapping(value = "put", method = RequestMethod.POST)
	public Result put(@Valid @ModelAttribute(ENTITY_KEY) CsmRole role, ServletRequest request) {
		adminRoleService.put(role, getOldEntity(request));
		return HttpResults.success();
	}

	@ModelAttribute
	public void getEntity(@RequestParam("id") String id, Model model, ServletRequest request) {
		cacheEntity(model, request, adminRoleService.getEntity(id));
	}
}
