package com.itdoes.csm.service.ui;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.shiro.Shiros;
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
		faqCategoryPair = env.getPair(CsmFaqCategory.class);
		faqPair = env.getPair(CsmFaq.class);
	}

	public Result listForm() {
		return Result.success().addData("faqCategoryList", faqCategoryPair.db().sortAsc("name").exeFindAll());
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

		faqCategory = faqCategoryPair.db().exePost(faqCategory);
		return Result.success().addData("id", faqCategory.getId());
	}

	public Result putForm(String id) {
		return Result.success().addData("faqCategory", getEntity(id));
	}

	public CsmFaqCategory getEntity(String id) {
		return faqCategoryPair.db().exeGet(UUID.fromString(id));
	}

	public Result put(CsmFaqCategory faqCategory, CsmFaqCategory oldFaqCategory) {
		final ShiroUser shiroUser = Shiros.getShiroUser();
		faqCategory.setModifyUserId(UUID.fromString(shiroUser.getId()));
		faqCategory.setModifyDateTime(LocalDateTime.now());
		faqCategoryPair.db().exePut(faqCategory, oldFaqCategory);
		return Result.success();
	}

	public Result delete(String id) {
		final long count = faqPair.db().filterEqual("categoryId", id).exeCount();
		if (count > 0) {
			return Result.fail(1, "Cannot delete FaqCategory since it is used by Faq");
		}

		faqCategoryPair.db().exeDelete(UUID.fromString(id));
		return Result.success();
	}
}
