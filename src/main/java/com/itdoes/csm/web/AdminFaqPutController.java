package com.itdoes.csm.web;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.web.BasePutController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmFaq;
import com.itdoes.csm.service.ui.AdminFaqUiService;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "admin/faq", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class AdminFaqPutController extends BasePutController {
	@Autowired
	private AdminFaqUiService faqService;

	@RequestMapping(value = "put", method = RequestMethod.POST)
	public Result put(@Valid @ModelAttribute(ENTITY_KEY) CsmFaq faq,
			@RequestParam(UPLOAD_FILE) List<MultipartFile> uploadFileList, ServletRequest request) {
		return faqService.put(faq, getOldEntity(request), uploadFileList);
	}

	@ModelAttribute
	public void getEntity(@RequestParam("id") String id, Model model, ServletRequest request) {
		cacheEntity(model, request, faqService.getEntity(id));
	}
}
