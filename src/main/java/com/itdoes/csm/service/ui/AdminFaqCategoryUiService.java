package com.itdoes.csm.service.ui;

import java.time.LocalDateTime;
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
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.common.core.spring.SpringDatas;
import com.itdoes.csm.entity.CsmFaq;
import com.itdoes.csm.entity.CsmFaqCategory;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminFaqCategoryUiService extends BaseService {
	@Autowired
	private EntityEnv env;

	private EntityPair<CsmFaqCategory, UUID> faqCategoryPair;
	private EntityPair<CsmFaq, UUID> faqPair;

	@PostConstruct
	public void myInit() {
		faqCategoryPair = env.getPair(CsmFaqCategory.class.getSimpleName());
		faqPair = env.getPair(CsmFaq.class.getSimpleName());
	}

	public Result listForm() {
		return Result.success().addData("faqCategoryList",
				faqCategoryPair.db().findAll(faqCategoryPair,
						Specifications.build(CsmFaqCategory.class,
								Lists.newArrayList(new FindFilter("active", Operator.EQ, true))),
						SpringDatas.newSort("name", true)));
	}

	public Result postForm() {
		return Result.success();
	}

	public Result post(CsmFaqCategory faqCategory) {
		final ShiroUser shiroUser = Shiros.getShiroUser();
		faqCategory.setCreateUserId(UUID.fromString(shiroUser.getId()));
		faqCategory.setCreateDateTime(LocalDateTime.now());
		faqCategory.setModifyUserId(faqCategory.getCreateUserId());
		faqCategory.setModifyDateTime(faqCategory.getCreateDateTime());

		faqCategory = faqCategoryPair.db().post(faqCategoryPair, faqCategory);
		return Result.success().addData("id", faqCategory.getId());
	}

	public Result putForm(String id) {
		return Result.success().addData("faqCategory", getEntity(id));
	}

	public CsmFaqCategory getEntity(String id) {
		return faqCategoryPair.db().get(faqCategoryPair, UUID.fromString(id));
	}

	public Result put(CsmFaqCategory faqCategory, CsmFaqCategory oldFaqCategory) {
		final ShiroUser shiroUser = Shiros.getShiroUser();
		faqCategory.setModifyUserId(UUID.fromString(shiroUser.getId()));
		faqCategory.setModifyDateTime(LocalDateTime.now());
		faqCategoryPair.db().put(faqCategoryPair, faqCategory, oldFaqCategory);
		return Result.success();
	}

	public Result delete(String id) {
		final long count = faqPair.db().count(faqPair,
				Specifications.build(CsmFaq.class, Lists.newArrayList(new FindFilter("categoryId", Operator.EQ, id))));
		if (count > 0) {
			return Result.fail(1, "Cannot delete FaqCategory since it is used by Faq");
		}

		faqCategoryPair.db().delete(faqCategoryPair, UUID.fromString(id));
		return Result.success();
	}
}
