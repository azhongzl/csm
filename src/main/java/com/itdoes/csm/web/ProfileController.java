package com.itdoes.csm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseEntityController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.service.ProfileService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/profile", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class ProfileController extends BaseEntityController {
	@Autowired
	private ProfileService profileService;

	@RequestMapping(method = RequestMethod.GET)
	public Result putForm() {
		return HttpResults.success(profileService.getUser());
	}
}
