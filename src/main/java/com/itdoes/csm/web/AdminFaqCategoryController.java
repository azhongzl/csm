package com.itdoes.csm.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmFaqCategory;
import com.itdoes.csm.service.ui.AdminFaqCategoryUiService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/faqCategory", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminFaqCategoryController extends BaseController {
	@Autowired
	private AdminFaqCategoryUiService faqCategoryService;

	@RequestMapping(value = "listForm", method = RequestMethod.GET)
	public Result listForm() {
		return faqCategoryService.listForm();
	}

	@RequestMapping(value = "postForm", method = RequestMethod.GET)
	public Result postForm() {
		return faqCategoryService.postForm();
	}

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result post(@Valid CsmFaqCategory faqCategory) {
		return faqCategoryService.post(faqCategory);
	}

	@RequestMapping(value = "putForm/{id}", method = RequestMethod.GET)
	public Result putForm(@PathVariable("id") String id) {
		return faqCategoryService.putForm(id);
	}

	@RequestMapping(value = "delete/{id}")
	public Result delete(@PathVariable("id") String id) {
		return faqCategoryService.delete(id);
	}
}
