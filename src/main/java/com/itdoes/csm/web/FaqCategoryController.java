package com.itdoes.csm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.csm.service.ui.FaqCategoryUiService;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping("/faqCategory")
public class FaqCategoryController extends BaseController {
	@Autowired
	private FaqCategoryUiService faqCategoryService;

	@RequestMapping(value = "listForm", method = RequestMethod.GET)
	public Result listForm() {
		return faqCategoryService.listForm();
	}
}
