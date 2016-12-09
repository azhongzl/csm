package com.itdoes.csm.service.ui;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.spring.SpringDatas;
import com.itdoes.csm.entity.CsmFaq;
import com.itdoes.csm.entity.CsmFaqCategory;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminFaqUiService extends BaseService {
	@Autowired
	private EntityEnv env;

	private EntityPair<CsmFaqCategory, UUID> faqCategoryPair;
	private EntityPair<CsmFaq, UUID> faqPair;

	@PostConstruct
	public void myInit() {
		faqCategoryPair = env.getPair(CsmFaqCategory.class.getSimpleName());
		faqPair = env.getPair(CsmFaq.class.getSimpleName());
	}

	public Result listForm(String id, int pageNo, int pageSize) {
		return Result.success().addData("faqList",
				faqPair.db().find(faqPair,
						Specifications.build(CsmFaq.class,
								Lists.newArrayList(new FindFilter("categoryId", Operator.EQ, id))),
						SpringDatas.newPageRequest(pageNo, pageSize, DEFAULT_MAX_PAGE_SIZE,
								SpringDatas.newSort("name", true))));
	}
}
