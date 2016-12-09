package com.itdoes.csm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.service.ui.FaqCategoryUiService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/faqCategory", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FaqCategoryController extends BaseController {
	@Autowired
	private FaqCategoryUiService faqCategoryService;

	@RequestMapping(value = "listForm", method = RequestMethod.GET)
	public Result listForm() {
		return faqCategoryService.listForm();
	}
}
