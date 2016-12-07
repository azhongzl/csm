package com.itdoes.csm.web;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.service.ui.SearchUiService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/search", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class SearchController extends BaseController {
	@Autowired
	private SearchUiService searchService;

	@RequestMapping(value = "CsmFaq", method = RequestMethod.GET)
	public Result searchFaq(@RequestParam(value = "ss") String searchString,
			@RequestParam(value = BaseController.PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = BaseController.PAGE_SIZE, defaultValue = "-1") int pageSize, ServletRequest request) {
		return searchService.searchFaq(searchString, pageNo, pageSize);
	}
}
