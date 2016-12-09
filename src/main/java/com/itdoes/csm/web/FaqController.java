package com.itdoes.csm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.service.ui.FaqUiService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/faq", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FaqController extends BaseController {
	@Autowired
	private FaqUiService faqService;

	@RequestMapping(value = "listForm/{categoryId}", method = RequestMethod.GET)
	public Result listForm(@PathVariable("categoryId") String categoryId,
			@RequestParam(value = PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = PAGE_SIZE, defaultValue = "-1") int pageSize) {
		return faqService.listForm(categoryId, pageNo, pageSize);
	}
}
