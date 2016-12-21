package com.itdoes.csm.service.ui;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.csm.entity.CsmFaq;

/**
 * @author Jalen Zhong
 */
@Service
public class FaqUiService extends BaseService {
	@Autowired
	private EntityEnv env;

	private EntityPair<CsmFaq, UUID> faqPair;

	@PostConstruct
	public void myInit() {
		faqPair = env.getPair(CsmFaq.class);
	}

	public Result listForm(String categoryId, int pageNo, int pageSize) {
		return Result.success().addData("faqList", faqPair.db().filterEqual("categoryId", categoryId)
				.page(pageNo, pageSize, DEFAULT_MAX_PAGE_SIZE).sortAsc("question").exeFindPage());
	}
}
