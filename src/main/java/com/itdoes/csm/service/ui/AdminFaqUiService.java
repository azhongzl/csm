package com.itdoes.csm.service.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

/**
 * @author Jalen Zhong
 */
@Service
public class AdminFaqUiService extends BaseService {
	@Autowired
	private EntityEnv env;

	private EntityPair<CsmFaq, UUID> faqPair;

	@PostConstruct
	public void myInit() {
		faqPair = env.getPair(CsmFaq.class.getSimpleName());
	}

	public Result listForm(String categoryId, int pageNo, int pageSize) {
		return Result.success().addData("faqList",
				faqPair.db().find(faqPair,
						Specifications.build(CsmFaq.class,
								Lists.newArrayList(new FindFilter("categoryId", Operator.EQ, categoryId))),
						SpringDatas.newPageRequest(pageNo, pageSize, DEFAULT_MAX_PAGE_SIZE,
								SpringDatas.newSort("name", true))));
	}

	public Result postForm() {
		return Result.success();
	}

	public Result post(CsmFaq faq, List<MultipartFile> uploadFileList) {
		final ShiroUser shiroUser = Shiros.getShiroUser();
		faq.setCreateUserId(UUID.fromString(shiroUser.getId()));
		faq.setCreateDateTime(LocalDateTime.now());
		faq.setModifyUserId(faq.getCreateUserId());
		faq.setModifyDateTime(faq.getCreateDateTime());

		faq = faqPair.upload().post(faqPair, faq, uploadFileList);
		return Result.success().addData("id", faq.getId());
	}

	public Result putForm(String id) {
		return Result.success().addData("faq", getEntity(id));
	}

	public CsmFaq getEntity(String id) {
		return faqPair.db().get(faqPair, UUID.fromString(id));
	}

	public Result put(CsmFaq faq, CsmFaq oldFaq, List<MultipartFile> uploadFileList) {
		final ShiroUser shiroUser = Shiros.getShiroUser();
		faq.setModifyUserId(UUID.fromString(shiroUser.getId()));
		faq.setModifyDateTime(LocalDateTime.now());
		faqPair.upload().put(faqPair, faq, oldFaq, uploadFileList);
		return Result.success();
	}

	public Result delete(String id) {
		faqPair.upload().delete(faqPair, UUID.fromString(id));
		return Result.success();
	}
}
