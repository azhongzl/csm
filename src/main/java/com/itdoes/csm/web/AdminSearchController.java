package com.itdoes.csm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.service.SearchService;
import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/search", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminSearchController extends BaseController {
	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "createIndex", method = RequestMethod.GET)
	public Result createIndex() {
		searchService.createIndex();
		return HttpResults.success();
	}
}
