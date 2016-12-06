package com.itdoes.csm.web;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseEntityPutController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.service.ProfileService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/profile", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class ProfilePutController extends BaseEntityPutController {
	@Autowired
	private ProfileService profileService;

	@RequestMapping(method = RequestMethod.POST)
	public Result put(@Valid @ModelAttribute("entity") CsmUser entity, ServletRequest request) {
		profileService.putUser(entity, getOldEntity(request));
		return HttpResults.success();
	}

	@ModelAttribute
	public <T, ID extends Serializable> void getEntity(Model model, ServletRequest request) {
		cacheEntity(request, profileService.getUser());
	}
}
