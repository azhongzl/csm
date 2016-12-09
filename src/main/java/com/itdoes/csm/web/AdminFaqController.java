package com.itdoes.csm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.csm.service.ui.AdminFaqUiService;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping("/faqCategory")
public class AdminFaqController extends BaseController {
	@Autowired
	private AdminFaqUiService faqService;

	@RequestMapping(value = "listForm/{id}", method = RequestMethod.GET)
	public Result listForm(String id, @RequestParam(value = PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = PAGE_SIZE, defaultValue = "-1") int pageSize) {
		return faqService.listForm(id, pageNo, pageSize);
	}
}
