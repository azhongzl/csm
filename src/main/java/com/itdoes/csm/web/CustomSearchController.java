package com.itdoes.csm.web;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Env;
import com.itdoes.common.business.service.FacadeTransactionalService;
import com.itdoes.common.business.service.SearchService;
import com.itdoes.common.business.service.SearchService.SearchEntity;
import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.business.web.SearchController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.Faq;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = SearchController.URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class CustomSearchController extends BaseController {
	private static final SearchEntity FAQ_SEARCH_ENTITY = new SearchEntity(Faq.class,
			new String[] { "question", "answer" });

	@Autowired
	private SearchService searchService;

	// TODO following should be removed if switching to search()
	@Autowired
	private FacadeTransactionalService facadeService;
	@Autowired
	private Env env;
	private EntityPair<Faq, UUID> pair;

	@PostConstruct
	public void myInit() {
		pair = env.getEntityPair(Faq.class.getSimpleName());
	}

	// TODO should be switched to search() which is full text search
	@RequestMapping(method = RequestMethod.GET)
	public <T, ID extends Serializable> Result find(@RequestParam(value = "ss") String searchString,
			@RequestParam(value = "page_no", defaultValue = "1") int pageNo,
			@RequestParam(value = "page_size", defaultValue = "-1") int pageSize,
			@RequestParam(value = "page_sort", required = false) String pageSort, ServletRequest request) {
		final Page<Faq> page = facadeService.find(pair,
				Specifications.build(pair.getEntityClass(),
						Lists.newArrayList(new FindFilter("question", Operator.LIKE, searchString),
								new FindFilter("answer", Operator.LIKE, searchString))),
				buildPageRequest(pageNo, pageSize, pageSort));
		return HttpResults.success(page);
	}

	public Result search(@RequestParam(value = "ss") String searchString,
			@RequestParam(value = "page_no", defaultValue = "1") int pageNo,
			@RequestParam(value = "page_size", defaultValue = "-1") int pageSize,
			@RequestParam(value = "page_sort", required = false) String pageSort, ServletRequest request) {
		final List<?> resultList = searchService.search(searchString, FAQ_SEARCH_ENTITY, 0, 10);
		return HttpResults.success(resultList);
	}
}
