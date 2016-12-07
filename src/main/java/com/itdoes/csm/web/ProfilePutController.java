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

import com.itdoes.common.business.web.BasePutController;
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
public class ProfilePutController extends BasePutController {
	@Autowired
	private ProfileService profileService;

	@RequestMapping(value = "put", method = RequestMethod.POST)
	public Result put(@Valid @ModelAttribute(ENTITY_KEY) CsmUser user, ServletRequest request) {
		profileService.put(user, getOldEntity(request));
		return HttpResults.success();
	}

	@ModelAttribute
	public <T, ID extends Serializable> void getEntity(Model model, ServletRequest request) {
		cacheEntity(model, request, profileService.getEntity());
	}
}
