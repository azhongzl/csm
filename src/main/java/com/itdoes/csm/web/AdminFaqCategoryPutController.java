package com.itdoes.csm.web;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BasePutController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmFaqCategory;
import com.itdoes.csm.service.ui.AdminFaqCategoryUiService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/admin/faqCategory", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminFaqCategoryPutController extends BasePutController {
	@Autowired
	private AdminFaqCategoryUiService faqCategoryService;

	@RequestMapping(value = "put", method = RequestMethod.POST)
	public Result put(@Valid @ModelAttribute(ENTITY_KEY) CsmFaqCategory faqCategory, ServletRequest request) {
		return faqCategoryService.put(faqCategory, getOldEntity(request));
	}

	@ModelAttribute
	public void getEntity(@RequestParam("id") String id, Model model, ServletRequest request) {
		cacheEntity(model, request, faqCategoryService.getEntity(id));
	}
}
