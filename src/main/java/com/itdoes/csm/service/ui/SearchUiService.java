package com.itdoes.csm.service.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.business.service.SearchService;
import com.itdoes.common.core.Result;
import com.itdoes.csm.entity.CsmFaq;

/**
 * @author Jalen Zhong
 */
@Service
public class SearchUiService extends BaseService {
	private static final String[] FAQ_FIELDS = { "question", "answer" };

	@Autowired
	private SearchService searchService;

	public Result searchFaq(String searchString, int pageNo, int pageSize) {
		final Page<?> page = searchService.searchDefault(searchString, CsmFaq.class, FAQ_FIELDS, pageNo, pageSize);
		return Result.success().addData("searchResult", page);
	}
}
