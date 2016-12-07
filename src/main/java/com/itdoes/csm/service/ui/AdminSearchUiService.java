package com.itdoes.csm.service.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.business.service.SearchService;
import com.itdoes.common.core.Result;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminSearchUiService extends BaseService {
	@Autowired
	private SearchService searchService;

	public Result createIndex() {
		searchService.createIndex();
		return Result.success();
	}
}
