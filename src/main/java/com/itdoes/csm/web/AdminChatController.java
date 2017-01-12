package com.itdoes.csm.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.csm.entity.CsmChatCustomerUserGroup;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.service.ui.AdminChatUiService;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping("/admin/chat")
public class AdminChatController extends BaseController {
	@Autowired
	private AdminChatUiService chatService;

	private final SimpMessagingTemplate template;

	@Autowired
	public AdminChatController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@RequestMapping
	public String chat(Principal principal, Model model) {
		return "admin/chat";
	}

	@RequestMapping("icon")
	public String chatIcon(
			@RequestParam(value = FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, required = false) String username,
			@RequestParam(value = "iconWidth", required = false) String iconWidth,
			@RequestParam(value = "iconHeight", required = false) String iconHeight, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute("iconWidth", iconWidth);
		model.addAttribute("iconHeight", iconHeight);
		return "admin/chatIcon";
	}

	@RequestMapping(value = "getCurrentUserGroupPair", method = RequestMethod.GET, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result getCurrentUserGroupPair(Principal principal) {
		return chatService.getCurrentUserGroupPair(principal);
	}

	@RequestMapping(value = "listHistory/{customerId}", method = RequestMethod.GET, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result listHistory(@PathVariable("customerId") String customerId,
			@RequestParam(value = "beginDateTime", required = false) LocalDateTime beginDateTime,
			@RequestParam(value = "endDateTime", required = false) LocalDateTime endDateTime,
			@RequestParam(value = PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = PAGE_SIZE, defaultValue = "-1") int pageSize, Principal principal) {
		return chatService.listHistory(customerId, beginDateTime, endDateTime, pageNo, pageSize, principal);
	}

	@RequestMapping(value = "hasUnhandledCustomers", method = RequestMethod.GET, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result hasUnhandledCustomers(Principal principal) {
		return chatService.hasUnhandledCustomers(principal);
	}

	@RequestMapping(value = "listCustomerUserGroups/{id}", method = RequestMethod.GET, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result listCustomerUserGroups(@PathVariable("id") String id, Principal principal) {
		return chatService.listCustomerUserGroups(id, principal);
	}

	@RequestMapping(value = "postCustomerUserGroup", method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result postCustomerUserGroup(@Valid CsmChatCustomerUserGroup chatCustomerUserGroup, Principal principal) {
		return chatService.postCustomerUserGroup(chatCustomerUserGroup, principal, template);
	}

	@RequestMapping(value = "deleteCustomerUserGroup/{id}", produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result deleteCustomerUserGroup(@PathVariable("id") String id, Principal principal) {
		return chatService.deleteCustomerUserGroup(id, principal, template);
	}

	@SubscribeMapping("/chatAInit")
	public Result init(Principal principal) {
		return chatService.init(principal);
	}

	@SubscribeMapping("/chatAInitMessage/{roomId}")
	public Result initMessage(@DestinationVariable String roomId, Principal principal) {
		return chatService.initMessage(roomId, principal);
	}

	@MessageMapping("/chatASendMessage")
	public void sendMessage(CsmChatMessage message, Principal principal) {
		chatService.sendMessage(message, principal, template);
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result upload(@Valid CsmChatMessage message, @RequestParam(UPLOAD_FILE) List<MultipartFile> uploadFileList,
			Principal principal) {
		return chatService.upload(message, uploadFileList, principal, template);
	}
}
