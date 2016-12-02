package com.itdoes.csm.web;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.service.SearchService;
import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmFaq;

/**
 * @author Jalen Zhong
 */
@RestController
public class SearchController extends BaseController {
	private static final String[] FAQ_FIELDS = { "question", "answer" };

	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "/admin/search/createIndex", method = RequestMethod.GET, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	public Result createIndex() {
		searchService.createIndex();
		return HttpResults.success();
	}

	@RequestMapping(value = "/search/CsmFaq", method = RequestMethod.GET, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	public Result searchFaq(@RequestParam(value = "ss") String searchString,
			@RequestParam(value = BaseController.OO_SORT, required = false) String ooSort,
			@RequestParam(value = BaseController.PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = BaseController.PAGE_SIZE, defaultValue = "-1") int pageSize, ServletRequest request) {
		final Page<?> page = searchService.searchDefault(searchString, CsmFaq.class, FAQ_FIELDS,
				buildPageRequest(ooSort, pageNo, pageSize));
		return HttpResults.success(page);
	}
}
