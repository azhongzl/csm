package com.itdoes.csm.web;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.service.SearchService;
import com.itdoes.common.business.service.SearchService.SearchEntity;
import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.business.web.SearchController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.Faq;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = SearchController.SEARCH_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class CustomSearchController extends BaseController {
	public static final String SEARCH_COMMAND_FAQ = "faq";

	private static final SearchEntity FAQ_SEARCH_ENTITY = new SearchEntity(Faq.class,
			new String[] { "question", "answer" });

	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "/" + CustomSearchController.SEARCH_COMMAND_FAQ, method = RequestMethod.GET)
	public Result searchFaq(@RequestParam(value = "ss") String searchString,
			@RequestParam(value = "page_no", defaultValue = "1") int pageNo,
			@RequestParam(value = "page_size", defaultValue = "-1") int pageSize,
			@RequestParam(value = "page_sort", required = false) String pageSort, ServletRequest request) {
		final List<?> resultList = searchService.search(searchString, FAQ_SEARCH_ENTITY, 0, 10);
		return HttpResults.success(resultList);
	}
}
