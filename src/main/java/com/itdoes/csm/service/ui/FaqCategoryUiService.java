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
import com.itdoes.csm.entity.CsmFaqCategory;

/**
 * @author Jalen Zhong
 */
@Service
public class FaqCategoryUiService extends BaseService {
	@Autowired
	private EntityEnv env;

	private EntityPair<CsmFaqCategory, UUID> faqCategoryPair;

	@PostConstruct
	public void myInit() {
		faqCategoryPair = env.getPair(CsmFaqCategory.class.getSimpleName());
	}

	public Result listForm() {
		return Result.success().addData("faqCategoryList",
				faqCategoryPair.db().findAll(faqCategoryPair,
						Specifications.build(CsmFaqCategory.class,
								Lists.newArrayList(new FindFilter("active", Operator.EQ, true))),
						SpringDatas.newSort("name", true)));
	}
}
