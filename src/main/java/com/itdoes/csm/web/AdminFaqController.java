package com.itdoes.csm.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmFaq;
import com.itdoes.csm.service.ui.AdminFaqUiService;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping(value = "admin/faq", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminFaqController extends BaseController {
	@Autowired
	private AdminFaqUiService faqService;

	@RequestMapping(value = "listForm/{categoryId}", method = RequestMethod.GET)
	public Result listForm(@PathVariable("categoryId") String categoryId,
			@RequestParam(value = PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = PAGE_SIZE, defaultValue = "-1") int pageSize) {
		return faqService.listForm(categoryId, pageNo, pageSize);
	}

	@RequestMapping(value = "postForm", method = RequestMethod.GET)
	public Result postForm() {
		return faqService.postForm();
	}

	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Result post(@Valid CsmFaq faq, @RequestParam(UPLOAD_FILE) List<MultipartFile> uploadFileList) {
		return faqService.post(faq, uploadFileList);
	}

	@RequestMapping(value = "putForm/{id}", method = RequestMethod.GET)
	public Result putForm(@PathVariable("id") String id) {
		return faqService.putForm(id);
	}

	@RequestMapping(value = "delete/{id}")
	public Result delete(@PathVariable("id") String id) {
		return faqService.delete(id);
	}
}
